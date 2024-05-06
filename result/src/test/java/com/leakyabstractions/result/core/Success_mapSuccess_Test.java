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
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#mapSuccess(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success mapSuccess")
class Success_mapSuccess_Test {

    @Test
    void should_use_success_mapping() {
        // Given
        final Result<Integer, String> success = new Success<>(123);
        final UnaryOperator<Integer> mapper = s -> s + 198;
        // When
        final Result<Integer, String> result = success.mapSuccess(mapper);
        // Then
        assertThat(result).isEqualTo(new Success<>(321));
    }

    @Test
    void should_throw_exception_when_mapper_returns_null() {
        // Given
        final Result<Integer, Integer> success = new Success<>(123);
        final Function<Integer, String> mapper = s -> null;
        // When
        final Throwable thrown = catchThrowable(() -> success.mapSuccess(mapper));
        // Then
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }
}
