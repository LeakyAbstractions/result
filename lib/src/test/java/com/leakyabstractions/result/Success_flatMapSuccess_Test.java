
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#flatMapSuccess(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success flatMapSuccess")
class Success_flatMapSuccess_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_new_failure_when_function_returns_failure() {
        // Given
        final Result<Integer, String> success = new Success<>(123);
        final Result<String, String> another = new Failure<>("FAILURE");
        final Function<Integer, Result<String, String>> mapper = s -> another;
        // When
        final Result<String, String> result = success.flatMapSuccess(mapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void should_return_new_success_when_function_returns_success() {
        // Given
        final Result<Integer, Integer> success = new Success<>(123);
        final Result<String, Integer> another = new Success<>(SUCCESS);
        final Function<Integer, Result<String, Integer>> mapper = s -> another;
        // When
        final Result<String, Integer> result = success.flatMapSuccess(mapper);
        // Then
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
        assertThat(bind(unit(SUCCESS), fun1))
                .isEqualTo(fun1.apply(SUCCESS))
                .isEqualTo(new Success<>(7));
        // Right Identity Law
        assertThat(result)
                .isEqualTo(bind(result, Success_flatMapSuccess_Test::unit))
                .isEqualTo(new Success<>(SUCCESS));
        // Associativity Law
        assertThat(bind(bind(result, fun1), fun2))
                .isEqualTo(bind(result, s -> fun2.apply(unwrap(fun1.apply(s)))))
                .isEqualTo(new Success<>("70a"));
    }

    private static <T> Result<T, Void> unit(T value) {
        return new Success<>(value);
    }

    private static <T, T2> Result<T2, Void> bind(
            Result<T, Void> result, Function<T, Result<T2, Void>> function) {
        return result.flatMapSuccess(function);
    }

    private static <T> T unwrap(Result<T, Void> result) {
        return result.getSuccess().get();
    }
}
