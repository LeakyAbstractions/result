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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#ifSuccessOrElse(Consumer, Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success ifSuccessOrElse")
class Success_ifSuccessOrElse_Test {

    @Test
    void should_perform_success_action_only() {
        // Given
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final Consumer<Object> successAction = s -> actionPerformed.set(true);
        final Consumer<Object> failureAction = f -> fail("Should not happen");
        final Result<?, ?> success = new Success<>("SUCCESS");
        // When
        final Result<?, ?> result = success.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(result).isSameAs(success);
        assertThat(actionPerformed).isTrue();
    }
}
