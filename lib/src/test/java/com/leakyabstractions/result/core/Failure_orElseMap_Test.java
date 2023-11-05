
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#orElseMap(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure orElseMap")
class Failure_orElseMap_Test {

    @Test
    void should_use_failure_mapper() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        // When
        final Integer value = failure.orElseMap(f -> 321);
        // Then
        assertThat(value).isEqualTo(321);
    }
}
