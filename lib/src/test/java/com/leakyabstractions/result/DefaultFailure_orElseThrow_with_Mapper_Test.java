
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.function.Function;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#orElseThrow(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure orElseThrow with mapper")
class DefaultFailure_orElseThrow_with_Mapper_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_throw_exception() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>(FAILURE);
        final Function<String, RuntimeException> failureMapper = RuntimeException::new;
        // When
        final ThrowingCallable callable = () -> failure.orElseThrow(failureMapper);
        // Then
        assertThatThrownBy(callable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage(FAILURE);
    }
}
