
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#isSuccess()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Failure isSuccess")
class Failure_isSuccess_Test {

    @Test
    void should_return_false() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        // When
        final boolean isSuccess = failure.isSuccess();
        // Then
        assertThat(isSuccess).isFalse();
    }
}
