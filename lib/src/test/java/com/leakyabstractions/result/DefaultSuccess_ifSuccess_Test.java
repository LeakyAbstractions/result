
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#ifSuccess(Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess ifSuccess")
class DefaultSuccess_ifSuccess_Test {

    @Test
    void should_perform_success_action() {
        // Given
        final AtomicBoolean successHandled = new AtomicBoolean(false);
        final Consumer<Object> successAction = s -> successHandled.set(true);
        final Result<?, ?> success = new DefaultSuccess<>("SUCCESS");
        // When
        final Result<?, ?> result = success.ifSuccess(successAction);
        // Then
        assertThat(result).isSameAs(success);
        assertThat(successHandled).isTrue();
    }
}
