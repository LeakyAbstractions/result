
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#mapFailure(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess mapFailure")
class DefaultSuccess_mapFailure_Test {

    @Test
    void should_ignore_failure_mapping() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>("SUCCESS");
        final Function<Integer, String> failureMapper = f -> "FAILURE";
        // When
        final Result<String, String> result = success.mapFailure(failureMapper);
        // Then
        assertThat(result).isEqualTo(success);
    }
}
