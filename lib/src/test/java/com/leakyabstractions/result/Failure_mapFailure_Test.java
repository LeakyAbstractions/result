
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Failure#mapFailure(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Failure mapFailure")
class Failure_mapFailure_Test {

    @Test
    void should_use_failure_mapping() {
        // Given
        final Result<Integer, Integer> failure = new Failure<>(123);
        final UnaryOperator<Integer> mapper = f -> f + 198;
        // When
        final Result<Integer, Integer> result = failure.mapFailure(mapper);
        // Then
        assertThat(result).isEqualTo(new Failure<>(321));
    }

    @Test
    void should_throw_exception_when_mapper_returns_null() {
        // Given
        final Result<Integer, Integer> failure = new Failure<>(123);
        final Function<Integer, String> mapper = f -> null;
        // When
        final ThrowingCallable callable = () -> failure.mapFailure(mapper);
        // Then
        assertThatThrownBy(callable).isInstanceOf(NullPointerException.class);
    }
}
