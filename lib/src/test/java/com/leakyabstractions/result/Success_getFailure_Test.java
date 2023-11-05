
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#getFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success getFailure")
class Success_getFailure_Test {

    @Test
    void should_return_null() {
        // Given
        final Result<String, Integer> result = new Success<>("SUCCESS");
        // When
        final Optional<Integer> failure = result.getFailure();
        // Then
        assertThat(failure).isEmpty();
    }
}
