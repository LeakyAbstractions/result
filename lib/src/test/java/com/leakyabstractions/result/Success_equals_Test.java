
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#equals(Object)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success equals")
class Success_equals_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_be_equal_to_itself() {
        // Given
        final Result<String, Integer> success = new Success<>(SUCCESS);
        // When
        final boolean equals = success.equals(success);
        // Then
        assertThat(equals).isTrue();
    }

    @Test
    void should_be_equal_to_a_successful_result_with_the_same_value() {
        // Given
        final Result<String, Integer> success = new Success<>(SUCCESS);
        final Result<String, Integer> another = new Success<>(SUCCESS);
        // Then
        assertThat(success).isEqualTo(another);
    }

    @Test
    void should_be_equal_to_a_successful_result_with_an_equal_value() {
        // Given
        final Result<String, Integer> success = new Success<>(SUCCESS);
        final Result<String, Integer> another = new Success<>(new String("SUCCESS"));
        // Then
        assertThat(success).isEqualTo(another);
    }

    @Test
    void should_not_be_equal_to_the_same_value() {
        // Given
        final Result<String, Integer> success = new Success<>(SUCCESS);
        // Then
        assertThat(success).isNotEqualTo(SUCCESS);
    }

    @Test
    void should_not_be_equal_to_a_successful_result_with_a_different_value() {
        // Given
        final Result<String, Integer> success = new Success<>(SUCCESS);
        final Result<String, Integer> another = new Success<>("FAILURE");
        // Then
        assertThat(success).isNotEqualTo(another);
    }

    @Test
    void should_not_be_equal_to_a_failed_result_with_the_same_value() {
        // Given
        final Result<String, String> success = new Success<>(SUCCESS);
        final Result<String, String> another = new Failure<>(SUCCESS);
        // Then
        assertThat(success).isNotEqualTo(another);
    }
}
