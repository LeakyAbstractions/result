
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#flatMapSuccess(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult flatMapSuccess")
class LazyResult_flatMapSuccess_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> fail("Should not happen");
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Result<String, String>> mapper = s -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.flatMapSuccess(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class);
    }

    @Test
    void should_eventually_flat_map_when_needed() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Success<>(SUCCESS);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Result<String, String>> mapper = s -> new Failure<>(FAILURE);
        // When
        final Optional<String> value = lazy.flatMapSuccess(mapper).optionalFailure();
        // Then
        assertThat(value).containsSame(FAILURE);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Success<>(SUCCESS);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Result<String, String> another = new Success<>("ANOTHER");
        final Function<String, Result<String, String>> mapper = s -> another;
        // When
        final String value = lazy.orElse(null);
        final Result<String, String> result = lazy.flatMapSuccess(mapper);
        // Then
        assertThat(value).isSameAs(SUCCESS);
        assertThat(result).isSameAs(another);
    }

    @Test
    void obeys_monad_laws() {
        // Given
        final Result<String, Void> result = unit(SUCCESS);
        final Function<String, Result<Integer, Void>> fun1 = x -> unit(x.length());
        final Function<Integer, Result<String, Void>> fun2 = x -> unit(x * 10 + "a");
        // Then
        // Left Identity Law
        assertThat(resolve(bind(unit(SUCCESS), fun1)))
                .isEqualTo(resolve(fun1.apply(SUCCESS)))
                .isEqualTo(new Success<>(7));
        // Right Identity Law
        assertThat(resolve(result))
                .isEqualTo(resolve(bind(result, LazyResult_flatMapSuccess_Test::unit)))
                .isEqualTo(new Success<>(SUCCESS));
        // Associativity Law
        assertThat(resolve(bind(bind(result, fun1), fun2)))
                .isEqualTo(resolve(bind(result, s -> fun2.apply(unwrap(fun1.apply(s))))))
                .isEqualTo(new Success<>("70a"));
    }

    private static <T> Result<T, Void> unit(T value) {
        return new LazyResult<>(() -> new Success<>(value));
    }

    private static <T, T2> Result<T2, Void> bind(Result<T, Void> result, Function<T, Result<T2, Void>> function) {
        return result.flatMapSuccess(function);
    }

    private static <T> T unwrap(Result<T, Void> result) {
        return result.optional().get();
    }

    private static <T> Result<T, Void> resolve(Result<T, Void> result) {
        return new Success<>(unwrap(result));
    }
}
