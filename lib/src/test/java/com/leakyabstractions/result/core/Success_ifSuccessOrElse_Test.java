
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#ifSuccessOrElse(Consumer, Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success ifSuccessOrElse")
class Success_ifSuccessOrElse_Test {

    @Test
    void should_perform_success_action_only() {
        // Given
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final Consumer<Object> successAction = s -> actionPerformed.set(true);
        final Consumer<Object> failureAction = f -> fail("Should not happen");
        final Result<?, ?> success = new Success<>("SUCCESS");
        // When
        final Result<?, ?> result = success.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(result).isSameAs(success);
        assertThat(actionPerformed).isTrue();
    }
}
