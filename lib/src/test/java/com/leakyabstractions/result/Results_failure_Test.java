
package com.leakyabstractions.result;

import static com.leakyabstractions.result.Results.failure;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

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
    void should_throw_exception_if_null() {
        // When
        final Throwable thrown = catchThrowable(() -> failure(null));
        // Then
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }
}
