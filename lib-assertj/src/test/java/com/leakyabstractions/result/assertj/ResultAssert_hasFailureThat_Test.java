
package com.leakyabstractions.result.assertj;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.AssertionsUtil.expectAssertionError;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.assertj.ResultShouldBe.shouldBeFailure;
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
 * Tests for {@link ResultAssert#hasFailureThat()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("ResultAssert hasFailureThat")
class ResultAssert_hasFailureThat_Test implements NavigationMethodBaseTest<ResultAssert<Integer, String>> {

    @Test
    void should_fail_if_result_is_null() {
        // Given
        final Result<Integer, String> result = null;
        // When
        final AssertionError assertionError = expectAssertionError(() -> assertThat(result).hasFailureThat());
        // Then
        then(assertionError).hasMessage(actualIsNull());
    }

    @Test
    void should_fail_if_result_is_success() {
        // Given
        final Result<Integer, String> result = success(123);
        // When
        final AssertionError assertionError = expectAssertionError(() -> assertThat(result).hasFailureThat());
        // Then
        then(assertionError).hasMessage(shouldBeFailure(result).create());
    }

    @Test
    void should_pass_if_result_contains_a_value() {
        // Given
        final Result<Integer, String> result = failure("Frodo");
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureThat().isEqualTo("Frodo");
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Override
    public ResultAssert<Integer, String> getAssertion() {
        final Result<Integer, String> result = failure("Frodo");
        return assertThat(result);
    }

    @Override
    public AbstractAssert<?, ?> invoke_navigation_method(ResultAssert<Integer, String> assertion) {
        return assertion.hasFailureThat();
    }

}
