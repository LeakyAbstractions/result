
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#orElse(Object)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Success orElse")
class Success_orElse_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_ignore_other() {
        // Given
        final Result<String, Integer> success = new Success<>(SUCCESS);
        // When
        final String value = success.orElse("ANOTHER");
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_ignore_other_even_if_value_is_null() {
        // Given
        final Result<String, Integer> success = new Success<>(null);
        // When
        final String value = success.orElse("ANOTHER");
        // Then
        assertThat(value).isNull();
    }
}
