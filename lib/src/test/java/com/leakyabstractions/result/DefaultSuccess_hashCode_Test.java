
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#hashCode()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess hashCode")
class DefaultSuccess_hashCode_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_same_hash_code_as_the_value() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(SUCCESS);
        // Then
        assertThat(success).hasSameHashCodeAs(SUCCESS);
    }

    @Test
    void should_return_zero_when_value_is_null() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>(null);
        // Then
        assertThat(success).hasSameHashCodeAs(0);
    }
}
