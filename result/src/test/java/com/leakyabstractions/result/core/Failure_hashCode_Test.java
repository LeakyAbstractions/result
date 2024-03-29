
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#hashCode()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure hashCode")
class Failure_hashCode_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_same_hash_code_as_the_value() {
        // Given
        final Result<Integer, String> failure = new Failure<>(FAILURE);
        // Then
        assertThat(failure).hasSameHashCodeAs(FAILURE);
    }
}
