
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#optionalFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success optionalFailure")
class Success_optionalFailure_Test {

    @Test
    void should_return_empty_optional() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        // When
        final Optional<Integer> optional = success.optionalFailure();
        // Then
        assertThat(optional).isEmpty();
    }
}
