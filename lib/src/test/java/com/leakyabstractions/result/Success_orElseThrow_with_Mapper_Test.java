
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#orElseThrow(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Success orElseThrow with mapper")
class Success_orElseThrow_with_Mapper_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_not_throw_exception() {
        // Given
        final Result<String, String> success = new Success<>(SUCCESS);
        final Function<String, RuntimeException> failureMapper = f -> fail("Should not happen");
        // When
        final String value = success.orElseThrow(failureMapper);
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_not_throw_exception_even_if_value_is_null() {
        // Given
        final Result<String, Integer> success = new Success<>(null);
        final Function<Integer, NullPointerException> failureMapper = f -> fail("Should not happen");
        // When
        final String value = success.orElseThrow(failureMapper);
        // Then
        assertThat(value).isNull();
    }
}
