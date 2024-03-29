
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#hashCode()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success hashCode")
class Success_hashCode_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_same_hash_code_as_the_value() {
        // Given
        final Result<String, Integer> success = new Success<>(SUCCESS);
        // Then
        assertThat(success).hasSameHashCodeAs(SUCCESS);
    }
}
