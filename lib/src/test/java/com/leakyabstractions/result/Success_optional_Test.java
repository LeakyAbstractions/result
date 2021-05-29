
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#optional()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success optional")
class Success_optional_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_non_empty_optional() {
        // Given
        final Result<String, Integer> success = new Success<>(SUCCESS);
        // When
        final Optional<String> optional = success.optional();
        // Then
        assertThat(optional).containsSame(SUCCESS);
    }
}
