
package com.leakyabstractions.result.assertj;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.EXTRACTION;

import java.util.Comparator;
import java.util.UUID;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.ThrowableAssertAlternative;
import org.junit.jupiter.api.Test;

class AssertionsUtil {

    static AssertionError expectAssertionError(ThrowingCallable shouldRaiseAssertionError) {
        AssertionError error = catchThrowableOfType(shouldRaiseAssertionError, AssertionError.class);
        assertThat(error).as("The code under test should have raised an AssertionError").isNotNull();
        return error;
    }

    static ThrowableAssertAlternative<AssertionError> assertThatAssertionErrorIsThrownBy(
            ThrowingCallable shouldRaiseAssertionError) {
        return assertThatExceptionOfType(AssertionError.class).isThrownBy(shouldRaiseAssertionError);
    }

    static class TestCondition<T> extends Condition<T> {

        private final boolean matches;

        TestCondition(boolean matches) {
            this.matches = matches;
        }

        @Override
        public boolean matches(T value) {
            return matches;
        }
    }

    static class AlwaysEqualComparator<T> implements Comparator<T> {

        static final AlwaysEqualComparator<Object> INSTANCE = new AlwaysEqualComparator<>();

        @Override
        public int compare(T o1, T o2) {
            return 0;
        }

        @Override
        public String toString() {
            return "AlwaysEqualComparator";
        }
    }

    static interface NavigationMethodBaseTest<ASSERT extends AbstractAssert<ASSERT, ?>> {

        ASSERT getAssertion();

        AbstractAssert<?, ?> invoke_navigation_method(ASSERT assertion);

        @Test
        default void should_honor_registered_comparator() {
            // Given
            ASSERT assertion = getAssertion().usingComparator(AlwaysEqualComparator.INSTANCE);
            // When
            AbstractAssert<?, ?> result = invoke_navigation_method(assertion);
            // Then
            result.isEqualTo(UUID.randomUUID()); // random value to avoid false positives
        }

        @Test
        default void should_keep_existing_assertion_state() {
            // Given
            ASSERT assertion = getAssertion().as("description")
                    .withFailMessage("error message")
                    .withRepresentation(UNICODE_REPRESENTATION)
                    .usingComparator(AlwaysEqualComparator.INSTANCE);
            // When
            AbstractAssert<?, ?> result = invoke_navigation_method(assertion);
            // Then
            then(result).hasFieldOrPropertyWithValue("objects", EXTRACTION.getValueOf("objects", assertion))
                    .extracting(AbstractAssert::getWritableAssertionInfo)
                    .usingRecursiveComparison()
                    .isEqualTo(assertion.info);
        }
    }
}
