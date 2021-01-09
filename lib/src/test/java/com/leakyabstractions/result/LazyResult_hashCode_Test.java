
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#hashCode()}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult hashCode")
class LazyResult_hashCode_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_same_hash_code_as_the_success_supplier() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> fail("Should not happen");
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // Then
        assertThat(lazy).hasSameHashCodeAs(supplier);
    }

    @Test
    void should_return_same_hash_code_as_the_success_result() {
        // Given
        final Result<String, Integer> result = new Success<>(SUCCESS);
        final Supplier<Result<String, Integer>> supplier = () -> result;
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // When
        boolean isSuccess = lazy.isSuccess();
        // Then
        assertThat(isSuccess).isTrue();
        assertThat(lazy).hasSameHashCodeAs(result);
    }

    @Test
    void should_return_same_hash_code_as_the_failure_result() {
        // Given
        final Result<Integer, String> result = new Failure<>(FAILURE);
        final Supplier<Result<Integer, String>> supplier = () -> result;
        final Result<Integer, String> lazy = new LazyResult<>(supplier);
        // When
        boolean isSuccess = lazy.isSuccess();
        // Then
        assertThat(isSuccess).isFalse();
        assertThat(lazy).hasSameHashCodeAs(result);
    }
}
