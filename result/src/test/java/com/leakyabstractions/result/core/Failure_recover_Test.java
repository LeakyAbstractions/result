/*
 * Copyright 2026 Guillermo Calvo
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
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#recover(Predicate, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure recover")
class Failure_recover_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_itself_when_predicate_returns_false() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        final Predicate<String> isRecoverable = s -> false;
        final Function<String, Integer> mapper = s -> fail("Should not happen");
        // When
        final Result<Integer, String> result = failure.recover(isRecoverable, mapper);
        // Then
        assertThat(result).isSameAs(failure);
    }

    @Test
    void should_return_success_when_predicate_returns_true() {
        // Given
        final Result<String, Integer> failure = new Failure<>(321);
        final Predicate<Integer> isRecoverable = s -> true;
        final Function<Integer, String> mapper = s -> SUCCESS;
        final Result<String, Integer> expected = new Success<>(SUCCESS);
        // When
        final Result<String, Integer> result = failure.recover(isRecoverable, mapper);
        // Then
        assertThat(result).isEqualTo(expected);
    }
}
