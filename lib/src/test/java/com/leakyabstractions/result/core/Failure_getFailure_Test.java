
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#getFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure getFailure")
class Failure_getFailure_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_failure_value() {
        // Given
        final Result<Integer, String> result = new Failure<>(FAILURE);
        // When
        final Optional<String> failure = result.getFailure();
        // Then
        assertThat(failure).containsSame(FAILURE);
    }
}
