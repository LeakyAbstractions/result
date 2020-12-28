
package com.leakyabstractions.result;

import static com.leakyabstractions.result.DefaultResult.failure;
import static com.leakyabstractions.result.DefaultResult.ofNullable;
import static com.leakyabstractions.result.DefaultResult.success;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultResult#ofNullable(Object, Supplier)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultResult ofNullable with supplier")
class DefaultResult_ofNullable_with_Supplier_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_success_when_not_null() {
        // Given
        final String nullable = SUCCESS;
        final Supplier<String> failureSupplier = () -> FAILURE;
        // When
        final Result<String, String> result = ofNullable(nullable, failureSupplier);
        // Then
        assertThat(result).isEqualTo(success(SUCCESS));
    }

    @Test
    void should_return_failure_when_null() {
        // Given
        final Integer nullable = null;
        final Supplier<String> failureSupplier = () -> FAILURE;
        // When
        final Result<Integer, String> result = ofNullable(nullable, failureSupplier);
        // Then
        assertThat(result).isEqualTo(failure(FAILURE));
    }
}
