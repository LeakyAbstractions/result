
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#toString()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess toString")
class DefaultSuccess_toString_Test {

    @Test
    void should_return_expected_string_when_value_is_not_null() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>("SUCCESS");
        // Then
        assertThat(success).hasToString("success[SUCCESS]");
    }

    @Test
    void should_return_expected_string_when_value_is_null() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(null);
        // Then
        assertThat(success).hasToString("success[null]");
    }
}
