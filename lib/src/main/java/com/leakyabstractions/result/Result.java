
package com.leakyabstractions.result;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents the result of an operation that may have succeeded or failed.
 * <p>
 * {@code Result} is a value-based class that describes one of these two mutually-exclusive fulfillment states:
 * <ul>
 * <li><strong>Success</strong>: the operation completed entirely and may have produced a success value</li>
 * <li><strong>Failure</strong>: the operation could not go through and may have produced a failure value</li>
 * </ul>
 * On the one hand, a result object conveys fulfillment state. On the other hand, it also holds a value produced by the
 * operation. It may be {@code null} and its meaning totally depends on the semantics defined by the operation:
 * <ul>
 * <li>If the fulfillment state is <strong>success</strong>, {@code isSuccess()} will return {@code true} and
 * {@code getSuccess()} will return the <em>success</em> value.</li>
 * <li>If the fulfillment state is <strong>failure</strong>, {@code isFailure()} will return {@code true} and
 * {@code getFailure()} will return the <em>failure</em> value.</li>
 * </ul>
 * <p>
 * Additional methods that depend on fulfillment state are provided, such as {@link #orElse(java.lang.Object) orElse()}
 * (return an <em>alternative success value</em> if the operation failed) and
 * {@link #ifSuccess(java.util.function.Consumer) ifSuccess()} (execute a block of code if the operation succeeded).
 * 
 * @apiNote A variable whose type is {@code Result} should never itself be {@code null}; it should always point to a
 *          {@code Result} instance.
 * @implNote Instances of classes that implement {@code Result}:
 *           <ul>
 *           <li>are {@code final} and immutable (though may contain references to mutable objects);</li>
 *           <li>have implementations of {@code equals}, {@code hashCode}, and {@code toString} which are computed
 *           solely from the instance's state and not from its identity or the state of any other object or
 *           variable;</li>
 *           <li>make no use of identity-sensitive operations such as reference equality ({@code ==}) between instances,
 *           identity hash code of instances, or synchronization on an instances's intrinsic lock;</li>
 *           <li>are considered equal solely based on {@code equals()}, not based on reference equality
 *           ({@code ==});</li>
 *           <li>do not have accessible constructors, but are instead instantiated through factory methods (such as the
 *           ones provided by {@link DefaultResult}) which make no commitment as to the identity of returned
 *           instances;</li>
 *           <li>are <em>freely substitutable</em> when equal, meaning that interchanging any two instances {@code x}
 *           and {@code y} that are equal according to {@code equals()} in any computation or method invocation should
 *           produce no visible change in behavior.</li>
 *           </ul>
 * @author Guillermo Calvo
 * @see DefaultResult
 * @param <S> the type of the success value
 * @param <F> the type of the failure value
 */
public interface Result<S, F> {

    /**
     * Return {@code true} if this is a successful result; otherwise return {@code false}.
     *
     * @return {@code true} if this is a successful result; otherwise {@code false}
     * @see Result#isFailure()
     */
    boolean isSuccess();

    /**
     * Return {@code true} if this is a failed result; otherwise return {@code false}.
     *
     * @return {@code true} if this is a failed result; otherwise {@code false}
     * @see Result#isSuccess()
     */
    boolean isFailure();

    /**
     * Return the success value if this is a successful result; otherwise return {@code other}.
     *
     * @param other the value to be returned if this is a failed result, may be {@code null}
     * @return the success value if this is a successful result; otherwise {@code other}
     * @see Result#isSuccess()
     */
    S orElse(S other);

    /**
     * Return the success value if this is a successful result; otherwise return a value to be created by the provided
     * mapping function.
     * <p>
     * If this is a failed result, the <em>failure</em> value will be applied to the provided mapping {@link Function}
     * to produce an alternative <em>success</em> value.
     *
     * @param failureMapper a mapping function which will produce the value to be returned if this is a failed result
     * @return the success value if this is a successful result; otherwise a value to be created by the provided mapping
     *         function
     * @throws NullPointerException if this is a failed result and {@code failureMapper} is {@code null}
     * @see Result#isSuccess()
     */
    S orElseMap(Function<? super F, ? extends S> failureMapper);

    /**
     * Return the success value if this is a successful result; otherwise throws {@code NoSuchElementException}.
     *
     * @return the success value if this is a successful result, may be {@code null}
     * @throws java.util.NoSuchElementException if this is a failed result
     * @see Result#isSuccess()
     */
    S orElseThrow();

    /**
     * Return the success value if this is a successful result; otherwise throw an exception to be created by the
     * provided mapping function.
     * <p>
     * If this is a failed result, the <em>failure</em> value will be applied to the provided mapping {@link Function}
     * to produce an exception.
     * 
     * @param <E> Type of the exception to be thrown
     * @param failureMapper a mapping function which will produce the exception to be thrown if this is a failed result
     * @return the success value if this is a successful result
     * @throws E if the operation failed
     * @throws NullPointerException if this is a failed result and {@code failureMapper} is {@code null}
     * @see Result#isSuccess()
     */
    <E extends Throwable> S orElseThrow(Function<? super F, E> failureMapper) throws E;

    /**
     * Return the failure value if this is a failed result; otherwise throws {@code NoSuchElementException}.
     *
     * @return the failure value if this is a failed result, may be {@code null}
     * @throws java.util.NoSuchElementException if this is a successful result
     * @see Result#isFailure()
     */
    F getFailureOrElseThrow();

    /**
     * Perform the given action with the success value if this is a successful result; otherwise do nothing.
     * 
     * @param successAction the action to be performed if this is a successful result
     * @throws NullPointerException if this is a successful result and {@code successAction} is {@code null}
     * @return this result
     */
    Result<S, F> ifSuccess(Consumer<? super S> successAction);

    /**
     * Perform the given action with the success value if this is a successful result; otherwise perform the given
     * failure-based action.
     * 
     * @param successAction the action to be performed if this is a successful result
     * @param failureAction the action to be performed if this is a failed result
     * @throws NullPointerException if this is a successful result and {@code successAction}; or if this is a failed
     *             result and {@code failureAction} is {@code null}
     * @return this result
     */
    Result<S, F> ifSuccessOrElse(Consumer<? super S> successAction, Consumer<? super F> failureAction);

    /**
     * Perform the given action with the failure value if this is a failed result; otherwise do nothing.
     * 
     * @param failureAction the action to be performed if this is a failed result
     * @throws NullPointerException if this is a failed result and {@code failureAction} is {@code null}
     * @return this result
     */
    Result<S, F> ifFailure(Consumer<? super F> failureAction);

    /**
     * Return a new failed result if this is a successful result whose value does not match the provided predicate;
     * otherwise return this result.
     * <p>
     * If this is a successful result whose value does not match the provided {@link Predicate}, the <em>success</em>
     * value will be applied to the provided mapping {@link Function} to produce a <em>failure</em> value to be held by
     * the new failed {@code Result}.
     * 
     * @param predicate a predicate to apply to the success value if this is a successful result
     * @param failureMapper a mapping function which will produce the value to be held by a new failed result
     * @throws NullPointerException if this is a successful result whose value does not match the provided predicate and
     *             {@code failureAction} is {@code null}
     * @return a new failed result if this is a successful result whose value does not match the provided predicate;
     *         otherwise this result
     */
    Result<S, F> filter(Predicate<? super S> predicate, Function<? super S, ? extends F> failureMapper);

    /**
     * Apply the appropriate mapping {@link Function} to the success or failure value and return a new {@code Result}
     * with the produced value.
     * <p>
     * Depending on the fulfillment state, the value will be applied to either one of the provided mapping functions to
     * produce a new value. Then the new {@code Result} will be created with the same fulfillment state and the produced
     * value. The types of the new success/failure values may be different from this result's.
     * 
     * @param <S2> the type of the new success value
     * @param <F2> the type of the new failure value
     * @param successMapper a mapping {@code Function} to apply to the success value if this is a successful result
     * @param failureMapper a mapping {@code Function} to apply to the failure value if this is a failed result
     * @throws NullPointerException if this is a successful result and {@code successMapper} is {@code null}; or if this
     *             is a failed result and {@code failureMapper} is {@code null}
     * @return a new {@code Result} whose value depends on this result's value
     */
    <S2, F2> Result<S2, F2> map(Function<? super S, S2> successMapper, Function<? super F, F2> failureMapper);

    /**
     * Apply the provided mapping {@link Function} to the success value if this is a successful result and return a new
     * successful {@code Result} with the produced value; otherwise return a failed result with the same
     * <em>failure</em> value.
     * <p>
     * If this is a successful result, the success value will be applied to the provided mapping {@code Function} to
     * produce a new value. Then the new successful {@code Result} will be created with the produced value. The type of
     * the new success value may be different from this result's.
     * 
     * @param <S2> the type of the new success value
     * @param successMapper a mapping {@code Function} to apply to the success value if this is a successful result
     * @throws NullPointerException if this is a successful result and {@code successMapper} is {@code null}
     * @return a new successful result with a new <em>success</em> value if this is a successful result; otherwise a
     *         failed result with the same <em>failure</em> value
     */
    <S2> Result<S2, F> mapSuccess(Function<? super S, S2> successMapper);

    /**
     * Apply the provided mapping {@link Function} to the failure value if this is a failed result and return a new
     * failed {@code Result} with the produced value; otherwise return a successful result with the same
     * <em>success</em> value.
     * <p>
     * If this is a failed result, the failure value will be applied to the provided mapping {@code Function} to produce
     * a new value. Then the new failed {@code Result} will be created with the produced value. The type of the new
     * failure value may be different from this result's.
     * 
     * @param <F2> the type of the new failure value
     * @param failureMapper a mapping {@code Function} to apply to the failure value if this is a failed result
     * @throws NullPointerException if this is a failed result and {@code failureMapper} is {@code null}
     * @return a new failed result with a new <em>failure</em> value if this is a failed result; otherwise a successful
     *         result with the same <em>success</em> value
     */
    <F2> Result<S, F2> mapFailure(Function<? super F, F2> failureMapper);

    /**
     * Apply the appropriate {@code Result}-bearing mapping {@link Function} to the success or failure value and return
     * the produced result.
     * <p>
     * Depending on the fulfillment state, the value will be applied to either one of the provided mapping functions to
     * produce a new {@code Result}. The fulfillment state and the types of the new success/failure values may be
     * different from this result's.
     * 
     * @param <S2> the type of the new success value
     * @param <F2> the type of the new failure value
     * @param successFlatMapper a {@code Function} to apply to the success value if this is a successful result; it must
     *            produce a new {@code Result}
     * @param failureFlatMapper a {@code Function} to apply to the failure value if this is a failed result; it must
     *            produce a new {@code Result}
     * @throws NullPointerException if this is a successful result and {@code successFlatMapper} is {@code null}; or if
     *             this is a failed result and {@code failureFlatMapper} is {@code null}
     * @return the result of applying a {@code Result}-bearing mapping function to this result's value
     */
    <S2, F2> Result<S2, F2> flatMap(
            Function<? super S, Result<S2, F2>> successFlatMapper,
            Function<? super F, Result<S2, F2>> failureFlatMapper);

    /**
     * Apply the provided {@code Result}-bearing mapping {@link Function} to the success value and return the produced
     * result if this is a successful result; otherwise return a failed result with the same <em>failure</em> value.
     * <p>
     * If this is a successful result, the success value will be applied to the provided mapping {@code Function} to
     * produce a new {@code Result}. The fulfillment state and the type of the new success value may be different from
     * this result's.
     * 
     * @param <S2> the type of the new success value
     * @param successFlatMapper a {@code Function} to apply to the success value if this is a successful result; it must
     *            produce a new {@code Result}
     * @throws NullPointerException if this is a successful result and {@code successFlatMapper} is {@code null}
     * @return the result of applying a {@code Result}-bearing mapping function the success value if this is a
     *         successful result; otherwise a failed result with the same <em>failure</em> value.
     */
    <S2> Result<S2, F> flatMapSuccess(Function<? super S, Result<S2, F>> successFlatMapper);

    /**
     * Apply the provided {@code Result}-bearing mapping {@link Function} to the failure value and return the produced
     * result if this is a failed result; otherwise return a successful result with the same <em>success</em> value.
     * <p>
     * If this is a failed result, the failure value will be applied to the provided mapping {@code Function} to produce
     * a new {@code Result}. The fulfillment state and the type of the new failure value may be different from this
     * result's.
     * 
     * @param <F2> the type of the new failure value
     * @param failureFlatMapper a {@code Function} to apply to the failure value if this is a failed result; it must
     *            produce a new {@code Result}
     * @throws NullPointerException if this is a failed result and {@code failureFlatMapper} is {@code null}
     * @return the result of applying a {@code Result}-bearing mapping function to the failure value if this is a failed
     *         result; otherwise a successful result with the same <em>failure</em> value.
     */
    <F2> Result<S, F2> flatMapFailure(Function<? super F, Result<S, F2>> failureFlatMapper);
}
