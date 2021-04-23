
package com.leakyabstractions.result.assertj;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.AssertionsUtil.expectAssertionError;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.assertj.ResultShouldBe.shouldBeSuccess;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;
import com.leakyabstractions.result.assertj.AssertionsUtil.NavigationMethodBaseTest;

/**
 * Tests for {@link ResultAssert#hasSuccessThat()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("ResultAssert hasSuccessThat")
class ResultAssert_hasSuccessThat_Test implements NavigationMethodBaseTest<ResultAssert<String, Integer>> {

    @Test
    void should_fail_if_result_is_null() {
        // Given
        final Result<String, Integer> result = null;
        // When
        final AssertionError assertionError = expectAssertionError(() -> assertThat(result).hasSuccessThat());
        // Then
        then(assertionError).hasMessage(actualIsNull());
    }

    @Test
    void should_fail_if_result_is_failure() {
        // Given
        final Result<String, Integer> result = failure(123);
        // When
        final AssertionError assertionError = expectAssertionError(() -> assertThat(result).hasSuccessThat());
        // Then
        then(assertionError).hasMessage(shouldBeSuccess(result).create());
    }

    @Test
    void should_pass_if_result_contains_a_value() {
        // Given
        final Result<String, Integer> result = success("Frodo");
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessThat().isEqualTo("Frodo");
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Override
    public ResultAssert<String, Integer> getAssertion() {
        final Result<String, Integer> result = success("Frodo");
        return assertThat(result);
    }

    @Override
    public AbstractAssert<?, ?> invoke_navigation_method(ResultAssert<String, Integer> assertion) {
        return assertion.hasSuccessThat();
    }

}
