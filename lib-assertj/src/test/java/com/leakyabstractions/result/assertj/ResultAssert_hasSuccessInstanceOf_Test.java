
package com.leakyabstractions.result.assertj;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.AssertionsUtil.expectAssertionError;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.assertj.ResultShouldBe.shouldBeSuccess;
import static com.leakyabstractions.result.assertj.ResultShouldHave.shouldHaveInstanceOf;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link ResultAssert#hasSuccessInstanceOf(Class)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("ResultAssert hasSuccessInstanceOf")
class ResultAssert_hasSuccessInstanceOf_Test {

    @Test
    void should_fail_if_expected_class_is_null() {
        // Given
        final Class<?> expectedClass = null;
        final String actual = "something";
        final Result<String, Integer> result = success(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessInstanceOf(expectedClass);
        // Then
        assertThatIllegalArgumentException().isThrownBy(callable)
                .withMessage("The expected value should not be <null>.");
    }

    @Test
    void should_fail_if_result_is_failure() {
        // Given
        final Class<?> expectedClass = Object.class;
        final Result<Object, Integer> result = failure(123);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessInstanceOf(expectedClass);
        // Then
        final AssertionError assertionError = expectAssertionError(callable);
        then(assertionError).hasMessage(shouldBeSuccess(result).create());
    }

    @Test
    void should_fail_if_result_is_empty() {
        // Given
        final Class<?> expectedClass = Object.class;
        final Object actual = null;
        final Result<Object, Integer> result = success(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessInstanceOf(expectedClass);
        // Then
        final AssertionError assertionError = expectAssertionError(callable);
        then(assertionError).hasMessage(shouldHaveInstanceOf(result, expectedClass, actual).create());
    }

    @Test
    void should_pass_if_result_contains_required_type() {
        // Given
        final Class<?> expectedClass = String.class;
        final Object actual = "something";
        final Result<Object, Integer> result = success(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessInstanceOf(expectedClass);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    void should_pass_if_result_contains_required_type_subclass() {
        // Given
        final Class<?> expectedClass = ParentClass.class;
        final Object actual = new SubClass();
        final Result<Object, Integer> result = success(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessInstanceOf(expectedClass);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    void should_fail_if_result_contains_other_type_than_required() {
        // Given
        final Class<?> expectedClass = OtherClass.class;
        final ParentClass actual = new ParentClass();
        final Result<ParentClass, Integer> result = success(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessInstanceOf(expectedClass);
        // Then
        final AssertionError assertionError = expectAssertionError(callable);
        then(assertionError).hasMessage(shouldHaveInstanceOf(result, expectedClass, actual).create());
    }

    private static class ParentClass {}

    private static class SubClass extends ParentClass {}

    private static class OtherClass {}
}
