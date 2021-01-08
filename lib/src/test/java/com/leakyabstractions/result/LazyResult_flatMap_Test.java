
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#flatMap(Function, Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult flatMap")
class LazyResult_flatMap_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> fail("Should not happen");
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Result<String, String>> successFlatMapper = s -> fail("Should not happen");
        final Function<String, Result<String, String>> failureFlatMapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.flatMap(successFlatMapper, failureFlatMapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class);
    }

    @Test
    void should_eventually_flat_map_when_needed() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new DefaultFailure<>(FAILURE);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, Result<String, String>> successFlatMapper = s -> fail("Should not happen");
        final Function<String, Result<String, String>> failureFlatMapper = f -> new DefaultSuccess<>(SUCCESS);
        // When
        final String value = lazy.flatMap(successFlatMapper, failureFlatMapper).orElseThrow();
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new DefaultSuccess<>(SUCCESS);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Result<String, String> another = new DefaultSuccess<>("ANOTHER");
        final Function<String, Result<String, String>> successFlatMapper = s -> another;
        final Function<String, Result<String, String>> failureFlatMapper = f -> fail("Should not happen");
        // When
        final String value = lazy.orElseThrow();
        final Result<String, String> result = lazy.flatMap(successFlatMapper, failureFlatMapper);
        // Then
        assertThat(value).isSameAs(SUCCESS);
        assertThat(result).isSameAs(another);
    }
}
