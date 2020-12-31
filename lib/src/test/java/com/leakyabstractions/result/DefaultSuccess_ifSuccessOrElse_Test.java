
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#ifSuccessOrElse(Consumer, Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess ifSuccessOrElse")
class DefaultSuccess_ifSuccessOrElse_Test {

    @Test
    void should_perform_success_action_only() {
        // Given
        final AtomicBoolean successHandled = new AtomicBoolean(false);
        final AtomicBoolean failureHandled = new AtomicBoolean(false);
        final Consumer<Object> successAction = s -> successHandled.set(true);
        final Consumer<Object> failureAction = f -> failureHandled.set(true);
        final Result<?, ?> success = new DefaultSuccess<>("SUCCESS");
        // When
        final Result<?, ?> result = success.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(result).isSameAs(success);
        assertThat(successHandled).isTrue();
        assertThat(failureHandled).isFalse();
    }
}
