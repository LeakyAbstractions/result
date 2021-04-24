
package com.leakyabstractions.result.assertj;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.AssertionsUtil.expectAssertionError;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.assertj.ResultShouldBe.shouldBeFailure;
import static com.leakyabstractions.result.assertj.ResultShouldHave.shouldHaveInstanceOf;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link ResultAssert#hasFailureInstanceOf(Class)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("ResultAssert hasFailureInstanceOf")
class ResultAssert_hasFailureInstanceOf_Test {

    @Test
    void should_fail_if_expected_class_is_null() {
        // Given
        final Class<?> expectedClass = null;
        final String actual = "something";
        final Result<Integer, String> result = failure(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureInstanceOf(expectedClass);
        // Then
        assertThatIllegalArgumentException().isThrownBy(callable)
                .withMessage("The expected value should not be <null>.");
    }

    @Test
    void should_fail_if_result_is_success() {
        // Given
        final Class<?> expectedClass = Object.class;
        final Result<Object, Integer> result = success(123);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureInstanceOf(expectedClass);
        // Then
        final AssertionError assertionError = expectAssertionError(callable);
        then(assertionError).hasMessage(shouldBeFailure(result).create());
    }

    @Test
    void should_pass_if_result_contains_required_type() {
        // Given
        final Class<?> expectedClass = String.class;
        final Object actual = "something";
        final Result<Integer, Object> result = failure(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureInstanceOf(expectedClass);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    void should_pass_if_result_contains_required_type_subclass() {
        // Given
        final Class<?> expectedClass = ParentClass.class;
        final Object actual = new SubClass();
        final Result<Integer, Object> result = failure(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureInstanceOf(expectedClass);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    void should_fail_if_result_contains_other_type_than_required() {
        // Given
        final Class<?> expectedClass = OtherClass.class;
        final ParentClass actual = new ParentClass();
        final Result<Integer, ParentClass> result = failure(actual);
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureInstanceOf(expectedClass);
        // Then
        final AssertionError assertionError = expectAssertionError(callable);
        then(assertionError).hasMessage(shouldHaveInstanceOf(result, expectedClass, actual).create());
    }

    private static class ParentClass {}

    private static class SubClass extends ParentClass {}

    private static class OtherClass {}
}
