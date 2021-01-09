
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Results#toOptionalFailure(Result))}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Results toOptionalFailure")
class Results_toOptionalFailure_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_empty_optional_when_result_is_success() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        // When
        final Optional<Integer> toOptional = Results.toOptionalFailure(success);
        // Then
        assertThat(toOptional).isEmpty();
    }

    @Test
    void should_return_not_empty_optional_when_value_is_not_null() {
        // Given
        final Result<Integer, String> failure = new Failure<>(FAILURE);
        // When
        final Optional<String> toOptional = Results.toOptionalFailure(failure);
        // Then
        assertThat(toOptional).contains(FAILURE);
    }

    @Test
    void should_return_empty_optional_when_value_is_null() {
        // Given
        final Result<Integer, String> failure = new Failure<>(null);
        // When
        final Optional<String> toOptional = Results.toOptionalFailure(failure);
        // Then
        assertThat(toOptional).isEmpty();
    }
}
