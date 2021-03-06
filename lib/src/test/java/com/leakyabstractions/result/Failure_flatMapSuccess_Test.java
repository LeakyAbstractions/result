
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#flatMapSuccess(Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Failure flatMapSuccess")
class Failure_flatMapSuccess_Test {

    @Test
    void should_return_equal_failure_no_matter_what() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        final Function<Integer, Result<String, String>> successFlatMapper = s -> fail("Should not happen");
        // When
        final Result<String, String> result = failure.flatMapSuccess(successFlatMapper);
        // Then
        assertThat(result).isEqualTo(failure);
    }
}
