
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#flatMapFailure(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure flatMapFailure")
class DefaultFailure_flatMapFailure_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_new_failure_when_function_returns_failure() {
        // Given
        final Result<Integer, Integer> failure = new DefaultFailure<>(123);
        final Function<Integer, Result<Integer, String>> failureFlatMapper = f -> new DefaultFailure<>(FAILURE);
        // When
        final Result<Integer, String> result = failure.flatMapFailure(failureFlatMapper);
        // Then
        assertThat(result).isEqualTo(new DefaultFailure<>(FAILURE));
    }

    @Test
    void should_return_new_success_when_function_returns_success() {
        // Given
        final Result<String, Integer> failure = new DefaultFailure<>(123);
        final Function<Integer, Result<String, Integer>> failureFlatMapper = f -> new DefaultSuccess<>(SUCCESS);
        // When
        final Result<String, Integer> result = failure.flatMapFailure(failureFlatMapper);
        // Then
        assertThat(result).isEqualTo(new DefaultSuccess<>(SUCCESS));
    }
}
