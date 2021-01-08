
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#mapSuccess(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult mapSuccess")
class LazyResult_mapSuccess_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";
    private static final String ANOTHER = "ANOTHER";

    @Test
    void should_be_lazy() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new DefaultSuccess<>(SUCCESS);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Integer> successMapper = s -> fail("Should not happen");
        // When
        final Result<Integer, String> result = lazy.mapSuccess(successMapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class);
    }

    @Test
    void should_eventually_map_when_needed() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new DefaultSuccess<>(SUCCESS);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, String> successMapper = s -> ANOTHER;
        // When
        final String value = lazy.mapSuccess(successMapper).orElseThrow();
        // Then
        assertThat(value).isSameAs(ANOTHER);
    }

    @Test
    void should_ignore_success_mapping() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new DefaultFailure<>(FAILURE);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, String> successMapper = s -> fail("Should not happen");
        // When
        final String value = lazy.mapSuccess(successMapper).getFailureOrElseThrow();
        // Then
        assertThat(value).isSameAs(FAILURE);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> new DefaultSuccess<>(123);
        final LazyResult<Integer, String> lazy = new LazyResult<>(supplier);
        final Function<Integer, String> successMapper = s -> SUCCESS;
        final DefaultSuccess<String, Object> expected = new DefaultSuccess<>(SUCCESS);
        // When
        final Integer value = lazy.orElseThrow();
        final Result<String, String> result = lazy.mapSuccess(successMapper);
        // Then
        assertThat(value).isEqualTo(123);
        assertThat(result).isEqualTo(expected);
    }
}
