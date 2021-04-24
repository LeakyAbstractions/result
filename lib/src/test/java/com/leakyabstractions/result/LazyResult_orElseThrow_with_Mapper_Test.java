
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
        final Supplier<Result<Integer, String>> supplier = () -> new Failure<>(FAILURE);
        final Result<Integer, String> lazy = new LazyResult<>(supplier);
        final Function<String, RuntimeException> mapper = RuntimeException::new;
        // When
        final ThrowingCallable callable = () -> lazy.orElseThrow(mapper);
        // Then
        assertThatThrownBy(callable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage(FAILURE);
    }

    @Test
    void should_not_throw_exception() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Success<>(SUCCESS);
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final Function<String, RuntimeException> mapper = f -> fail("Should not happen");
        // When
        final String value = lazy.orElseThrow(mapper);
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }
}
