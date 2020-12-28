
package com.leakyabstractions.result;

import static com.leakyabstractions.result.DefaultResult.failure;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultResult#failure(Object)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultResult failure")
class DefaultResult_failure_Test {

    @Test
    void should_pass_with_failure_value() {
        // When
        final Result<Void, String> result = failure("FAILURE");
        // Then
        assertThat(result).isInstanceOf(DefaultFailure.class);
    }

    @Test
    void should_pass_even_if_null() {
        // When
        final ThrowingCallable callable = () -> failure(null);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }
}
