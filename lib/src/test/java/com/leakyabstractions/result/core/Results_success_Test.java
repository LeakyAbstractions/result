
package com.leakyabstractions.result.core;

import static com.leakyabstractions.result.core.Results.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

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
    void should_throw_exception_if_null() {
        // When
        final Throwable thrown = catchThrowable(() -> success(null));
        // Then
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }
}
