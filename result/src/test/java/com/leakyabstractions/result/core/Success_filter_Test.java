/*
 * Copyright 2024 Guillermo Calvo
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
 * Tests for {@link Success#filter(Predicate, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success filter")
class Success_filter_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_itself_when_predicate_returns_true() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        final Predicate<String> isAcceptable = s -> true;
        final Function<String, Integer> mapper = s -> fail("Should not happen");
        // When
        final Result<String, Integer> result = success.filter(isAcceptable, mapper);
        // Then
        assertThat(result).isSameAs(success);
    }

    @Test
    void should_return_failure_when_predicate_returns_false() {
        // Given
        final Result<Integer, String> success = new Success<>(321);
        final Predicate<Integer> isAcceptable = s -> false;
        final Function<Integer, String> mapper = s -> FAILURE;
        final Result<Integer, String> expected = new Failure<>(FAILURE);
        // When
        final Result<Integer, String> result = success.filter(isAcceptable, mapper);
        // Then
        assertThat(result).isEqualTo(expected);
    }
}
