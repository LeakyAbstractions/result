
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#map(Function, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure map")
class Failure_map_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_use_failure_mapping_only() {
        // Given
        final Result<Integer, Integer> failure = new Failure<>(123);
        final Function<Integer, String> successMapper = s -> fail("Should not happen");
        final Function<Integer, String> failureMapper = f -> FAILURE;
        final Result<String, String> expected = new Failure<>(FAILURE);
        // When
        final Result<String, String> result = failure.map(successMapper, failureMapper);
        // Then
        assertThat(result).isEqualTo(expected);
    }
}
