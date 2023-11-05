
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#streamFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure streamFailure")
class Failure_streamFailure_Test {

    @Test
    void should_return_non_empty_stream() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        // When
        final Stream<String> stream = failure.streamFailure();
        // Then
        assertThat(stream).singleElement().isEqualTo("FAILURE");
    }
}
