
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#mapSuccess(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Success mapSuccess")
class Success_mapSuccess_Test {

    @Test
    void should_use_success_mapping() {
        // Given
        final Result<Integer, String> success = new Success<>(123);
        final UnaryOperator<Integer> mapper = s -> s + 198;
        // When
        final Result<Integer, String> result = success.mapSuccess(mapper);
        // Then
        assertThat(result).isEqualTo(new Success<>(321));
    }

    @Test
    void should_use_success_mapping_even_if_value_is_null() {
        // Given
        final Result<Object, Integer> success = new Success<>(null);
        final Function<Object, String> mapper = s -> "SUCCESS";
        // When
        final Result<String, Integer> result = success.mapSuccess(mapper);
        // Then
        assertThat(result).isEqualTo(new Success<>("SUCCESS"));
    }
}
