
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#flatMapFailure(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult flatMapFailure")
class LazyResult_flatMapFailure_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> fail("Should not happen");
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Result<String, String>> mapper = s -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.flatMapFailure(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class);
    }

    @Test
    void should_eventually_flat_map_when_needed() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Failure<>(FAILURE);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Result<String, String>> mapper = s -> new Success<>(SUCCESS);
        // When
        final String value = lazy.flatMapFailure(mapper).orElse(null);
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Failure<>(FAILURE);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Result<String, String> another = new Failure<>("ANOTHER");
        final Function<String, Result<String, String>> mapper = s -> another;
        // When
        final Optional<String> value = lazy.optionalFailure();
        final Result<String, String> result = lazy.flatMapFailure(mapper);
        // Then
        assertThat(value).containsSame(FAILURE);
        assertThat(result).isSameAs(another);
    }

    @Test
    void obeys_monad_laws() {
        // Given
        final Result<Void, String> result = unit(FAILURE);
        final Function<String, Result<Void, Integer>> fun1 = x -> unit(x.length());
        final Function<Integer, Result<Void, String>> fun2 = x -> unit(x * 10 + "a");
        // Then
        // Left Identity Law
        assertThat(resolve(bind(unit(FAILURE), fun1)))
                .isEqualTo(resolve(fun1.apply(FAILURE)))
                .isEqualTo(new Failure<>(7));
        // Right Identity Law
        assertThat(resolve(result))
                .isEqualTo(resolve(bind(result, LazyResult_flatMapFailure_Test::unit)))
                .isEqualTo(new Failure<>(FAILURE));
        // Associativity Law
        assertThat(resolve(bind(bind(result, fun1), fun2)))
                .isEqualTo(resolve(bind(result, s -> fun2.apply(unwrap(fun1.apply(s))))))
                .isEqualTo(new Failure<>("70a"));
    }

    private static <T> Result<Void, T> unit(T value) {
        return new LazyResult<>(() -> new Failure<>(value));
    }

    private static <T, T2> Result<Void, T2> bind(Result<Void, T> result, Function<T, Result<Void, T2>> function) {
        return result.flatMapFailure(function);
    }

    private static <T> T unwrap(Result<Void, T> result) {
        return result.optionalFailure().get();
    }

    private static <T> Result<Void, T> resolve(Result<Void, T> result) {
        return new Failure<>(unwrap(result));
    }
}
