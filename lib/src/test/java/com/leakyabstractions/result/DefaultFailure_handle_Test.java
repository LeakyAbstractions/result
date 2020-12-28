
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#handle(Consumer, Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure handle")
class DefaultFailure_handle_Test {

    @Test
    void should_invoke_failure_handler_only() {
        // Given
        final AtomicBoolean successHandled = new AtomicBoolean(false);
        final AtomicBoolean failureHandled = new AtomicBoolean(false);
        final Consumer<Object> successHandler = s -> successHandled.set(true);
        final Consumer<Object> failureHandler = f -> failureHandled.set(true);
        final Result<?, ?> failure = new DefaultFailure<>("FAILURE");
        // When
        final Result<?, ?> result = failure.handle(successHandler, failureHandler);
        // Then
        assertThat(result).isSameAs(failure);
        assertThat(successHandled).isFalse();
        assertThat(failureHandled).isTrue();
    }
}
