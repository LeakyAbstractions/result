
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#mapSuccess(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure mapSuccess")
class DefaultFailure_mapSuccess_Test {

    @Test
    void should_ignore_success_mapping() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>("FAILURE");
        final Function<Integer, String> successMapper = s -> "SUCCESS";
        // When
        final Result<String, String> result = failure.mapSuccess(successMapper);
        // Then
        assertThat(result).isEqualTo(failure);
    }
}
