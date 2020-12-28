
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#orElseThrow(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess orElseThrow with mapper")
class DefaultSuccess_orElseThrow_with_Mapper_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_not_throw_exception() {
        // Given
        final Result<String, String> success = new DefaultSuccess<>(SUCCESS);
        // When
        final String value = success.orElseThrow(RuntimeException::new);
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_not_throw_exception_even_if_value_is_null() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(null);
        final Function<Integer, NullPointerException> failureMapper = f -> new NullPointerException();
        // When
        final String value = success.orElseThrow(failureMapper);
        // Then
        assertThat(value).isNull();
    }
}
