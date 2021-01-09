
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyResult#filter(Predicate, Function)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult filter")
class LazyResult_filter_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> fail("Should not happen");
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        final Predicate<String> filter = s -> fail("Should not happen");
        final Function<String, Integer> mapper = s -> fail("Should not happen");
        // When
        final Result<String, Integer> result = lazy.filter(filter, mapper);
        // Then
        assertThat(result)
                .isInstanceOf(LazyResult.class)
                .isNotSameAs(lazy);
    }

    @Test
    void should_eventually_evaluate_predicate_when_needed() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> new Success<>(SUCCESS);
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        final AtomicBoolean filterEvaluated = new AtomicBoolean(false);
        final Predicate<String> filter = s -> {
            filterEvaluated.set(true);
            return true;
        };
        final Function<String, Integer> mapper = s -> fail("Should not happen");
        // When
        final Result<String, Integer> result = lazy.filter(filter, mapper);
        final String supplied = result.orElseThrow();
        // Then
        assertThat(supplied).isSameAs(SUCCESS);
        assertThat(filterEvaluated).isTrue();
    }

    @Test
    void should_not_be_lazy_when_already_supplied() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> new Success<>(SUCCESS);
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final Predicate<String> filter = s -> false;
        final Function<String, String> mapper = s -> FAILURE;
        // When
        final String supplied = lazy.orElseThrow();
        final Result<String, String> result = lazy.filter(filter, mapper);
        // Then
        assertThat(supplied).isSameAs(SUCCESS);
        assertThat(result).isEqualTo(new Failure<>(FAILURE));
    }
}
