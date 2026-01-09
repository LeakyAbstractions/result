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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#map(Function, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success map")
class Success_map_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_use_success_mapping_only() {
        // Given
        final Result<Integer, Integer> success = new Success<>(123);
        final Function<Integer, String> successMapper = s -> SUCCESS;
        final Function<Integer, String> failureMapper = f -> fail("Should not happen");
        final Result<String, String> expected = new Success<>(SUCCESS);
        // When
        final Result<String, String> result = success.map(successMapper, failureMapper);
        // Then
        assertThat(result).isEqualTo(expected);
    }
}
