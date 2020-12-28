
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#flatMap(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess flatMapSuccess")
class DefaultSuccess_flatMapSuccess_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_new_success_when_function_returns_success() {
        // Given
        final Result<Integer, String> success = new DefaultSuccess<>(123);
        final Function<Integer, Result<String, String>> successFlatMapper = s -> new DefaultFailure<>(FAILURE);
        // When
        final Result<String, String> result = success.flatMap(successFlatMapper);
        // Then
        assertThat(result).isEqualTo(new DefaultFailure<>(FAILURE));
    }

    @Test
    void should_return_new_failure_when_function_returns_failure() {
        // Given
        final Result<Integer, Integer> success = new DefaultSuccess<>(123);
        final Function<Integer, Result<String, Integer>> successFlatMapper = s -> new DefaultSuccess<>(SUCCESS);
        // When
        final Result<String, Integer> result = success.flatMap(successFlatMapper);
        // Then
        assertThat(result).isEqualTo(new DefaultSuccess<>(SUCCESS));
    }
}