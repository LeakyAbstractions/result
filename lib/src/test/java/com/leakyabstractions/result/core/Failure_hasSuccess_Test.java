
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#hasSuccess()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure hasSuccess")
class Failure_hasSuccess_Test {

    @Test
    void should_return_false() {
        // Given
        final Result<Integer, String> result = new Failure<>("FAILURE");
        // When
        final boolean hasSuccess = result.hasSuccess();
        // Then
        assertThat(hasSuccess).isFalse();
    }
}
