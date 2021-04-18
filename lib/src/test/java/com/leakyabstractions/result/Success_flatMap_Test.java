
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#flatMap(Function, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success flatMap")
class Success_flatMap_Test {

    @Test
    void should_return_new_success_when_success_function_returns_success() {
        // Given
        final Result<Integer, Integer> success = new Success<>(123);
        final Result<String, String> another = new Success<>("SUCCESS");
        final Function<Integer, Result<String, String>> successMapper = s -> another;
        final Function<Integer, Result<String, String>> failureMapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = success.flatMap(successMapper, failureMapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void should_return_new_failure_when_success_function_returns_failure() {
        // Given
        final Result<Integer, Integer> success = new Success<>(123);
        final Result<String, String> another = new Failure<>("FAILURE");
        final Function<Integer, Result<String, String>> successMapper = s -> another;
        final Function<Integer, Result<String, String>> failureMapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = success.flatMap(successMapper, failureMapper);
        // Then
        assertThat(result).isSameAs(another);
    }
}
