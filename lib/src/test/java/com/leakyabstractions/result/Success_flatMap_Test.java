
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#flatMap(Function, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success flatMap")
class Success_flatMap_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_new_success_when_success_function_returns_success() {
        // Given
        final Result<Integer, Integer> success = new Success<>(123);
        final Result<String, String> another = new Success<>(SUCCESS);
        final Function<Integer, Result<String, String>> successMapper = s -> another;
        final Function<Integer, Result<String, String>> failureMapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = success.flatMap(successMapper, failureMapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void should_return_new_failure_when_success_function_returns_failure() {
        // Given
        final Result<Integer, Integer> success = new Success<>(123);
        final Result<String, String> another = new Failure<>("FAILURE");
        final Function<Integer, Result<String, String>> successMapper = s -> another;
        final Function<Integer, Result<String, String>> failureMapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = success.flatMap(successMapper, failureMapper);
        // Then
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
        assertThat(bind(unit(SUCCESS), fun1))
                .isEqualTo(fun1.apply(SUCCESS))
                .isEqualTo(new Success<>(7));
        // Right Identity Law
        assertThat(result)
                .isEqualTo(bind(result, Success_flatMap_Test::unit))
                .isEqualTo(new Success<>(SUCCESS));
        // Associativity Law
        assertThat(bind(bind(result, fun1), fun2))
                .isEqualTo(bind(result, s -> fun2.apply(unwrap(fun1.apply(s)))))
                .isEqualTo(new Failure<>("70a"));
    }

    private static <T> Result<T, T> unit(T value) {
        return new Success<>(value);
    }

    private static <T, T2> Result<T2, T2> bind(Result<T, T> result, Function<T, Result<T2, T2>> function) {
        return result.flatMap(function, function);
    }

    private static <T> T unwrap(Result<T, T> result) {
        return result.optional().orElseGet(() -> result.optionalFailure().get());
    }
}
