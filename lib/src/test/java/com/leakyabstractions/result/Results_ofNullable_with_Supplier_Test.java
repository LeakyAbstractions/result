
package com.leakyabstractions.result;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.ofNullable;
import static com.leakyabstractions.result.Results.success;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Results#ofNullable(Object, Supplier)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Results ofNullable with supplier")
class Results_ofNullable_with_Supplier_Test {

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
