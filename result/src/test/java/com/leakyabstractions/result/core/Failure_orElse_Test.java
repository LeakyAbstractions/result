
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#orElse(Object)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure orElse")
class Failure_orElse_Test {

    @Test
    void should_return_other() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        // When
        final Integer value = failure.orElse(123);
        // Then
        assertThat(value).isEqualTo(123);
    }
}
