
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#getSuccess()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success getSuccess")
class Success_getSuccess_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_success_value() {
        // Given
        final Result<String, Integer> result = new Success<>(SUCCESS);
        // When
        final Optional<String> success = result.getSuccess();
        // Then
        assertThat(success).containsSame(SUCCESS);
    }
}
