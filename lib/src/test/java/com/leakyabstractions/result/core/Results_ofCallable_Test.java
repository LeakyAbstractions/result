
package com.leakyabstractions.result.core;

import static com.leakyabstractions.result.core.Results.failure;
import static com.leakyabstractions.result.core.Results.ofCallable;
import static com.leakyabstractions.result.core.Results.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

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
    void should_throw_exception_when_null_callable() {
        // Given
        final Callable<Result<String, Integer>> callable = null;
        // When
        final Throwable thrown = catchThrowable(() -> Results.ofCallable(callable));
        // Then
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_throw_exception_when_callable_returns_null() {
        // Given
        final Callable<Result<String, Integer>> callable = () -> null;
        // When
        final Throwable thrown = catchThrowable(() -> Results.ofCallable(callable));
        // Then
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

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
