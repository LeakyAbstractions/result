
package com.leakyabstractions.result;

import static com.leakyabstractions.result.DefaultResult.failure;
import static com.leakyabstractions.result.DefaultResult.ofOptional;
import static com.leakyabstractions.result.DefaultResult.success;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultResult#ofOptional(Optional, Supplier)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultResult ofOptional with supplier")
class DefaultResult_ofOptional_with_Supplier_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_success_when_present() {
        // Given
        final Optional<String> optional = Optional.of(SUCCESS);
        final Supplier<Integer> supplier = () -> 123;
        // When
        final Result<String, Integer> result = ofOptional(optional, supplier);
        // Then
        assertThat(result).isEqualTo(success(SUCCESS));
    }

    @Test
    void should_return_failure_when_not_present() {
        // Given
        final Optional<Integer> optional = Optional.empty();
        final Supplier<String> supplier = () -> FAILURE;
        // When
        final Result<Integer, String> result = ofOptional(optional, supplier);
        // Then
        assertThat(result).isEqualTo(failure(FAILURE));
    }
}
