
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#hasSuccess()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success hasSuccess")
class Success_hasSuccess_Test {

    @Test
    void should_return_true() {
        // Given
        final Result<String, Integer> result = new Success<>("SUCCESS");
        // When
        final boolean hasSuccess = result.hasSuccess();
        // Then
        assertThat(hasSuccess).isTrue();
    }
}
