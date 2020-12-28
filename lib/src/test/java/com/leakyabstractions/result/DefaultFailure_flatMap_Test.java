
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#flatMap(Function, Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure flatMap")
class DefaultFailure_flatMap_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_new_success_when_failure_function_returns_success() {
        // Given
        final Result<Integer, Integer> failure = new DefaultFailure<>(123);
        final Function<Integer, Result<String, String>> successFlatMapper = s -> new DefaultSuccess<>(SUCCESS);
        final Function<Integer, Result<String, String>> failureFlatMapper = f -> new DefaultSuccess<>(SUCCESS);
        // When
        final Result<String, String> result = failure.flatMap(successFlatMapper, failureFlatMapper);
        // Then
        assertThat(result).isEqualTo(new DefaultSuccess<>(SUCCESS));
    }

    @Test
    void should_return_new_failure_when_failure_function_returns_failure() {
        // Given
        final Result<Integer, Integer> failure = new DefaultFailure<>(123);
        final Function<Integer, Result<String, String>> successFlatMapper = s -> new DefaultSuccess<>(SUCCESS);
        final Function<Integer, Result<String, String>> failureFlatMapper = f -> new DefaultFailure<>(FAILURE);
        // When
        final Result<String, String> result = failure.flatMap(successFlatMapper, failureFlatMapper);
        // Then
        assertThat(result).isEqualTo(new DefaultFailure<>(FAILURE));
    }
}
