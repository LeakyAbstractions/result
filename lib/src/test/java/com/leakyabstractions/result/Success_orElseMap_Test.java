
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#orElseMap(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Success orElseMap")
class Success_orElseMap_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_ignore_failure_mapper() {
        // Given
        final Result<String, Integer> success = new Success<>(SUCCESS);
        final Function<Integer, String> mapper = f -> fail("Should not happen");
        // When
        final String value = success.orElseMap(mapper);
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_ignore_failure_mapper_even_if_value_is_null() {
        // Given
        final Result<String, Integer> success = new Success<>(null);
        final Function<Integer, String> mapper = f -> fail("Should not happen");
        // When
        final String value = success.orElseMap(mapper);
        // Then
        assertThat(value).isNull();
    }
}
