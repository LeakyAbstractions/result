
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#isFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult isFailure")
class LazyResult_isFailure_Test {

    @Test
    void should_return_false() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new Success<>("SUCCESS");
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // When
        final boolean isFailure = lazy.isFailure();
        // Then
        assertThat(isFailure).isFalse();
    }

    @Test
    void should_return_true() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> new Failure<>("FAILURE");
        final Result<Integer, String> lazy = new LazyResult<>(supplier);
        // When
        final boolean isFailure = lazy.isFailure();
        // Then
        assertThat(isFailure).isTrue();
    }
}
