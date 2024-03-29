
package com.leakyabstractions.result.core;

import static com.leakyabstractions.result.core.Results.failure;
import static com.leakyabstractions.result.core.Results.ofOptional;
import static com.leakyabstractions.result.core.Results.success;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Results#ofOptional(Optional, Object)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Results ofOptional")
class Results_ofOptional_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_success_when_present() {
        // Given
        final Optional<String> optional = Optional.of(SUCCESS);
        // When
        final Result<String, String> result = ofOptional(optional, FAILURE);
        // Then
        assertThat(result).isEqualTo(success(SUCCESS));
    }

    @Test
    void should_return_failure_when_not_present() {
        // Given
        final Optional<Integer> optional = Optional.empty();
        // When
        final Result<Integer, String> result = ofOptional(optional, FAILURE);
        // Then
        assertThat(result).isEqualTo(failure(FAILURE));
    }
}
