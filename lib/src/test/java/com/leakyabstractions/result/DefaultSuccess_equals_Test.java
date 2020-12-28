
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#equals(Object)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess equals")
class DefaultSuccess_equals_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_be_equal_to_itself() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(SUCCESS);
        // When
        final boolean equals = success.equals(success);
        // Then
        assertThat(equals).isTrue();
    }

    @Test
    void should_be_equal_to_a_successful_result_with_the_same_value() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(SUCCESS);
        // Then
        assertThat(success).isEqualTo(new DefaultSuccess<>(SUCCESS));
    }

    @Test
    void should_be_equal_to_a_successful_result_with_an_equal_value() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(SUCCESS);
        // Then
        assertThat(success).isEqualTo(new DefaultSuccess<>(new String("SUCCESS")));
    }

    @Test
    void should_not_be_equal_to_the_same_value() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(SUCCESS);
        // Then
        assertThat(success).isNotEqualTo(SUCCESS);
    }

    @Test
    void should_not_be_equal_to_a_successful_result_with_a_different_value() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(SUCCESS);
        // Then
        assertThat(success).isNotEqualTo(new DefaultSuccess<>("FAILURE"));
    }

    @Test
    void should_not_be_equal_to_a_failed_result_with_the_same_value() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(SUCCESS);
        // Then
        assertThat(success).isNotEqualTo(new DefaultFailure<>(SUCCESS));
    }
}
