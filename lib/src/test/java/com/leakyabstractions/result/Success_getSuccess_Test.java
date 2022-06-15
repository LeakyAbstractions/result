
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        final String success = result.getSuccess();
        // Then
        assertThat(success).isSameAs(SUCCESS);
    }
}
