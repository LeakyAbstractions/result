
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.function.Consumer;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Results#lazy(Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("Results lazy with consumer")
class Results_lazy_with_Consumer_Test {

    @Test
    void should_throw_exception_when_null_consumer() {
        // Given
        final Consumer<String> consumer = null;
        // When
        ThrowingCallable callable = () -> Results.lazy(consumer);
        // Then
        assertThatThrownBy(callable).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_pass_when_non_null() {
        // Given
        final Consumer<String> consumer = s -> {};
        // When
        Consumer<String> result = Results.lazy(consumer);
        // Then
        assertThat(result).isInstanceOf(LazyConsumer.class);
    }
}
