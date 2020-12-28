
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#handleFailure(Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure handleFailure")
class DefaultFailureTest {

    @Test
    void should_invoke_failure_handler() {
        // Given
        final AtomicBoolean failureHandled = new AtomicBoolean(false);
        final Consumer<Object> failureHandler = s -> failureHandled.set(true);
        final Result<?, ?> failure = new DefaultFailure<>("FAILURE");
        // When
        final Result<?, ?> result = failure.handleFailure(failureHandler);
        // Then
        assertThat(result).isSameAs(failure);
        assertThat(failureHandled).isTrue();
    }
}
