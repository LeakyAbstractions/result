
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#streamSuccess()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure streamSuccess")
class Failure_streamSuccess_Test {

    @Test
    void should_return_empty_stream() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        // When
        final Stream<Integer> stream = failure.streamSuccess();
        // Then
        assertThat(stream).isEmpty();
    }
}
