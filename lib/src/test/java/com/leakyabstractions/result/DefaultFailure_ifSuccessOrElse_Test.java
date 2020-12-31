
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#ifSuccessOrElse(Consumer, Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure ifSuccessOrElse")
class DefaultFailure_ifSuccessOrElse_Test {

    @Test
    void should_perform_failure_action_only() {
        // Given
        final AtomicBoolean successHandled = new AtomicBoolean(false);
        final AtomicBoolean failureHandled = new AtomicBoolean(false);
        final Consumer<Object> successAction = s -> successHandled.set(true);
        final Consumer<Object> failureAction = f -> failureHandled.set(true);
        final Result<?, ?> failure = new DefaultFailure<>("FAILURE");
        // When
        final Result<?, ?> result = failure.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(result).isSameAs(failure);
        assertThat(successHandled).isFalse();
        assertThat(failureHandled).isTrue();
    }
}
