
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Success#mapSuccess(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Success mapSuccess")
class Success_mapSuccess_Test {

    @Test
    void should_use_success_mapping() {
        // Given
        final Result<Integer, String> success = new Success<>(123);
        final UnaryOperator<Integer> mapper = s -> s + 198;
        // When
        final Result<Integer, String> result = success.mapSuccess(mapper);
        // Then
        assertThat(result).isEqualTo(new Success<>(321));
    }

    @Test
    void should_throw_exception_when_mapper_returns_null() {
        // Given
        final Result<Integer, Integer> success = new Success<>(123);
        final Function<Integer, String> mapper = s -> null;
        // When
        final ThrowingCallable callable = () -> success.mapSuccess(mapper);
        // Then
        assertThatThrownBy(callable).isInstanceOf(NullPointerException.class);
    }
}
