
package org.assertj.core.api;

/**
 * Proxy to access package-protected methods in {@code org.assertj.core.api}.
 *
 * @author Guillermo Calvo
 */
public interface ObjectAssertProxy {

    /**
     * Assert with assertion state.
     * <p>
     * Invokes {@link Assertions#assertThatObject(Object)} and then
     * {@link ObjectAssert#withAssertionState(AbstractAssert)}.
     *
     * @param <T> the generic type of the assert.
     * @param state the assertion state.
     * @param actual the actual value.
     * @return the created assertion object with the assertion state.
     */
    static <T> ObjectAssert<T> assertWithAssertionState(AbstractAssert<?, ?> state, T actual) {
        return Assertions.assertThatObject(actual).withAssertionState(state);
    }
}
