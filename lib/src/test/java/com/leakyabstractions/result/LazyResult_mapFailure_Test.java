
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#mapFailure(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult mapFailure")
class LazyResult_mapFailure_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";
    private static final String ANOTHER = "ANOTHER";

    @Test
    void should_be_lazy() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Failure<>(FAILURE);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Integer> mapper = f -> fail("Should not happen");
        // When
        final Result<String, Integer> result = lazy.mapFailure(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class);
    }

    @Test
    void should_eventually_map_when_needed() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Failure<>(FAILURE);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, String> mapper = f -> ANOTHER;
        // When
        final Optional<String> value = lazy.mapFailure(mapper).optionalFailure();
        // Then
        assertThat(value).containsSame(ANOTHER);
    }

    @Test
    void should_ignore_failure_mapping() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Success<>(SUCCESS);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, String> mapper = f -> fail("Should not happen");
        // When
        final String value = lazy.mapFailure(mapper).orElse(null);
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new Failure<>(123);
        final LazyResult<String, Integer> lazy = new LazyResult<>(supplier);
        final Function<Integer, String> mapper = f -> FAILURE;
        final Failure<Object, String> expected = new Failure<>(FAILURE);
        // When
        final Optional<Integer> value = lazy.optionalFailure();
        final Result<String, String> result = lazy.mapFailure(mapper);
        // Then
        assertThat(value).containsSame(123);
        assertThat(result).isEqualTo(expected);
    }
}
