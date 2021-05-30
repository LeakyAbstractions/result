
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#optionalFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult optionalFailure")
class LazyResult_optionalFailure_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_non_empty_optional() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new Success<>("SUCCESS");
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // When
        final Optional<Integer> optional = lazy.optionalFailure();
        // Then
        assertThat(optional).isEmpty();
    }

    @Test
    void should_return_empty_optional() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> new Failure<>(FAILURE);
        final Result<Integer, String> lazy = new LazyResult<>(supplier);
        // When
        final Optional<String> optional = lazy.optionalFailure();
        // Then
        assertThat(optional).containsSame(FAILURE);
    }
}
