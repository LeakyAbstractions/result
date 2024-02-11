
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#ifFailure(Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure ifFailure")
class Failure_ifFailure_Test {

    @Test
    void should_perform_failure_action() {
        // Given
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final Consumer<Object> failureAction = f -> actionPerformed.set(true);
        final Result<?, ?> failure = new Failure<>("FAILURE");
        // When
        final Result<?, ?> result = failure.ifFailure(failureAction);
        // Then
        assertThat(result).isSameAs(failure);
        assertThat(actionPerformed).isTrue();
    }
}
