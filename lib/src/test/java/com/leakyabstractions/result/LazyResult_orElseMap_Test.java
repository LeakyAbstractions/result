
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#orElseMap(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult orElseMap")
class LazyResult_orElseMap_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_use_failure_mapper() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> new Failure<>("FAILURE");
        final Result<Integer, String> lazy = new LazyResult<>(supplier);
        final Function<String, Integer> failureMapper = f -> 321;
        // When
        final Integer value = lazy.orElseMap(failureMapper);
        // Then
        assertThat(value).isEqualTo(321);
    }

    @Test
    void should_ignore_failure_mapper() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new Success<>(SUCCESS);
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        final Function<Integer, String> failureMapper = f -> fail("Should not happen");
        // When
        final String value = lazy.orElseMap(failureMapper);
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_ignore_failure_mapper_even_if_value_is_null() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new Success<>(null);
        final Result<String, Integer> success = new LazyResult<>(supplier);
        final Function<Integer, String> failureMapper = f -> fail("Should not happen");
        // When
        final String value = success.orElseMap(failureMapper);
        // Then
        assertThat(value).isNull();
    }
}
