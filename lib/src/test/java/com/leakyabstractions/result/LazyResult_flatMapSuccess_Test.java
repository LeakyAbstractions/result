
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#flatMapSuccess(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult flatMapSuccess")
class LazyResult_flatMapSuccess_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> fail("Should not happen");
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Result<String, String>> mapper = s -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.flatMapSuccess(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class);
    }

    @Test
    void should_eventually_flat_map_when_needed() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Success<>(SUCCESS);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Result<String, String>> mapper = s -> new Failure<>(FAILURE);
        // When
        final String value = lazy.flatMapSuccess(mapper).getFailureOrElseThrow();
        // Then
        assertThat(value).isSameAs(FAILURE);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Success<>(SUCCESS);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Result<String, String> another = new Success<>("ANOTHER");
        final Function<String, Result<String, String>> mapper = s -> another;
        // When
        final String value = lazy.orElseThrow();
        final Result<String, String> result = lazy.flatMapSuccess(mapper);
        // Then
        assertThat(value).isSameAs(SUCCESS);
        assertThat(result).isSameAs(another);
    }
}
