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
 * Tests for {@link Success#mapFailure(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success mapFailure")
class Success_mapFailure_Test {

    @Test
    void should_ignore_failure_mapping() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        final Function<Integer, String> mapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = success.mapFailure(mapper);
        // Then
        assertThat(result).isSameAs(success);
    }
}
