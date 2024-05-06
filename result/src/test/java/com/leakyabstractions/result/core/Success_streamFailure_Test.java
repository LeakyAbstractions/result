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

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#streamFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success streamFailure")
class Success_streamFailure_Test {

    @Test
    void should_return_empty_stream() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        // When
        final Stream<Integer> stream = success.streamFailure();
        // Then
        assertThat(stream).isEmpty();
    }
}
