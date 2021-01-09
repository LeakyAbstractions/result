
package com.leakyabstractions.result;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Results#lazy(Supplier)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Results lazy with supplier")
class Results_lazy_with_Supplier {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_pass_with_success_value() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> success(SUCCESS);
        // When
        final Result<String, Integer> result = Results.lazy(supplier);
        // Then
        assertThat(result)
                .isInstanceOf(LazyResult.class)
                .isNotEqualTo(success(SUCCESS));
    }

    @Test
    void should_pass_with_failure_value() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> failure(FAILURE);
        // When
        final Result<Integer, String> result = Results.lazy(supplier);
        // Then
        assertThat(result)
                .isInstanceOf(LazyResult.class)
                .isNotEqualTo(failure(FAILURE));
    }
}
