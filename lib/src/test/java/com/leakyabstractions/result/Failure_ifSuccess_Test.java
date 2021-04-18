
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#ifSuccess(Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure ifSuccess")
class Failure_ifSuccess_Test {

    @Test
    void should_ignore_success_action() {
        // Given
        final Consumer<Object> successAction = s -> fail("Should not happen");
        final Result<?, ?> failure = new Failure<>("FAILURE");
        // When
        final Result<?, ?> result = failure.ifSuccess(successAction);
        // Then
        assertThat(result).isSameAs(failure);
    }
}
