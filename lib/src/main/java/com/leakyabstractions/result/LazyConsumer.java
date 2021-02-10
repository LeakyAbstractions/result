
package com.leakyabstractions.result;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents an action to be <em>lazily</em> performed with the success/failure value of a result.
 * <p>
 * Unlike regular ones, lazy consumers won't force a {@link Results#lazy(java.util.function.Supplier) lazy result} to
 * retrieve the backing result from its supplier. In other words, they may be ignored if the lazy result ends up not
 * invoking its result supplier.
 * <p>
 * Instances of objects implementing this interface are intended to be passed as parameters to
 * {@link Result#ifSuccess(Consumer)}, {@link Result#ifSuccessOrElse(Consumer, Consumer)} and
 * {@link Result#ifFailure(Consumer)} when the action only needs to be performed if the lazy result eventually retrieves
 * an actual result from its supplier.
 * <p>
 * If a lazy consumer is passed to a non-lazy result, or to a lazy result that has already retrieved the backing result
 * from its supplier, then the action will be executed immediately.
 * <p>
 * This is a functional interface whose functional method is {@link #accept(Object)}.
 *
 * @author Guillermo Calvo
 * @param <T> the type of the input to the action
 * @see Results#lazy(Consumer)
 */
@FunctionalInterface
interface LazyConsumer<T> extends Consumer<T> {

    /**
     * Creates a new lazy consumer based on a regular one.
     * 
     * @param <T> the type of the input to the action
     * @param consumer the regular consumer that may be eventually performed
     * @return the new lazy consumer
     */
    static <T> LazyConsumer<T> of(Consumer<T> consumer) {
        return Objects.requireNonNull(consumer)::accept;
    }
}
