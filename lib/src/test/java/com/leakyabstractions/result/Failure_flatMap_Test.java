
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#flatMap(Function, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure flatMap")
class Failure_flatMap_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_new_success_when_failure_function_returns_success() {
        // Given
        final Result<Integer, Integer> failure = new Failure<>(123);
        final Function<Integer, Result<String, String>> successMapper = s -> fail("Should not happen");
        final Result<String, String> another = new Success<>("SUCCESS");
        final Function<Integer, Result<String, String>> failureMapper = f -> another;
        // When
        final Result<String, String> result = failure.flatMap(successMapper, failureMapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void should_return_new_failure_when_failure_function_returns_failure() {
        // Given
        final Result<Integer, Integer> failure = new Failure<>(123);
        final Function<Integer, Result<String, String>> successMapper = s -> fail("Should not happen");
        final Result<String, String> another = new Failure<>(FAILURE);
        final Function<Integer, Result<String, String>> failureMapper = f -> another;
        // When
        final Result<String, String> result = failure.flatMap(successMapper, failureMapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void obeys_monad_laws() {
        // Given
        final Result<String, String> result = unit(FAILURE);
        final Function<String, Result<Integer, Integer>> fun1 = x -> new Failure<>(x.length());
        final Function<Integer, Result<String, String>> fun2 = x -> new Success<>(x * 10 + "a");
        // Then
        // Left Identity Law
        assertThat(bind(unit(FAILURE), fun1))
                .isEqualTo(fun1.apply(FAILURE))
                .isEqualTo(new Failure<>(7));
        // Right Identity Law
        assertThat(result)
                .isEqualTo(bind(result, Failure_flatMap_Test::unit))
                .isEqualTo(new Failure<>(FAILURE));
        // Associativity Law
        assertThat(bind(bind(result, fun1), fun2))
                .isEqualTo(bind(result, s -> fun2.apply(unwrap(fun1.apply(s)))))
                .isEqualTo(new Success<>("70a"));
    }

    private static <T> Result<T, T> unit(T value) {
        return new Failure<>(value);
    }

    private static <T, T2> Result<T2, T2> bind(Result<T, T> result, Function<T, Result<T2, T2>> function) {
        return result.flatMap(function, function);
    }

    private static <T> T unwrap(Result<T, T> result) {
        return result.getSuccess().orElseGet(result.getFailure()::get);
    }
}
