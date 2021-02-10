
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#flatMapFailure(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Success flatMapFailure")
class Success_flatMapFailure_Test {

    @Test
    void should_return_equal_success_no_matter_what() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        final Function<Integer, Result<String, String>> mapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = success.flatMapFailure(mapper);
        // Then
        assertThat(result).isEqualTo(success);
    }
}
