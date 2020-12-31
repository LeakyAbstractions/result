
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#ifSuccess(Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure ifSuccess")
class DefaultFailure_ifSuccess_Test {

    @Test
    void should_ignore_success_action() {
        // Given
        final AtomicBoolean successHandled = new AtomicBoolean(false);
        final Consumer<Object> successAction = f -> successHandled.set(true);
        final Result<?, ?> failure = new DefaultFailure<>("FAILURE");
        // When
        final Result<?, ?> result = failure.ifSuccess(successAction);
        // Then
        assertThat(result).isSameAs(failure);
        assertThat(successHandled).isFalse();
    }
}
