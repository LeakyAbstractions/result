
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultResult#toOptionalFailure(Result))}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultResult toOptionalFailure")
class DefaultResult_toOptionalFailure_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_empty_optional_when_result_is_success() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>("SUCCESS");
        // When
        final Optional<Integer> toOptional = DefaultResult.toOptionalFailure(success);
        // Then
        assertThat(toOptional).isEmpty();
    }

    @Test
    void should_return_not_empty_optional_when_value_is_not_null() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>(FAILURE);
        // When
        final Optional<String> toOptional = DefaultResult.toOptionalFailure(failure);
        // Then
        assertThat(toOptional).contains(FAILURE);
    }

    @Test
    void should_return_empty_optional_when_value_is_null() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>(null);
        // When
        final Optional<String> toOptional = DefaultResult.toOptionalFailure(failure);
        // Then
        assertThat(toOptional).isEmpty();
    }
}
