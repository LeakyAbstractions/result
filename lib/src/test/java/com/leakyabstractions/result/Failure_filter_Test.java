
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link Failure#filter(Predicate, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure filter")
class Failure_filter_Test {

    @Test
    void should_return_itself_no_matter_what() {
        // Given
        final Result<Integer, String> failure = new Failure<>("FAILURE");
        final Predicate<Integer> isAcceptable = s -> fail("Should not happen");
        final Function<Integer, String> mapper = s -> fail("Should not happen");
        // When
        final Result<Integer, String> result = failure.filter(isAcceptable, mapper);
        // Then
        assertThat(result).isSameAs(failure);
    }
}
