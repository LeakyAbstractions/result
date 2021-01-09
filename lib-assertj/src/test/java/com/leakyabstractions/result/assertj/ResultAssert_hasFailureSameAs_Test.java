
package com.leakyabstractions.result.assertj;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.assertj.ResultShouldBe.shouldBeFailure;
import static com.leakyabstractions.result.assertj.ResultShouldHave.shouldHaveSame;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link ResultAssert#hasFailureSameAs(Object)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("ResultAssert hasFailureSameAs")
class ResultAssert_hasFailureSameAs_Test {

    @Test
    void should_fail_when_result_is_null() {
        // Given
        final String expected = "something";
        final Result<Integer, String> result = null;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureSameAs(expected);
        // Then
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(callable)
                .withMessage(actualIsNull());
    }

    @Test
    void should_fail_if_expected_value_is_null() {
        // Given
        final String expected = null;
        final String actual = "something";
        final Result<Integer, String> result = failure(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureSameAs(expected);
        // Then
        assertThatIllegalArgumentException().isThrownBy(callable)
                .withMessage("The expected value should not be <null>.");
    }

    @Test
    void should_fail_if_expected_value_is_null_even_if_result_is_empty() {
        // Given
        final String expected = null;
        final String actual = expected;
        final Result<Integer, String> result = failure(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureSameAs(expected);
        // Then
        assertThatIllegalArgumentException().isThrownBy(callable)
                .withMessage("The expected value should not be <null>.");
    }

    @Test
    void should_pass_if_result_contains_the_expected_object_reference() {
        // Given
        final String expected = "something";
        final String actual = expected;
        final Result<Integer, String> result = failure(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureSameAs(expected);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    void should_fail_if_result_does_not_contain_the_expected_object_reference() {
        // Given
        final String expected = "something";
        final String actual = "not-expected";
        final Result<Integer, String> result = failure(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureSameAs(expected);
        // Then
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(callable)
                .withMessage(shouldHaveSame(result, expected, actual).create());
    }

    @Test
    void should_fail_if_result_contains_equal_but_not_same_value() {
        // Given
        final String expected = "something";
        final String actual = new String("something");
        final Result<Integer, String> result = failure(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureSameAs(expected);
        // Then
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(callable)
                .withMessage(shouldHaveSame(result, expected, actual).create());
    }

    @Test
    void should_fail_if_result_is_success() {
        // Given
        final String expected = "something";
        final Result<Integer, String> result = success(123);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureSameAs(expected);
        // Then
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(callable)
                .withMessage(shouldBeFailure(result).create());
    }

    @Test
    void should_fail_if_result_is_empty() {
        // Given
        final String expected = "something";
        final String actual = null;
        final Result<Integer, String> result = failure(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureSameAs(expected);
        // Then
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(callable)
                .withMessage(shouldHaveSame(result, expected, actual).create());
    }
}
