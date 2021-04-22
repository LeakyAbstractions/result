
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#streamFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult streamFailure")
class LazyResult_streamFailure_Test {

    @Test
    void should_return_non_empty_stream() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new Success<>("SUCCESS");
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // When
        final Stream<Integer> stream = lazy.streamFailure();
        // Then
        assertThat(stream).isEmpty();
    }

    @Test
    void should_return_empty_stream() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> new Failure<>("FAILURE");
        final Result<Integer, String> lazy = new LazyResult<>(supplier);
        // When
        final Stream<String> stream = lazy.streamFailure();
        // Then
        assertThat(stream).singleElement().isEqualTo("FAILURE");
    }
}
