
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#handle(Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure handleSuccess")
class DefaultFailure_handleSuccess_Test {

    @Test
    void should_ignore_success_handler() {
        // Given
        final AtomicBoolean successHandled = new AtomicBoolean(false);
        final Consumer<Object> successHandler = f -> successHandled.set(true);
        final Result<?, ?> failure = new DefaultFailure<>("FAILURE");
        // When
        final Result<?, ?> result = failure.handle(successHandler);
        // Then
        assertThat(result).isSameAs(failure);
        assertThat(successHandled).isFalse();
    }
}
