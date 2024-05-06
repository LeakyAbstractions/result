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

import static com.leakyabstractions.result.core.Results.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Results#success(Object)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Results success")
class Results_success_Test {

    @Test
    void should_pass_with_success_value() {
        // When
        final Result<String, Void> result = success("SUCCESS");
        // Then
        assertThat(result).isInstanceOf(Success.class);
    }

    @Test
    void should_throw_exception_if_null() {
        // When
        final Throwable thrown = catchThrowable(() -> success(null));
        // Then
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }
}
