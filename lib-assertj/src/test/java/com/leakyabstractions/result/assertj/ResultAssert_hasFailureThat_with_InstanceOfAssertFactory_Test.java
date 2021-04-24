
package com.leakyabstractions.result.assertj;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.AssertionsUtil.expectAssertionError;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.assertj.ResultShouldBe.shouldBeFailure;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;
import com.leakyabstractions.result.assertj.AssertionsUtil.NavigationMethodBaseTest;

/**
 * Tests for {@link ResultAssert#hasFailureThat(InstanceOfAssertFactory)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("ResultAssert hasFailureThat(InstanceOfAssertFactory)")
class ResultAssert_hasFailureThat_with_InstanceOfAssertFactory_Test
        implements NavigationMethodBaseTest<ResultAssert<Integer, String>> {

    @Test
    void should_fail_if_result_is_null() {
        // Given
        final Result<Integer, String> result = null;
        final InstanceOfAssertFactory<String, AbstractStringAssert<?>> factory = STRING;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureThat(factory);
        // Then
        final AssertionError assertionError = expectAssertionError(callable);
        then(assertionError).hasMessage(actualIsNull());
    }

    @Test
    void should_fail_if_result_is_success() {
        // Given
        final Result<Integer, String> result = success(123);
        final InstanceOfAssertFactory<String, AbstractStringAssert<?>> factory = STRING;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureThat(factory);
        // Then
        final AssertionError assertionError = expectAssertionError(callable);
        then(assertionError).hasMessage(shouldBeFailure(result).create());
    }

    @Test
    void should_fail_throwing_npe_if_assert_factory_is_null() {
        // Given
        final Result<Integer, String> result = failure("Frodo");
        final InstanceOfAssertFactory<String, AbstractStringAssert<?>> factory = null;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureThat(factory);
        // Then
        final Throwable thrown = catchThrowable(callable);
        then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("instanceOfAssertFactory").create());
    }

    @Test
    void should_pass_allowing_type_narrowed_assertions_if_result_contains_an_instance_of_the_factory_type() {
        // Given
        final Result<Integer, String> result = failure("Frodo");
        final InstanceOfAssertFactory<String, AbstractStringAssert<?>> factory = STRING;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureThat(factory).startsWith("Frodo");
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    void should_fail_if_result_does_not_contain_an_instance_of_the_factory_type() {
        // Given
        final Result<Integer, String> result = failure("Frodo");
        final InstanceOfAssertFactory<Integer, AbstractIntegerAssert<?>> factory = INTEGER;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasFailureThat(factory);
        // Then
        final AssertionError assertionError = expectAssertionError(callable);
        then(assertionError).hasMessageContainingAll("Expecting:", "to be an instance of:", "but was instance of:");
    }

    @Override
    public ResultAssert<Integer, String> getAssertion() {
        final Result<Integer, String> result = failure("Frodo");
        return assertThat(result);
    }

    @Override
    public AbstractAssert<?, ?> invoke_navigation_method(ResultAssert<Integer, String> assertion) {
        return assertion.hasFailureThat(STRING);
    }

}
