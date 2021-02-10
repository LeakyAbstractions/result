
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#mapSuccess(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Failure mapSuccess")
class Failure_mapSuccess_Test {

    @Test
    void should_ignore_success_mapping() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        final Function<Integer, String> mapper = s -> fail("Should not happen");
        // When
        final Result<String, String> result = failure.mapSuccess(mapper);
        // Then
        assertThat(result).isSameAs(failure);
    }
}
