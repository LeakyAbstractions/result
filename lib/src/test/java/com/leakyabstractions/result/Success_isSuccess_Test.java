
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#isSuccess()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success isSuccess")
class Success_isSuccess_Test {

    @Test
    void should_return_true() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        // When
        final boolean isSuccess = success.isSuccess();
        // Then
        assertThat(isSuccess).isTrue();
    }
}
