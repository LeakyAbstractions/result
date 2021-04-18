
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#equals(Object)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult equals")
class LazyResult_equals_Test {

    @Test
    void should_be_equal_to_itself() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> fail("Should not happen");
        final Result<String, Integer> async = new LazyResult<>(supplier);
        // When
        final boolean equals = async.equals(async);
        // Then
        assertThat(equals).isTrue();
    }

    @Test
    void should_be_equal_to_an_async_result_with_the_same_supplier() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> fail("Should not happen");
        final Result<String, Integer> async = new LazyResult<>(supplier);
        final Result<String, Integer> expected = new LazyResult<>(supplier);
        // Then
        assertThat(async).isEqualTo(expected);
    }

    @Test
    void should_not_be_equal_to_the_same_supplier() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> fail("Should not happen");
        final Result<String, Integer> async = new LazyResult<>(supplier);
        // Then
        assertThat(async).isNotEqualTo(supplier);
    }

    @Test
    void should_not_be_equal_to_an_async_result_with_a_different_supplier() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> fail("Should not happen");
        final Result<String, Integer> async = new LazyResult<>(supplier);
        final LazyResult<String, Integer> another = new LazyResult<>(() -> fail("Should not happen"));
        // Then
        assertThat(async).isNotEqualTo(another);
    }
}
