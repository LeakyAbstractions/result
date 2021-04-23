
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#isFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure isFailure")
class Failure_isFailure_Test {

    @Test
    void should_return_true() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        // When
        final boolean isFailure = failure.isFailure();
        // Then
        assertThat(isFailure).isTrue();
    }
}
