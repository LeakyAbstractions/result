
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#getFailureOrElseThrow()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure getFailureOrElseThrow")
class Failure_getFailureOrElseThrow_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_same_value() {
        // Given
        final Result<Integer, String> failure = new Failure<>(FAILURE);
        // When
        final String value = failure.getFailureOrElseThrow();
        // Then
        assertThat(value).isSameAs(FAILURE);
    }
}
