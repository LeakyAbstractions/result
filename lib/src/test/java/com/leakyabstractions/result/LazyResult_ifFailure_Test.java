
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#ifFailure(Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult ifFailure")
class LazyResult_ifFailure_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        final Supplier<Result<String, String>> supplier = () -> fail("Should not happen");
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final LazyConsumer<String> failureAction = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifFailure(failureAction);
        // Then
        assertThat(result)
                .isInstanceOf(LazyResult.class)
                .isNotSameAs(lazy);
    }

    @Test
    void should_not_be_lazy_and_ignore_failure_action() {
        // Given
        final Result<String, String> success = new DefaultSuccess<>("SUCCESS");
        final Supplier<Result<String, String>> supplier = () -> success;
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final Consumer<String> failureAction = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifFailure(failureAction);
        // Then
        assertThat(result).isSameAs(success);
    }

    @Test
    void should_not_be_lazy_and_perform_failure_action() {
        final Result<String, String> failure = new DefaultFailure<>(FAILURE);
        final Supplier<Result<String, String>> supplier = () -> failure;
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final Consumer<String> failureAction = f -> actionPerformed.set(true);
        // When
        final Result<String, String> result = lazy.ifFailure(failureAction);
        // Then
        assertThat(result).isSameAs(failure);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_eventually_perform_action() {
        final Supplier<Result<String, String>> supplier = () -> new DefaultFailure<>(FAILURE);
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> failureAction = f -> actionPerformed.set(true);
        // When
        final Result<?, String> result = lazy.ifFailure(failureAction);
        final String value = result.getFailureOrElseThrow();
        // Then
        assertThat(value).isSameAs(FAILURE);
        assertThat(result)
                .isInstanceOf(LazyResult.class)
                .isNotSameAs(lazy);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_perform_action_if_already_supplied() {
        final Result<String, String> failure = new DefaultFailure<>(FAILURE);
        final Supplier<Result<String, String>> supplier = () -> failure;
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> failureAction = f -> actionPerformed.set(true);
        // When
        final String value = lazy.getFailureOrElseThrow();
        final Result<?, String> result = lazy.ifFailure(failureAction);
        // Then
        assertThat(value).isSameAs(FAILURE);
        assertThat(result).isSameAs(failure);
        assertThat(actionPerformed).isTrue();
    }
}
