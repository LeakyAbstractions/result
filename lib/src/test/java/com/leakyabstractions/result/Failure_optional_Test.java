
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#optional()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure optional")
class Failure_optional_Test {

    @Test
    void should_return_empty_optional() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        // When
        final Optional<Integer> optional = failure.optional();
        // Then
        assertThat(optional).isEmpty();
    }
}
