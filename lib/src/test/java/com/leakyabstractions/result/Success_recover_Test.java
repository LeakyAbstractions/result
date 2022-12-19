
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#recover(Predicate, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success recover")
class Success_recover_Test {

    @Test
    void should_return_itself_no_matter_what() {
        // Given
        final Result<String, Integer> failure = new Success<>("SUCCESS");
        final Predicate<Integer> isRecoverable = s -> fail("Should not happen");
        final Function<Integer, String> mapper = s -> fail("Should not happen");
        // When
        final Result<String, Integer> result = failure.recover(isRecoverable, mapper);
        // Then
        assertThat(result).isSameAs(failure);
    }
}
