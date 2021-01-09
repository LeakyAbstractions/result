
package com.leakyabstractions.result;

import static com.leakyabstractions.result.Results.failure;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Results#failure(Object)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Results failure")
class Results_failure_Test {

    @Test
    void should_pass_with_failure_value() {
        // When
        final Result<Void, String> result = failure("FAILURE");
        // Then
        assertThat(result).isInstanceOf(Failure.class);
    }

    @Test
    void should_pass_even_if_null() {
        // When
        final ThrowingCallable callable = () -> failure(null);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }
}
