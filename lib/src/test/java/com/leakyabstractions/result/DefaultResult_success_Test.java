
package com.leakyabstractions.result;

import static com.leakyabstractions.result.DefaultResult.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultResult#success(Object)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultResult success")
class DefaultResult_success_Test {

    @Test
    void should_pass_with_success_value() {
        // When
        final Result<String, Void> result = success("SUCCESS");
        // Then
        assertThat(result).isInstanceOf(DefaultSuccess.class);
    }

    @Test
    void should_pass_even_if_null() {
        // When
        final ThrowingCallable callable = () -> success(null);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }
}
