
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#orElse(Object)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure orElse")
class DefaultFailure_orElse_Test {

    @Test
    void should_return_other() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>("FAILURE");
        // When
        final Integer value = failure.orElse(123);
        // Then
        assertThat(value).isEqualTo(123);
    }
}
