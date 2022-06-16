
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        final Integer failure = result.getFailure();
        // Then
        assertThat(failure).isNull();
    }
}
