
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#orElseThrow()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess orElseThrow")
class DefaultSuccess_orElseThrow_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_same_value() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(SUCCESS);
        // When
        final String value = success.orElseThrow();
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_not_throw_exception_even_if_value_is_null() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(null);
        // When
        final String value = success.orElseThrow();
        // Then
        assertThat(value).isNull();
    }
}
