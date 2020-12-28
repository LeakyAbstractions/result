
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#handle(Consumer, Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess handle")
class DefaultSuccess_handle_Test {

    @Test
    void should_invoke_success_handler_only() {
        // Given
        final AtomicBoolean successHandled = new AtomicBoolean(false);
        final AtomicBoolean failureHandled = new AtomicBoolean(false);
        final Consumer<Object> successHandler = s -> successHandled.set(true);
        final Consumer<Object> failureHandler = f -> failureHandled.set(true);
        final Result<?, ?> success = new DefaultSuccess<>("SUCCESS");
        // When
        final Result<?, ?> result = success.handle(successHandler, failureHandler);
        // Then
        assertThat(result).isSameAs(success);
        assertThat(successHandled).isTrue();
        assertThat(failureHandled).isFalse();
    }
}
