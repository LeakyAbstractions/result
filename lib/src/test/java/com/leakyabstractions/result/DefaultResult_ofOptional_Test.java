
package com.leakyabstractions.result;

import static com.leakyabstractions.result.DefaultResult.ofOptional;
import static com.leakyabstractions.result.DefaultResult.success;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultResult#ofOptional(Optional)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultResult ofOptional")
class DefaultResult_ofOptional_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_success_when_present() {
        // Given
        final Optional<String> optional = Optional.of(SUCCESS);
        // When
        final Result<String, Void> result = ofOptional(optional);
        // Then
        assertThat(result).isEqualTo(success(SUCCESS));
    }

    @Test
    void should_return_failure_when_not_present() {
        // Given
        final Optional<Integer> optional = Optional.empty();
        // When
        final Result<Integer, Void> result = ofOptional(optional);
        // Then
        assertThat(result).isInstanceOf(DefaultFailure.class);
    }
}
