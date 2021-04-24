
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#orElse(Object)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult orElse")
class LazyResult_orElse_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_other() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> new Failure<>("FAILURE");
        final Result<Integer, String> lazy = new LazyResult<>(supplier);
        // When
        final Integer value = lazy.orElse(123);
        // Then
        assertThat(value).isEqualTo(123);
    }

    @Test
    void should_ignore_other() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new Success<>(SUCCESS);
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // When
        final String value = lazy.orElse("ANOTHER");
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }
}
