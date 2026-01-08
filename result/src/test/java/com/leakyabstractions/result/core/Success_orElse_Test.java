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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#orElse(Object)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success orElse")
class Success_orElse_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_ignore_other() {
        // Given
        final Result<String, Integer> success = new Success<>(SUCCESS);
        // When
        final String value = success.orElse("ANOTHER");
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }
}
