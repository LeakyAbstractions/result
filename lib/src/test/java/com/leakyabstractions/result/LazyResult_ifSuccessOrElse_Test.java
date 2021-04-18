
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#ifSuccessOrElse(Consumer, Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult ifSuccessOrElse")
class LazyResult_ifSuccessOrElse_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        final Supplier<Result<String, String>> supplier = () -> fail("Should not happen");
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final LazyConsumer<String> successAction = s -> fail("Should not happen");
        final LazyConsumer<String> failureAction = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(result)
                .isInstanceOf(LazyResult.class)
                .isNotSameAs(lazy);
    }

    @Test
    void should_not_be_lazy_and_perform_success_action_only() {
        final Result<String, String> supplied = new Success<>(SUCCESS);
        final Result<String, String> lazy = new LazyResult<>(() -> supplied);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final Consumer<String> successAction = s -> actionPerformed.set(true);
        final LazyConsumer<String> failureAction = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(result).isSameAs(supplied);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_not_be_lazy_and_perform_failure_action_only() {
        final Result<String, String> failure = new Failure<>(FAILURE);
        final Supplier<Result<String, String>> supplier = () -> failure;
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> successAction = s -> fail("Should not happen");
        final Consumer<String> failureAction = f -> actionPerformed.set(true);
        // When
        final Result<String, String> result = lazy.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(result).isSameAs(failure);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_eventually_perform_action() {
        final Supplier<Result<String, String>> supplier = () -> new Success<>(SUCCESS);
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> successAction = s -> actionPerformed.set(true);
        final LazyConsumer<String> failureAction = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifSuccessOrElse(successAction, failureAction);
        final String value = result.orElseThrow();
        // Then
        assertThat(value).isSameAs(SUCCESS);
        assertThat(result)
                .isInstanceOf(LazyResult.class)
                .isNotSameAs(lazy);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_perform_action_if_already_supplied() {
        final Result<String, String> failure = new Failure<>(FAILURE);
        final Supplier<Result<String, String>> supplier = () -> failure;
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> successAction = s -> fail("Should not happen");
        final LazyConsumer<String> failureAction = f -> actionPerformed.set(true);
        // When
        final String value = lazy.getFailureOrElseThrow();
        final Result<String, String> result = lazy.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(value).isSameAs(FAILURE);
        assertThat(result).isSameAs(failure);
        assertThat(actionPerformed).isTrue();
    }
}
