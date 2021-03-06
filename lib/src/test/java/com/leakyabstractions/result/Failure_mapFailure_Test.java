
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#mapFailure(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Failure mapFailure")
class Failure_mapFailure_Test {

    @Test
    void should_use_failure_mapping() {
        // Given
        final Result<Integer, Integer> failure = new Failure<>(123);
        final UnaryOperator<Integer> failureMapper = f -> f + 198;
        // When
        final Result<Integer, Integer> result = failure.mapFailure(failureMapper);
        // Then
        assertThat(result).isEqualTo(new Failure<>(321));
    }

    @Test
    void should_use_failure_mapping_even_if_value_is_null() {
        // Given
        final Result<Integer, Integer> failure = new Failure<>(123);
        final Function<Integer, String> failureMapper = f -> "FAILURE";
        // When
        final Result<Integer, String> result = failure.mapFailure(failureMapper);
        // Then
        assertThat(result).isEqualTo(new Failure<>("FAILURE"));
    }
}
