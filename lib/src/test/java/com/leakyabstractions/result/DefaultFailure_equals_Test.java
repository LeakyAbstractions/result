
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#equals(Object)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure equals")
class DefaultFailure_equals_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_equal_to_itself() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>(FAILURE);
        // When
        final boolean equals = failure.equals(failure);
        // Then
        assertThat(equals).isTrue();
    }

    @Test
    void should_be_equal_to_a_failed_result_with_the_same_value() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>(FAILURE);
        // Then
        assertThat(failure).isEqualTo(new DefaultFailure<>(FAILURE));
    }

    @Test
    void should_be_equal_to_a_successful_result_with_an_equal_value() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>(FAILURE);
        // Then
        assertThat(failure).isEqualTo(new DefaultFailure<>(new String("FAILURE")));
    }

    @Test
    void should_not_be_equal_to_the_same_value() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>(FAILURE);
        // Then
        assertThat(failure).isNotEqualTo(FAILURE);
    }

    @Test
    void should_not_be_equal_to_a_failed_result_with_a_different_value() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>(FAILURE);
        // Then
        assertThat(failure).isNotEqualTo(new DefaultFailure<>("ANOTHER"));
    }

    @Test
    void should_not_be_equal_to_a_successful_result_with_the_same_value() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>(FAILURE);
        // Then
        assertThat(failure).isNotEqualTo(new DefaultSuccess<>(FAILURE));
    }
}