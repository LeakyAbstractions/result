
package com.leakyabstractions.result.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Success#mapFailure(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success mapFailure")
class Success_mapFailure_Test {

    @Test
    void should_ignore_failure_mapping() {
        // Given
        final Result<String, Integer> success = new Success<>("SUCCESS");
        final Function<Integer, String> mapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = success.mapFailure(mapper);
        // Then
        assertThat(result).isSameAs(success);
    }
}
