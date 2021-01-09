
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#getFailureOrElseThrow()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult getFailureOrElseThrow")
class LazyResult_getFailureOrElseThrow_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_throw_exception() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new Success<>("SUCCESS");
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // When
        final ThrowingCallable callable = () -> lazy.getFailureOrElseThrow();
        // Then
        assertThatThrownBy(callable).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void should_return_same_value() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> new Failure<>(FAILURE);
        final Result<Integer, String> lazy = new LazyResult<>(supplier);
        // When
        final String value = lazy.getFailureOrElseThrow();
        // Then
        assertThat(value).isSameAs(FAILURE);
    }

    @Test
    void should_not_throw_exception_even_if_value_is_null() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> new Failure<>(null);
        final Result<Integer, String> lazy = new LazyResult<>(supplier);
        // When
        final String value = lazy.getFailureOrElseThrow();
        // Then
        assertThat(value).isNull();
    }
}
