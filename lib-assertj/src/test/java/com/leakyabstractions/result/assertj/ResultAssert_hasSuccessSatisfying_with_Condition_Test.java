
package com.leakyabstractions.result.assertj;

import static com.leakyabstractions.result.DefaultResult.failure;
import static com.leakyabstractions.result.DefaultResult.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.assertj.ResultShouldBe.shouldBeSuccess;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBe.shouldBe;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.Condition;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;
import com.leakyabstractions.result.assertj.AssertionsUtil.TestCondition;

/**
 * Tests for {@link ResultAssert#hasSuccessSatisfying(Condition)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("ResultAssert hasSuccessSatisfying(Condition)")
class ResultAssert_hasSuccessSatisfying_with_Condition_Test {

    private Condition<String> passingCondition = new TestCondition<>(true);
    private Condition<String> notPassingCondition = new TestCondition<>(false);

    @Test
    void should_fail_when_result_is_null() {
        // Given
        final Result<String, Integer> result = null;
        final Condition<String> condition = passingCondition;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessSatisfying(condition);
        // Then
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(callable)
                .withMessage(actualIsNull());
    }

    @Test
    void should_fail_when_result_is_failure() {
        // Given
        final Result<String, Integer> result = failure(123);
        final Condition<String> condition = passingCondition;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessSatisfying(condition);
        // Then
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(callable)
                .withMessage(shouldBeSuccess(result).create());
    }

    @Test
    void should_fail_when_condition_is_null() {
        // Given
        final Result<String, Integer> result = success("something");
        final Condition<String> condition = null;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessSatisfying(condition);
        // Then
        assertThatNullPointerException()
                .isThrownBy(callable)
                .withMessage("The condition to evaluate should not be null");
    }

    @Test
    void should_pass_when_condition_is_met() {
        // Given
        final Result<String, Integer> result = success("something");
        final Condition<String> condition = passingCondition;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessSatisfying(condition);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    void should_pass_when_condition_is_met_even_if_result_is_empty() {
        // Given
        final Result<String, Integer> result = success(null);
        final Condition<String> condition = passingCondition;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessSatisfying(condition);
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    void should_fail_when_condition_is_not_met() {
        // Given
        final Result<String, Integer> result = success("something");
        final Condition<String> condition = notPassingCondition;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessSatisfying(condition);
        // Then
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(callable)
                .withMessage(shouldBe("something", notPassingCondition).create());
    }
}
