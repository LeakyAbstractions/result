
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#toString()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success toString")
class Success_toString_Test {

    @Test
    void should_return_expected_string_when_value_is_not_null() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        // Then
        assertThat(success).hasToString("Success[SUCCESS]");
    }
}
