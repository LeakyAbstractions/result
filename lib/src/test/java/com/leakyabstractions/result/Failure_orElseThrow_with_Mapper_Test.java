
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.function.Function;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#orElseThrow(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure orElseThrow with mapper")
class Failure_orElseThrow_with_Mapper_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_throw_exception() {
        // Given
        final Result<Integer, String> failure = new Failure<>(FAILURE);
        final Function<String, RuntimeException> mapper = RuntimeException::new;
        // When
        final ThrowingCallable callable = () -> failure.orElseThrow(mapper);
        // Then
        assertThatThrownBy(callable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage(FAILURE);
    }
}
