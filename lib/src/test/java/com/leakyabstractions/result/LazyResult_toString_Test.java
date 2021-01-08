
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#toString()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult toString")
class LazyResult_toString_Test {

    @Test
    void should_return_expected_string_when_not_supplied() {
        // Given
        final Supplier<Result<String, Integer>> supplier = new Supplier<Result<String, Integer>>() {
            @Override
            public Result<String, Integer> get() {
                return fail("Should not happen");
            }

            @Override
            public String toString() {
                return "TESTING";
            }
        };
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // Then
        assertThat(lazy).hasToString("lazy-result[TESTING]");
    }

    @Test
    void should_return_expected_string_when_supplied() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new DefaultSuccess<>("TESTING");
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // When
        lazy.orElseThrow();
        // Then
        assertThat(lazy).hasToString("lazy-result[success[TESTING]]");
    }
}
