
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#streamSuccess()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success streamSuccess")
class Success_streamSuccess_Test {

    @Test
    void should_return_non_empty_stream() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        // When
        final Stream<String> stream = success.streamSuccess();
        // Then
        assertThat(stream).singleElement().isEqualTo("SUCCESS");
    }
}
