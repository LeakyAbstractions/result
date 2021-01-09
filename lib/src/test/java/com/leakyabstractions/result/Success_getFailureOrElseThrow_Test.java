
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#getFailureOrElseThrow()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Success getFailureOrElseThrow")
class Success_getFailureOrElseThrow_Test {

    @Test
    void should_throw_exception() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        // When
        final ThrowingCallable callable = () -> success.getFailureOrElseThrow();
        // Then
        assertThatThrownBy(callable).isInstanceOf(NoSuchElementException.class);
    }
}
