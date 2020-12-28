
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultResult#toOptional(Result))}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultResult toOptional")
class DefaultResult_toOptional_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_empty_optional_when_result_is_failure() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>("FAILURE");
        // When
        final Optional<Integer> toOptional = DefaultResult.toOptional(failure);
        // Then
        assertThat(toOptional).isEmpty();
    }

    @Test
    void should_return_not_empty_optional_when_value_is_not_null() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(SUCCESS);
        // When
        final Optional<String> toOptional = DefaultResult.toOptional(success);
        // Then
        assertThat(toOptional).contains(SUCCESS);
    }

    @Test
    void should_return_empty_optional_when_value_is_null() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(null);
        // When
        final Optional<String> toOptional = DefaultResult.toOptional(success);
        // Then
        assertThat(toOptional).isEmpty();
    }
}
