
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#flatMapFailure(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult flatMapFailure")
class LazyResult_flatMapFailure_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> fail("Should not happen");
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Result<String, String>> failureFlatMapper = s -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.flatMapFailure(failureFlatMapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class);
    }

    @Test
    void should_eventually_flat_map_when_needed() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new DefaultFailure<>(FAILURE);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Result<String, String>> failureFlatMapper = s -> new DefaultSuccess<>(SUCCESS);
        // When
        final String value = lazy.flatMapFailure(failureFlatMapper).orElseThrow();
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new DefaultFailure<>(FAILURE);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Result<String, String> another = new DefaultFailure<>("ANOTHER");
        final Function<String, Result<String, String>> failureFlatMapper = s -> another;
        // When
        final String value = lazy.getFailureOrElseThrow();
        final Result<String, String> result = lazy.flatMapFailure(failureFlatMapper);
        // Then
        assertThat(value).isSameAs(FAILURE);
        assertThat(result).isSameAs(another);
    }
}
