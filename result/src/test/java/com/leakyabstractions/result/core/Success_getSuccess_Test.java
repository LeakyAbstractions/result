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

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#getSuccess()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success getSuccess")
class Success_getSuccess_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_success_value() {
        // Given
        final Result<String, Integer> result = new Success<>(SUCCESS);
        // When
        final Optional<String> success = result.getSuccess();
        // Then
        assertThat(success).containsSame(SUCCESS);
    }
}
