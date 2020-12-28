
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#orElseMap(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess orElseMap")
class DefaultSuccess_orElseMap_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_ignore_failure_mapper() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(SUCCESS);
        final Function<Integer, String> failureMapper = e -> "ANOTHER";
        // When
        final String value = success.orElseMap(failureMapper);
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_ignore_failure_mapper_even_if_value_is_null() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(null);
        // When
        final String value = success.orElseMap(e -> "ANOTHER");
        // Then
        assertThat(value).isNull();
    }
}
