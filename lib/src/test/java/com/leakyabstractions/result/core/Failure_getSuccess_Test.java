
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#getSuccess()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure getSuccess")
class Failure_getSuccess_Test {

    @Test
    void should_return_null() {
        // Given
        final Result<Integer, String> result = new Failure<>("FAILURE");
        // When
        final Optional<Integer> success = result.getSuccess();
        // Then
        assertThat(success).isEmpty();
    }
}
