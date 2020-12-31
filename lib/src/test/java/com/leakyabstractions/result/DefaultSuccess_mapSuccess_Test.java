
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#mapSuccess(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess mapSuccess")
class DefaultSuccess_mapSuccess_Test {

    @Test
    void should_use_success_mapping() {
        // Given
        final Result<Integer, String> success = new DefaultSuccess<>(123);
        final UnaryOperator<Integer> successMapper = s -> s + 198;
        // When
        final Result<Integer, String> result = success.mapSuccess(successMapper);
        // Then
        assertThat(result).isEqualTo(new DefaultSuccess<>(321));
    }

    @Test
    void should_use_success_mapping_even_if_value_is_null() {
        // Given
        final Result<Object, Integer> success = new DefaultSuccess<>(null);
        final Function<Object, String> successMapper = s -> "SUCCESS";
        // When
        final Result<String, Integer> result = success.mapSuccess(successMapper);
        // Then
        assertThat(result).isEqualTo(new DefaultSuccess<>("SUCCESS"));
    }
}
