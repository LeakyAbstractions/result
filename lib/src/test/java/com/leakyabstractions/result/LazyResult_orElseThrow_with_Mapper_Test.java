
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Supplier;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#orElseThrow(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult orElseThrow with mapper")
class LazyResult_orElseThrow_with_Mapper_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_throw_exception() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> new DefaultFailure<>(FAILURE);
        final Result<Integer, String> lazy = new LazyResult<>(supplier);
        final Function<String, RuntimeException> failureMapper = RuntimeException::new;
        // When
        final ThrowingCallable callable = () -> lazy.orElseThrow(failureMapper);
        // Then
        assertThatThrownBy(callable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage(FAILURE);
    }

    @Test
    void should_not_throw_exception() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new DefaultSuccess<>(SUCCESS);
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, RuntimeException> failureMapper = f -> fail("Should not happen");
        // When
        final String value = lazy.orElseThrow(failureMapper);
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }

    @Test
    void should_not_throw_exception_even_if_value_is_null() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new DefaultSuccess<>(null);
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        final Function<Integer, NullPointerException> failureMapper = f -> new NullPointerException();
        // When
        final String value = lazy.orElseThrow(failureMapper);
        // Then
        assertThat(value).isNull();
    }
}
