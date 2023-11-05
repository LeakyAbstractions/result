
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#streamFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success streamFailure")
class Success_streamFailure_Test {

    @Test
    void should_return_empty_stream() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        // When
        final Stream<Integer> stream = success.streamFailure();
        // Then
        assertThat(stream).isEmpty();
    }
}
