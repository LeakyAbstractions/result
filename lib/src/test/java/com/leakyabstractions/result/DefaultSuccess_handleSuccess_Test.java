
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#handle(Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess handleSuccess")
class DefaultSuccess_handleSuccess_Test {

    @Test
    void should_invoke_success_handler() {
        // Given
        final AtomicBoolean successHandled = new AtomicBoolean(false);
        final Consumer<Object> successHandler = s -> successHandled.set(true);
        final Result<?, ?> success = new DefaultSuccess<>("SUCCESS");
        // When
        final Result<?, ?> result = success.handle(successHandler);
        // Then
        assertThat(result).isSameAs(success);
        assertThat(successHandled).isTrue();
    }
}
