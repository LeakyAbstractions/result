
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#isFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success isFailure")
class Success_isFailure_Test {

    @Test
    void should_return_false() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        // When
        final boolean isFailure = success.isFailure();
        // Then
        assertThat(isFailure).isFalse();
    }
}
