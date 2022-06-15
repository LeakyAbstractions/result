
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        final String failure = result.getFailure();
        // Then
        assertThat(failure).isSameAs(FAILURE);
    }
}
