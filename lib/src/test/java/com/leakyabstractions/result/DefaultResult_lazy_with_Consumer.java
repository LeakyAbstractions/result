
package com.leakyabstractions.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.function.Consumer;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultResult#lazy(Consumer)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("DefaultResult lazy with consumer")
class DefaultResult_lazy_with_Consumer {

    @Test
    void should_throw_exception_when_null_consumer() {
        // Given
        final Consumer<String> consumer = null;
        // When
        ThrowingCallable callable = () -> DefaultResult.lazy(consumer);
        // Then
        assertThatThrownBy(callable).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_pass_when_non_null() {
        // Given
        final Consumer<String> consumer = s -> {};
        // When
        LazyConsumer<String> result = DefaultResult.lazy(consumer);
        // Then
        assertThat(result).isInstanceOf(LazyConsumer.class);
    }
}
