/*
 * Copyright 2025 Guillermo Calvo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leakyabstractions.result.core;

import static com.leakyabstractions.result.core.Results.failure;
import static com.leakyabstractions.result.core.Results.ofCallable;
import static com.leakyabstractions.result.core.Results.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Results#ofCallable(Callable, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Results ofCallable with function")
class Results_ofCallable_with_Function_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_throw_exception_when_null_callable() {
        // Given
        final Callable<String> callable = null;
        final Function<String, String> mapper = Function.identity();
        // When
        final Throwable thrown = catchThrowable(() -> Results.ofCallable(callable, mapper));
        // Then
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_throw_exception_when_null_function() {
        // Given
        final Callable<String> callable = () -> SUCCESS;
        final Function<String, String> mapper = null;
        // When
        final Throwable thrown = catchThrowable(() -> Results.ofCallable(callable, mapper));
        // Then
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_return_success_when_callable_returns_null() {
        // Given
        final Callable<String> callable = () -> null;
        final Function<String, Optional<String>> mapper = Optional::ofNullable;
        // When
        final Result<Optional<String>, Exception> result = Results.ofCallable(callable, mapper);
        // Then
        assertThat(result).isEqualTo(success(Optional.empty()));
    }

    @Test
    void should_return_failure_when_callable_returns_null() {
        // Given
        final Callable<String> callable = () -> null;
        final Function<String, String> mapper = Objects::requireNonNull;
        // When
        final Result<String, Exception> result = Results.ofCallable(callable, mapper);
        // Then
        assertThat(result.getFailure()).containsInstanceOf(NullPointerException.class);
    }

    @Test
    void should_throw_exception_when_function_returns_null() {
        // Given
        final Callable<String> callable = () -> SUCCESS;
        final Function<String, String> mapper = x -> null;
        // When
        final Throwable thrown = catchThrowable(() -> Results.ofCallable(callable, mapper));
        // Then
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_return_success_when_no_exception() {
        // Given
        final Callable<String> callable = () -> SUCCESS;
        final Function<String, String> mapper = String::toLowerCase;
        // When
        final Result<String, Exception> result = ofCallable(callable, mapper);
        // Then
        assertThat(result).isEqualTo(success(SUCCESS.toLowerCase()));
    }

    @Test
    void should_return_failure_when_callable_throws_exception() {
        // Given
        final IOException exception = new IOException(FAILURE);
        final Callable<String> callable = () -> {
            throw exception;
        };
        final Function<String, String> mapper = x -> SUCCESS;
        // When
        final Result<String, Exception> result = ofCallable(callable, mapper);
        // Then
        assertThat(result).isEqualTo(failure(exception));
    }

    @Test
    void should_return_failure_when_function_throws_exception() {
        // Given
        final RuntimeException exception = new RuntimeException(FAILURE);
        final Callable<String> callable = () -> SUCCESS;
        final Function<String, String> mapper = x -> {
            throw exception;
        };
        // When
        final Result<String, Exception> result = ofCallable(callable, mapper);
        // Then
        assertThat(result).isEqualTo(failure(exception));
    }
}
