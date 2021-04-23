
package com.leakyabstractions.result.assertj;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.assertj.ResultShouldBe.shouldBeSuccess;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.function.Consumer;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link ResultAssert#hasSuccessSatisfying(Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("ResultAssert hasSuccessSatisfying(Consumer)")
class ResultAssert_hasSuccessSatisfying_with_Consumer_Test {

    @Test
    void should_fail_when_result_is_null() {
        // Given
        final Result<String, Integer> result = null;
        final Consumer<String> consumer = s -> {};
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessSatisfying(consumer);
        // Then
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(callable)
                .withMessage(actualIsNull());
    }

    @Test
    void should_fail_when_result_is_failure() {
        // Given
        final Result<String, Integer> result = failure(123);
        final Consumer<String> consumer = s -> {};
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessSatisfying(consumer);
        // Then
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(callable)
                .withMessage(shouldBeSuccess(result).create());
    }

    @Test
    void should_pass_when_consumer_passes() {
        // Given
        final Result<String, Integer> result = success("something");
        final Consumer<String> consumer = s -> assertThat(s).isEqualTo("something").startsWith("so").endsWith("ng");
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessSatisfying(consumer);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    void should_fail_from_consumer() {
        // Given
        final Result<String, Integer> actual = success("something else");
        final Consumer<String> consumer = s -> assertThat(s).isEqualTo("something");
        // When
        final ThrowingCallable callable = () -> assertThat(actual).hasSuccessSatisfying(consumer);
        // Then
        assertThatExceptionOfType(AssertionError.class).isThrownBy(callable)
                .withMessage(format("%nexpected: \"something\"%nbut was : \"something else\""));
    }
}
