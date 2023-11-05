
package com.leakyabstractions.result;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.ofNullable;
import static com.leakyabstractions.result.Results.success;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Results#ofNullable(Object, Object)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Results ofNullable")
class Results_ofNullable_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_success_when_not_null() {
        // Given
        final String nullable = SUCCESS;
        // When
        final Result<String, Integer> result = ofNullable(nullable, 123);
        // Then
        assertThat(result).isEqualTo(success(SUCCESS));
    }

    @Test
    void should_return_failure_when_null() {
        // Given
        final Integer nullable = null;
        // When
        final Result<Integer, String> result = ofNullable(nullable, FAILURE);
        // Then
        assertThat(result).isEqualTo(failure(FAILURE));
    }
}
