
package com.leakyabstractions.result;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.ofOptional;
import static com.leakyabstractions.result.Results.success;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
