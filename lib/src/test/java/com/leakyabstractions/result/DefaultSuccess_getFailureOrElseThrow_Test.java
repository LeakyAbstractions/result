
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#getFailureOrElseThrow()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess getFailureOrElseThrow")
class DefaultSuccess_getFailureOrElseThrow_Test {

    @Test
    void should_throw_exception() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>("SUCCESS");
        // When
        final ThrowingCallable callable = () -> success.getFailureOrElseThrow();
        // Then
        assertThatThrownBy(callable).isInstanceOf(NoSuchElementException.class);
    }
}
