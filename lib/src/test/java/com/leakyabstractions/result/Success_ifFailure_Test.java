
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#ifFailure(Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success ifFailure")
class Success_ifFailure_Test {

    @Test
    void should_ignore_failure_action() {
        // Given
        final Consumer<Object> failureAction = f -> fail("Should not happen");
        final Result<?, ?> success = new Success<>("SUCCESS");
        // When
        final Result<?, ?> result = success.ifFailure(failureAction);
        // Then
        assertThat(result).isSameAs(success);
    }
}
