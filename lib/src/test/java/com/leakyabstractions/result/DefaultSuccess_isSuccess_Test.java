
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#isSuccess()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess isSuccess")
class DefaultSuccess_isSuccess_Test {

    @Test
    void should_return_true() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>("SUCCESS");
        // When
        final boolean isSuccess = success.isSuccess();
        // Then
        assertThat(isSuccess).isTrue();
    }

    @Test
    void should_return_true_even_if_value_is_null() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(null);
        // When
        final boolean isSuccess = success.isSuccess();
        // Then
        assertThat(isSuccess).isTrue();
    }
}
