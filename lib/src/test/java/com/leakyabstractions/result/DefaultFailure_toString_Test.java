
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#toString()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure toString")
class DefaultFailure_toString_Test {

    @Test
    void should_return_expected_string_when_value_is_not_null() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>("FAILURE");
        // Then
        assertThat(failure).hasToString("failure[FAILURE]");
    }

    @Test
    void should_return_expected_string_when_value_is_null() {
        // Given
        final Result<String, Integer> failure = new DefaultFailure<>(null);
        // Then
        assertThat(failure).hasToString("failure[null]");
    }
}
