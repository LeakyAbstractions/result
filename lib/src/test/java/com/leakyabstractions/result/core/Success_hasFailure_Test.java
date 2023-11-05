
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#hasFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success hasFailure")
class Success_hasFailure_Test {

    @Test
    void should_return_false() {
        // Given
        final Result<String, Integer> result = new Success<>("SUCCESS");
        // When
        final boolean hasFailure = result.hasFailure();
        // Then
        assertThat(hasFailure).isFalse();
    }
}
