
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#map(Function, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success map")
class Success_map_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_use_success_mapping_only() {
        // Given
        final Result<Integer, Integer> success = new Success<>(123);
        final Function<Integer, String> successMapper = s -> SUCCESS;
        final Function<Integer, String> failureMapper = f -> fail("Should not happen");
        final Result<String, String> expected = new Success<>(SUCCESS);
        // When
        final Result<String, String> result = success.map(successMapper, failureMapper);
        // Then
        assertThat(result).isEqualTo(expected);
    }
}
