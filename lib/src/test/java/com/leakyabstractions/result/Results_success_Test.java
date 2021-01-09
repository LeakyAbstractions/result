
package com.leakyabstractions.result;

import static com.leakyabstractions.result.Results.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Results#success(Object)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Results success")
class Results_success_Test {

    @Test
    void should_pass_with_success_value() {
        // When
        final Result<String, Void> result = success("SUCCESS");
        // Then
        assertThat(result).isInstanceOf(Success.class);
    }

    @Test
    void should_pass_even_if_null() {
        // When
        final ThrowingCallable callable = () -> success(null);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }
}
