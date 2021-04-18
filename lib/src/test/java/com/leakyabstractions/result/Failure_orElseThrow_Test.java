
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#orElseThrow()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure orElseThrow")
class Failure_orElseThrow_Test {

    @Test
    void should_throw_exception() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        // When
        final ThrowingCallable callable = () -> failure.orElseThrow();
        // Then
        assertThatThrownBy(callable).isInstanceOf(NoSuchElementException.class);
    }
}
