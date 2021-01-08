
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#isSuccess()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult isSuccess")
class LazyResult_isSuccess_Test {

    @Test
    void should_return_true() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new DefaultSuccess<>("SUCCESS");
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // When
        final boolean isSuccess = lazy.isSuccess();
        // Then
        assertThat(isSuccess).isTrue();
    }

    @Test
    void should_return_true_even_if_value_is_null() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new DefaultSuccess<>(null);
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // When
        final boolean isSuccess = lazy.isSuccess();
        // Then
        assertThat(isSuccess).isTrue();
    }

    @Test
    void should_return_false() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> new DefaultFailure<>("FAILURE");
        final Result<Integer, String> lazy = new LazyResult<>(supplier);
        // When
        final boolean isSuccess = lazy.isSuccess();
        // Then
        assertThat(isSuccess).isFalse();
    }
}
