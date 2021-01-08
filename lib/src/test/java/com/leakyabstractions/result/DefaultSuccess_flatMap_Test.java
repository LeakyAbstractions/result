
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#flatMap(Function, Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess flatMap")
class DefaultSuccess_flatMap_Test {

    @Test
    void should_return_new_success_when_success_function_returns_success() {
        // Given
        final Result<Integer, Integer> success = new DefaultSuccess<>(123);
        final Result<String, String> another = new DefaultSuccess<>("SUCCESS");
        final Function<Integer, Result<String, String>> successFlatMapper = s -> another;
        final Function<Integer, Result<String, String>> failureFlatMapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = success.flatMap(successFlatMapper, failureFlatMapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void should_return_new_failure_when_success_function_returns_failure() {
        // Given
        final Result<Integer, Integer> success = new DefaultSuccess<>(123);
        final Result<String, String> another = new DefaultFailure<>("FAILURE");
        final Function<Integer, Result<String, String>> successFlatMapper = s -> another;
        final Function<Integer, Result<String, String>> failureFlatMapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = success.flatMap(successFlatMapper, failureFlatMapper);
        // Then
        assertThat(result).isSameAs(another);
    }
}
