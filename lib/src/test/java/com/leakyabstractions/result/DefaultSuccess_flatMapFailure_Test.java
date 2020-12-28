
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultSuccess#flatMapFailure(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultSuccess flatMapFailure")
class DefaultSuccess_flatMapFailure_Test {

    @Test
    void should_return_equal_success_no_matter_what() {
        // Given
        final Result<String, Integer> success = new DefaultSuccess<>("SUCCESS");
        final Function<Integer, Result<String, String>> failureFlatMapper = s -> new DefaultFailure<>("FAILURE");
        // When
        final Result<String, String> result = success.flatMapFailure(failureFlatMapper);
        // Then
        assertThat(result).isEqualTo(success);
    }
}
