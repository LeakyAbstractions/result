
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#isFailure()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure isFailure")
class DefaultFailure_isFailure_Test {

    @Test
    void should_return_true() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>("FAILURE");
        // When
        final boolean isFailure = failure.isFailure();
        // Then
        assertThat(isFailure).isTrue();
    }

    @Test
    void should_return_true_even_if_value_is_null() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>(null);
        // When
        final boolean isFailure = failure.isFailure();
        // Then
        assertThat(isFailure).isTrue();
    }
}
