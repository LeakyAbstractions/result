
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#toString()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure toString")
class Failure_toString_Test {

    @Test
    void should_return_expected_string_when_value_is_not_null() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        // Then
        assertThat(failure).hasToString("Failure[FAILURE]");
    }
}
