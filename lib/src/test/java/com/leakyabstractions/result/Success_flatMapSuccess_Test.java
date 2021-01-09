
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#flatMapSuccess(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Success flatMapSuccess")
class Success_flatMapSuccess_Test {

    @Test
    void should_return_new_failure_when_function_returns_failure() {
        // Given
        final Result<Integer, String> success = new Success<>(123);
        final Result<String, String> another = new Failure<>("FAILURE");
        final Function<Integer, Result<String, String>> successFlatMapper = s -> another;
        // When
        final Result<String, String> result = success.flatMapSuccess(successFlatMapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void should_return_new_success_when_function_returns_success() {
        // Given
        final Result<Integer, Integer> success = new Success<>(123);
        final Result<String, Integer> another = new Success<>("SUCCESS");
        final Function<Integer, Result<String, Integer>> successFlatMapper = s -> another;
        // When
        final Result<String, Integer> result = success.flatMapSuccess(successFlatMapper);
        // Then
        assertThat(result).isSameAs(another);
    }
}
