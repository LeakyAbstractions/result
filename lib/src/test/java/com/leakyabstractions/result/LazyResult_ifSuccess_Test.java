
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#ifSuccess(Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult ifSuccess")
class LazyResult_ifSuccess_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_be_lazy() {
        final Supplier<Result<String, String>> supplier = () -> fail("Should not happen");
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final LazyConsumer<String> successAction = s -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifSuccess(successAction);
        // Then
        assertThat(result)
                .isInstanceOf(LazyResult.class)
                .isNotSameAs(lazy);
    }

    @Test
    void should_not_be_lazy_and_ignore_success_action() {
        // Given
        final Result<String, String> failure = new Failure<>("FAILURE");
        final Supplier<Result<String, String>> supplier = () -> failure;
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final Consumer<String> successAction = s -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifSuccess(successAction);
        // Then
        assertThat(result).isSameAs(failure);
    }

    @Test
    void should_not_be_lazy_and_perform_success_action() {
        final Result<String, String> success = new Success<>(SUCCESS);
        final Supplier<Result<String, String>> supplier = () -> success;
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final Consumer<String> successAction = s -> actionPerformed.set(true);
        // When
        final Result<String, String> result = lazy.ifSuccess(successAction);
        // Then
        assertThat(result).isSameAs(success);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_eventually_perform_action() {
        final Supplier<Result<String, String>> supplier = () -> new Success<>(SUCCESS);
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> successAction = s -> actionPerformed.set(true);
        // When
        final Result<String, String> result = lazy.ifSuccess(successAction);
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
        final Result<String, String> success = new Success<>(SUCCESS);
        final Supplier<Result<String, String>> supplier = () -> success;
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> successAction = s -> actionPerformed.set(true);
        // When
        final String value = lazy.orElseThrow();
        final Result<String, String> result = lazy.ifSuccess(successAction);
        // Then
        assertThat(value).isSameAs(SUCCESS);
        assertThat(result).isSameAs(success);
        assertThat(actionPerformed).isTrue();
    }
}
