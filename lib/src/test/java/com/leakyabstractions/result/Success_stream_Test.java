
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#stream()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success stream")
class Success_stream_Test {

    @Test
    void should_return_non_empty_stream() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        // When
        final Stream<String> stream = success.stream();
        // Then
        assertThat(stream).singleElement().isEqualTo("SUCCESS");
    }
}
