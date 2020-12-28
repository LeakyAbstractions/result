
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#isFailure()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess isFailure")
class DefaultSuccess_isFailure_Test {

    @Test
    void should_return_false() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>("SUCCESS");
        // When
        final boolean isFailure = success.isFailure();
        // Then
        assertThat(isFailure).isFalse();
    }

    @Test
    void should_return_false_even_if_value_is_null() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(null);
        // When
        final boolean isFailure = success.isFailure();
        // Then
        assertThat(isFailure).isFalse();
    }
}
