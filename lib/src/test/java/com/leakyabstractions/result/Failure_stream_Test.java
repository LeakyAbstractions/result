
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#stream()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure stream")
class Failure_stream_Test {

    @Test
    void should_return_empty_stream() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        // When
        final Stream<Integer> stream = failure.stream();
        // Then
        assertThat(stream).isEmpty();
    }
}
