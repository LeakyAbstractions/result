
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#flatMapFailure(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Failure flatMapFailure")
class Failure_flatMapFailure_Test {

    @Test
    void should_return_new_failure_when_function_returns_failure() {
        // Given
        final Result<Integer, Integer> failure = new Failure<>(123);
        final Result<Integer, String> another = new Failure<>("FAILURE");
        final Function<Integer, Result<Integer, String>> failureFlatMapper = f -> another;
        // When
        final Result<Integer, String> result = failure.flatMapFailure(failureFlatMapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void should_return_new_success_when_function_returns_success() {
        // Given
        final Result<String, Integer> failure = new Failure<>(123);
        final Result<String, Integer> another = new Success<>("SUCCESS");
        final Function<Integer, Result<String, Integer>> failureFlatMapper = f -> another;
        // When
        final Result<String, Integer> result = failure.flatMapFailure(failureFlatMapper);
        // Then
        assertThat(result).isSameAs(another);
    }
}
