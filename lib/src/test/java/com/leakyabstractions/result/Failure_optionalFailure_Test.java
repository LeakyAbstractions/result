
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#optionalFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure optionalFailure")
class Failure_optionalFailure_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_non_empty_optional() {
        // Given
        final Result<Integer, String> failure = new Failure<>(FAILURE);
        // When
        final Optional<String> optional = failure.optionalFailure();
        // Then
        assertThat(optional).containsSame(FAILURE);
    }
}
