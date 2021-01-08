
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#map(Function, Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult map")
class LazyResult_map_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Supplier<Result<Integer, Integer>> supplier = () -> fail("Should not happen");
        final LazyResult<Integer, Integer> lazy = new LazyResult<>(supplier);
        final Function<Integer, String> successMapper = s -> fail("Should not happen");
        final Function<Integer, String> failureMapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.map(successMapper, failureMapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class);
    }

    @Test
    void should_eventually_map_when_needed() {
        // Given
        final Supplier<Result<Integer, Integer>> supplier = () -> new DefaultSuccess<>(123);
        final LazyResult<Integer, Integer> lazy = new LazyResult<>(supplier);
        final Function<Integer, String> successMapper = s -> SUCCESS;
        final Function<Integer, String> failureMapper = f -> fail("Should not happen");
        // When
        final String value = lazy.map(successMapper, failureMapper).orElseThrow();
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final Supplier<Result<Integer, Integer>> supplier = () -> new DefaultFailure<>(123);
        final LazyResult<Integer, Integer> lazy = new LazyResult<>(supplier);
        final Result<Integer, String> expected = new DefaultFailure<>(FAILURE);
        final Function<Integer, String> successMapper = s -> fail("Should not happen");
        final Function<Integer, String> failureMapper = f -> FAILURE;
        // When
        final Integer value = lazy.getFailureOrElseThrow();
        final Result<String, String> result = lazy.map(successMapper, failureMapper);
        // Then
        assertThat(value).isEqualTo(123);
        assertThat(result).isEqualTo(expected);
    }
}
