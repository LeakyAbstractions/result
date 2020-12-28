
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#handleFailure(Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess handleFailure")
class DefaultSuccess_handleFailure_Test {

    @Test
    void should_ignore_failure_handler() {
        // Given
        final AtomicBoolean failureHandled = new AtomicBoolean(false);
        final Consumer<Object> failureHandler = f -> failureHandled.set(true);
        final Result<?, ?> success = new DefaultSuccess<>("SUCCESS");
        // When
        final Result<?, ?> result = success.handleFailure(failureHandler);
        // Then
        assertThat(result).isSameAs(success);
        assertThat(failureHandled).isFalse();
    }
}
