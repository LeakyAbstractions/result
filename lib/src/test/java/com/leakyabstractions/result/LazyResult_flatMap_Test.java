
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#flatMap(Function, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult flatMap")
class LazyResult_flatMap_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> fail("Should not happen");
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Result<String, String>> successMapper = s -> fail("Should not happen");
        final Function<String, Result<String, String>> failureMapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.flatMap(successMapper, failureMapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class);
    }

    @Test
    void should_eventually_flat_map_when_needed() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Failure<>(FAILURE);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Result<String, String>> successMapper = s -> fail("Should not happen");
        final Function<String, Result<String, String>> failureMapper = f -> new Success<>(SUCCESS);
        // When
        final String value = lazy.flatMap(successMapper, failureMapper).orElse(null);
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Success<>(SUCCESS);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Result<String, String> another = new Success<>("ANOTHER");
        final Function<String, Result<String, String>> successMapper = s -> another;
        final Function<String, Result<String, String>> failureMapper = f -> fail("Should not happen");
        // When
        final String value = lazy.orElse(null);
        final Result<String, String> result = lazy.flatMap(successMapper, failureMapper);
        // Then
        assertThat(value).isSameAs(SUCCESS);
        assertThat(result).isSameAs(another);
    }

    @Test
    void obeys_monad_laws() {
        // Given
        final Result<String, String> result = unit(SUCCESS);
        final Function<String, Result<Integer, Integer>> fun1 = x -> new Success<>(x.length());
        final Function<Integer, Result<String, String>> fun2 = x -> new Failure<>(x * 10 + "a");
        // Then
        // Left Identity Law
        assertThat(resolve(bind(unit(SUCCESS), fun1)))
                .isEqualTo(resolve(fun1.apply(SUCCESS)))
                .isEqualTo(new Success<>(7));
        // Right Identity Law
        assertThat(resolve(result))
                .isEqualTo(resolve(bind(result, LazyResult_flatMap_Test::unit)))
                .isEqualTo(new Success<>(SUCCESS));
        // Associativity Law
        assertThat(resolve(bind(bind(result, fun1), fun2)))
                .isEqualTo(resolve(bind(result, s -> fun2.apply(unwrap(fun1.apply(s))))))
                .isEqualTo(new Failure<>("70a"));
    }

    private static <T> Result<T, T> unit(T value) {
        return new LazyResult<>(() -> new Success<>(value));
    }

    private static <T, T2> Result<T2, T2> bind(Result<T, T> result, Function<T, Result<T2, T2>> function) {
        return result.flatMap(function, function);
    }

    private static <T> T unwrap(Result<T, T> result) {
        return result.optional().orElseGet(() -> result.optionalFailure().get());
    }

    private static <T> Result<T, T> resolve(Result<T, T> result) {
        return Results.ofOptional(result.optional(), () -> result.optionalFailure().get());
    }
}
