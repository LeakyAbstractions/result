
package com.leakyabstractions.result;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.ofCallable;
import static com.leakyabstractions.result.Results.success;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Results#ofCallable(Callable)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Results ofCallable")
class Results_ofCallable_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_success_when_no_exception() {
        // Given
        final Callable<String> callable = () -> SUCCESS;
        // When
        final Result<String, Exception> result = ofCallable(callable);
        // Then
        assertThat(result).isEqualTo(success(SUCCESS));
    }

    @Test
    void should_return_failure_when_exception() {
        // Given
        final IOException exception = new IOException(FAILURE);
        final Callable<String> callable = () -> {
            throw exception;
        };
        // When
        final Result<String, Exception> result = ofCallable(callable);
        // Then
        assertThat(result).isEqualTo(failure(exception));
    }
}
