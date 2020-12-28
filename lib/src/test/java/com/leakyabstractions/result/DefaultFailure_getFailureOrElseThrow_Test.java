
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#getFailureOrElseThrow()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure getFailureOrElseThrow")
class DefaultFailure_getFailureOrElseThrow_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_same_value() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>(FAILURE);
        // When
        final String value = failure.getFailureOrElseThrow();
        // Then
        assertThat(value).isSameAs(FAILURE);
    }

    @Test
    void should_not_throw_exception_even_if_value_is_null() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>(null);
        // When
        final String value = failure.getFailureOrElseThrow();
        // Then
        assertThat(value).isNull();
    }
}
