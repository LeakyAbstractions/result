
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#hasFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure hasFailure")
class Failure_hasFailure_Test {

    @Test
    void should_return_true() {
        // Given
        final Result<Integer, String> result = new Failure<>("FAILURE");
        // When
        final boolean hasFailure = result.hasFailure();
        // Then
        assertThat(hasFailure).isTrue();
    }
}
