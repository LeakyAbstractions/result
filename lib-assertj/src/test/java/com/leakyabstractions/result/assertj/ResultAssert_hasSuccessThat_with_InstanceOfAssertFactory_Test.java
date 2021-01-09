
package com.leakyabstractions.result.assertj;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.AssertionsUtil.expectAssertionError;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.assertj.ResultShouldBe.shouldBeSuccess;
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
 * Tests for {@link ResultAssert#hasSuccessThat(InstanceOfAssertFactory)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("ResultAssert hasSuccessThat(InstanceOfAssertFactory)")
class ResultAssert_hasSuccessThat_with_InstanceOfAssertFactory_Test
        implements NavigationMethodBaseTest<ResultAssert<String, Integer>> {

    @Test
    void should_fail_if_result_is_null() {
        // Given
        final Result<String, Integer> result = null;
        final InstanceOfAssertFactory<String, AbstractStringAssert<?>> factory = STRING;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessThat(factory);
        // Then
        final AssertionError assertionError = expectAssertionError(callable);
        then(assertionError).hasMessage(actualIsNull());
    }

    @Test
    void should_fail_if_result_is_failure() {
        // Given
        final Result<String, Integer> result = failure(123);
        final InstanceOfAssertFactory<String, AbstractStringAssert<?>> factory = STRING;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessThat(factory);
        // Then
        final AssertionError assertionError = expectAssertionError(callable);
        then(assertionError).hasMessage(shouldBeSuccess(result).create());
    }

    @Test
    void should_fail_if_result_is_empty() {
        // Given
        final Result<String, Integer> result = success(null);
        final InstanceOfAssertFactory<String, AbstractStringAssert<?>> factory = STRING;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessThat(factory);
        final AssertionError assertionError = expectAssertionError(callable);
        // Then
        then(assertionError).hasMessage(shouldNotBeNull("success value").create());
    }

    @Test
    void should_fail_throwing_npe_if_assert_factory_is_null() {
        // Given
        final Result<String, Integer> result = success("Frodo");
        final InstanceOfAssertFactory<String, AbstractStringAssert<?>> factory = null;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessThat(factory);
        // Then
        final Throwable thrown = catchThrowable(callable);
        then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("instanceOfAssertFactory").create());
    }

    @Test
    void should_pass_allowing_type_narrowed_assertions_if_result_contains_an_instance_of_the_factory_type() {
        // Given
        final Result<String, Integer> result = success("Frodo");
        final InstanceOfAssertFactory<String, AbstractStringAssert<?>> factory = STRING;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessThat(factory).startsWith("Frodo");
        // Then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    void should_fail_if_result_does_not_contain_an_instance_of_the_factory_type() {
        // Given
        final Result<String, Integer> result = success("Frodo");
        final InstanceOfAssertFactory<Integer, AbstractIntegerAssert<?>> factory = INTEGER;
        // When
        final ThrowingCallable callable = () -> assertThat(result).hasSuccessThat(factory);
        // Then
        final AssertionError assertionError = expectAssertionError(callable);
        then(assertionError).hasMessageContainingAll("Expecting:", "to be an instance of:", "but was instance of:");
    }

    @Override
    public ResultAssert<String, Integer> getAssertion() {
        final Result<String, Integer> result = success("Frodo");
        return assertThat(result);
    }

    @Override
    public AbstractAssert<?, ?> invoke_navigation_method(ResultAssert<String, Integer> assertion) {
        return assertion.hasSuccessThat(STRING);
    }

}
