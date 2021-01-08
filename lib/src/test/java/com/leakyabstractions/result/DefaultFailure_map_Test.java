
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#map(Function, Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure map")
class DefaultFailure_map_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_use_failure_mapping_only() {
        // Given
        final Result<Integer, Integer> failure = new DefaultFailure<>(123);
        final Function<Integer, String> successMapper = s -> fail("Should not happen");
        final Function<Integer, String> failureMapper = f -> FAILURE;
        final Result<String, String> expected = new DefaultFailure<>(FAILURE);
        // When
        final Result<String, String> result = failure.map(successMapper, failureMapper);
        // Then
        assertThat(result).isEqualTo(expected);
    }
}
