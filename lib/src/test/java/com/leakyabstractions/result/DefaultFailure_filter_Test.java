
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultFailure#filter(Predicate, Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultFailure filter")
class DefaultFailure_filter_Test {

    @Test
    void should_return_itself_no_matter_what() {
        // Given
        final Result<Integer, String> failure = new DefaultFailure<>("FAILURE");
        final Predicate<Integer> filter = s -> fail("Should not happen");
        final Function<Integer, String> failureMapper = s -> fail("Should not happen");
        // When
        final Result<Integer, String> result = failure.filter(filter, failureMapper);
        // Then
        assertThat(result).isSameAs(failure);
    }
}
