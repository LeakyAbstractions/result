
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#orElseMap(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure orElseMap")
class DefaultFailure_orElseMap_Test {

    @Test
    void should_use_failure_mapper() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>("FAILURE");
        // When
        final Integer value = failure.orElseMap(e -> 321);
        // Then
        assertThat(value).isEqualTo(321);
    }
}
