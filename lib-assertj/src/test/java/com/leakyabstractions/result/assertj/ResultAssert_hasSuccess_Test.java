
package com.leakyabstractions.result.assertj;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.assertj.ResultShouldBe.shouldBeSuccess;
import static com.leakyabstractions.result.assertj.ResultShouldHave.shouldHave;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link ResultAssert#hasSuccess(Object)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("ResultAssert hasSuccess")
class ResultAssert_hasSuccess_Test {

    @Test
    void should_fail_when_result_is_null() {
        // Given
        final String expected = "something";
        final Result<String, Integer> result = null;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccess(expected);
        // Then
        assertThatAssertionErrorIsThrownBy(callable).withMessage(actualIsNull());
    }

    @Test
    void should_fail_if_expected_value_is_null() {
        // Given
        final String expected = null;
        final String actual = "something";
        final Result<String, Integer> result = success(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccess(expected);
        // Then
        assertThatIllegalArgumentException().isThrownBy(callable)
                .withMessage("The expected value should not be <null>.");
    }

    @Test
    void should_pass_if_result_has_expected_value() {
        // Given
        final String expected = "something";
        final String actual = expected;
        final Result<String, Integer> result = success(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccess(expected);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    void should_fail_if_result_does_not_contain_expected_value() {
        // Given
        final String expected = "something";
        final String actual = "not-expected";
        final Result<String, Integer> result = success(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccess(expected);
        // Then
        final AssertionFailedError error = catchThrowableOfType(callable, AssertionFailedError.class);
        assertThat(error).hasMessage(shouldHave(result, expected, actual).create());
        assertThat(error.getActual().getStringRepresentation()).isEqualTo(actual);
        assertThat(error.getExpected().getStringRepresentation()).isEqualTo(expected);
    }

    @Test
    void should_fail_if_result_is_failure() {
        // Given
        final String expected = "something";
        final Result<String, Integer> result = failure(123);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccess(expected);
        // Then
        final Throwable error = catchThrowable(callable);
        assertThat(error).hasMessage(shouldBeSuccess(result).create());
    }
}
