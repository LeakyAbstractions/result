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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#flatMapFailure(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure flatMapFailure")
class Failure_flatMapFailure_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_new_failure_when_function_returns_failure() {
        // Given
        final Result<Integer, Integer> failure = new Failure<>(123);
        final Result<Integer, String> another = new Failure<>(FAILURE);
        final Function<Integer, Result<Integer, String>> mapper = f -> another;
        // When
        final Result<Integer, String> result = failure.flatMapFailure(mapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void should_return_new_success_when_function_returns_success() {
        // Given
        final Result<String, Integer> failure = new Failure<>(123);
        final Result<String, Integer> another = new Success<>("SUCCESS");
        final Function<Integer, Result<String, Integer>> mapper = f -> another;
        // When
        final Result<String, Integer> result = failure.flatMapFailure(mapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void obeys_monad_laws() {
        // Given
        final Result<Void, String> result = unit(FAILURE);
        final Function<String, Result<Void, Integer>> fun1 = x -> unit(x.length());
        final Function<Integer, Result<Void, String>> fun2 = x -> unit(x * 10 + "a");
        // Then
        // Left Identity Law
        assertThat(bind(unit(FAILURE), fun1))
                .isEqualTo(fun1.apply(FAILURE))
                .isEqualTo(new Failure<>(7));
        // Right Identity Law
        assertThat(result)
                .isEqualTo(bind(result, Failure_flatMapFailure_Test::unit))
                .isEqualTo(new Failure<>(FAILURE));
        // Associativity Law
        assertThat(bind(bind(result, fun1), fun2))
                .isEqualTo(bind(result, s -> fun2.apply(unwrap(fun1.apply(s)))))
                .isEqualTo(new Failure<>("70a"));
    }

    private static <T> Result<Void, T> unit(T value) {
        return new Failure<>(value);
    }

    private static <T, T2> Result<Void, T2> bind(
            Result<Void, T> result, Function<T, Result<Void, T2>> function) {
        return result.flatMapFailure(function);
    }

    private static <T> T unwrap(Result<Void, T> result) {
        return result.getFailure().get();
    }
}
