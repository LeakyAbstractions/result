/*
 * Copyright 2022 Guillermo Calvo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leakyabstractions.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Represents either the success or failure of an operation, including an associated value in each case.
 * <p>
 * On the one hand, a result object conveys one of these two mutually-exclusive states:
 * <ul>
 * <li><strong>Success</strong>: the operation completed entirely.</li>
 * <li><strong>Failure</strong>: the operation could not get through.</li>
 * </ul>
 * On the other hand, it also holds a non-{@code null} value whose meaning totally depends on the semantics defined by
 * the operation:
 * <ul>
 * <li>A <em>successful</em> result wraps a value of type {@code S}.</li>
 * <li>A <em>failed</em> result wraps a value of type {@code F}.</li>
 * </ul>
 * <p>
 * Result state can be determined via {@link #hasSuccess() hasSuccess} or {@link #hasFailure() hasFailure}. Additional
 * methods to unwrap the included value are provided, such as {@link #orElse(Object) orElse} (return an <em>alternative
 * success value</em> if the operation failed) and {@link #ifSuccess(Consumer) ifSuccess} (execute a block of code if
 * the operation succeeded).
 *
 * @apiNote {@code Result} is primarily intended for use as a method return type whenever failure is expected and
 *     recoverable, and where using {@code null} is likely to cause errors. A variable whose type is {@code Result}
 *     should never itself be {@code null}; it should always point to a {@code Result} instance.
 * @implSpec This is a
 *     <a href="https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/lang/doc-files/ValueBased.html">
 *     value-based</a> type. Instances of classes that implement {@code Result}:
 *     <ul>
 *     <li>MUST be {@code final} and immutable (though MAY contain references to mutable objects);</li>
 *     <li>MUST have implementations of {@code equals}, {@code hashCode}, and {@code toString} which are computed solely
 *     from the instance's state and not from its identity or the state of any other object or variable;</li>
 *     <li>MUST NOT make no use of identity-sensitive operations such as reference equality ({@code ==}) between
 *     instances, identity hash code of instances, or synchronization on an instances's intrinsic lock;</li>
 *     <li>MUST be considered equal solely based on {@code equals()}, not based on reference equality ({@code ==});</li>
 *     <li>MUST NOT have accessible constructors, but SHOULD be instead instantiated through factory methods (such as
 *     the ones provided by {@link Results}) which make no commitment as to the identity of returned instances;</li>
 *     <li>MUST be <em>freely substitutable</em> when equal, meaning that interchanging any two instances {@code x} and
 *     {@code y} that are equal according to {@code equals()} in any computation or method invocation SHOULD produce no
 *     visible change in behavior.</li>
 *     </ul>
 * @author Guillermo Calvo
 * @see com.leakyabstractions.result
 * @see Results
 * @param <S> the type of the success value
 * @param <F> the type of the failure value
 */
public interface Result<S, F> {

    /**
     * If this is a successful result, returns an optional containing its success value; otherwise returns an empty
     * optional.
     *
     * @return this result's success value as an optional if successful; otherwise an empty optional.
     * @see #getFailure()
     */
    Optional<S> getSuccess();

    /**
     * If this is a failed result, returns an optional containing its failure value; otherwise returns an empty
     * optional.
     *
     * @return this result's failure value as an optional if failed; otherwise an empty optional.
     * @see #getSuccess()
     */
    Optional<F> getFailure();

    /**
     * If this is a successful result, returns {@code true}; otherwise {@code false}.
     *
     * @return {@code true} if successful; otherwise {@code false}
     * @see #hasFailure()
     */
    boolean hasSuccess();

    /**
     * If this is a failed result, returns {@code true}; otherwise {@code false}.
     *
     * @return {@code true} if failed; otherwise {@code false}
     * @see #hasSuccess()
     */
    boolean hasFailure();

    /**
     * If this is a successful result, returns its success value; otherwise returns {@code other}.
     *
     * @param other the possibly-{@code null} alternative success value
     * @return this result's success value if successful; otherwise {@code other}
     * @see #orElseMap(Function)
     */
    S orElse(S other);

    /**
     * If this is a successful result, returns its success value; otherwise returns the value produced by the given
     * mapping function.
     * <p>
     * The mapping function will be applied to this result's failure value to produce an alternative success value.
     *
     * @param mapper the mapping function that produces the possibly-{@code null} alternative success value
     * @return this result's success value if successful; otherwise the value produced by the mapping function
     * @throws NullPointerException if this is a failed result and {@code mapper} is {@code null}
     * @see #orElse(Object)
     */
    S orElseMap(Function<? super F, ? extends S> mapper);

    /**
     * If this is a successful result, returns a sequential stream containing only its success value; otherwise returns
     * an empty stream.
     *
     * @return this result's success value as a stream if successful; otherwise an empty stream.
     */
    Stream<S> stream();

    /**
     * If this is a successful result, performs the given action with its success value; otherwise does nothing.
     *
     * @param action the action to be applied to this result's success value
     * @throws NullPointerException if this is a successful result and {@code action} is {@code null}
     * @return this result
     * @see #ifFailure(Consumer)
     * @see #ifSuccessOrElse(Consumer, Consumer)
     */
    Result<S, F> ifSuccess(Consumer<? super S> action);

    /**
     * If this is a successful result, performs the given success action; otherwise performs the given failure action.
     *
     * @param successAction the action to be applied to this result's success value
     * @param failureAction the action to be applied to this result's failure value
     * @return this result
     * @throws NullPointerException if this is a successful result and {@code successAction} is {@code null}; or if it
     *     is failed and {@code failureAction} is {@code null}
     * @see #ifFailure(Consumer)
     * @see #ifSuccess(Consumer)
     */
    Result<S, F> ifSuccessOrElse(Consumer<? super S> successAction, Consumer<? super F> failureAction);

    /**
     * If this is a failed result, performs the given action with its failure value; otherwise does nothing.
     *
     * @param action the action to be applied to this result's failure value
     * @return this result
     * @throws NullPointerException if this is a failed result and {@code action} is {@code null}
     * @see #ifSuccess(Consumer)
     * @see #ifSuccessOrElse(Consumer, Consumer)
     */
    Result<S, F> ifFailure(Consumer<? super F> action);

    /**
     * If this is a successful result whose value does not match the given predicate, returns a new failed result with a
     * value produced by the given mapping function; otherwise returns this result.
     * <p>
     * The mapping function will be applied to this result's success value to produce the failure value.
     *
     * @param isAcceptable the predicate to apply to this result's success value
     * @param mapper the mapping function that produces the failure value
     * @return a new failed result with the value produced by {@code mapper} if this is a successful result whose value
     *     does not match the given predicate; otherwise this result
     * @throws NullPointerException if this is a successful result and {@code isAcceptable} is {@code null}; or if its
     *     success value is not acceptable and {@code mapper} is {@code null} or returns {@code null}
     * @see #fallBack(Predicate, Function)
     */
    Result<S, F> filter(Predicate<? super S> isAcceptable, Function<? super S, ? extends F> mapper);

    /**
     * If this is a failed result whose value matches the given predicate, returns a new successful result with a value
     * produced by the given mapping function; otherwise returns this result.
     * <p>
     * The mapping function will be applied to this result's failure value to produce the success value.
     *
     * @param isRecoverable the predicate to apply to this result's failure value
     * @param mapper the mapping function that produces the success value
     * @return a new successful result with the value produced by {@code mapper} if this is a failed result whose value
     *     matches the given predicate; otherwise this result
     * @throws NullPointerException if this is a failed result and {@code isRecoverable} is {@code null}; or if its
     *     failure value is recoverable and {@code mapper} is {@code null} or returns {@code null}
     * @see #filter(Predicate, Function)
     */
    Result<S, F> fallBack(Predicate<? super F> isRecoverable, Function<? super F, ? extends S> mapper);

    /**
     * Returns a new result with the value produced by the appropriate mapping function.
     * <p>
     * Depending on this result's state, one of the two given functions will be applied to its success or failure value
     * to produce a new value. The types of the new <em>success/failure</em> values may be different from this result's.
     *
     * @param <S2> the type of the value returned by {@code successMapper}
     * @param <F2> the type of the value returned by {@code failureMapper}
     * @param successMapper the mapping function that produces a success value
     * @param failureMapper the mapping function that produces a failure value
     * @return a new result with a value produced by either {@code successMapper} or {@code failureMapper}
     * @throws NullPointerException if this is a successful result and {@code successMapper} is {@code null} or returns
     *     {@code null}; or if this is a failed result and {@code failureMapper} is {@code null} or returns {@code null}
     * @see #mapFailure(Function)
     * @see #mapSuccess(Function)
     */
    <S2, F2> Result<S2, F2> map(
            Function<? super S, ? extends S2> successMapper,
            Function<? super F, ? extends F2> failureMapper);

    /**
     * If this is a successful result, returns a new successful result with the value produced by the given mapping
     * function; otherwise returns a failed result with this result's failure value.
     * <p>
     * The mapping function will be applied to this result's success value to produce the new success value. The type of
     * the new success value may be different from this result's.
     *
     * @param <S2> the type of the value returned by {@code mapper}
     * @param mapper the mapping function that produces the new success value
     * @return a new successful result with the value produced by {@code mapper} if this is a successful result;
     *     otherwise a failed result with this result's failure value
     * @throws NullPointerException if this is a successful result and {@code mapper} is {@code null} or returns
     *     {@code null}
     * @see #map(Function, Function)
     * @see #mapFailure(Function)
     */
    <S2> Result<S2, F> mapSuccess(Function<? super S, ? extends S2> mapper);

    /**
     * If this is a failed result, returns a new failed result with the value produced by the given mapping function;
     * otherwise returns a successful result with this result's success value.
     * <p>
     * The mapping function will be applied to this result's failure value to produce the new failure value. The type of
     * the new failure value may be different from this result's.
     *
     * @param <F2> the type of the value returned by {@code mapper}
     * @param mapper the mapping function that produces the new failure value
     * @return a new failed result with the value produced by {@code mapper} if this is a failed result; otherwise a
     *     successful result with this result's success value
     * @throws NullPointerException if this is a failed result and {@code mapper} is {@code null} or returns
     *     {@code null}
     * @see #map(Function, Function)
     * @see #mapSuccess(Function)
     */
    <F2> Result<S, F2> mapFailure(Function<? super F, ? extends F2> mapper);

    /**
     * Returns a new result produced by the appropriate {@code Result}-bearing mapping function.
     * <p>
     * Depending on this result's state, one of the two given functions will be applied to its success or failure value
     * to produce a new result. State and types may be different from this result's.
     *
     * @param <S2> the success type of the result returned by the given functions
     * @param <F2> the failure type of the result returned by the given functions
     * @param successMapper the mapping function that produces a new result if this is a successful result
     * @param failureMapper the mapping function that produces a new result if this is a failed result
     * @return the result produced by either {@code successMapper} or {@code failureMapper}
     * @throws NullPointerException if this is a successful result and {@code successMapper} is {@code null} or returns
     *     {@code null}; or if this is a failed result and {@code failureMapper} is {@code null} or returns {@code null}
     * @see #flatMapFailure(Function)
     * @see #flatMapSuccess(Function)
     */
    <S2, F2> Result<S2, F2> flatMap(
            Function<? super S, ? extends Result<? extends S2, ? extends F2>> successMapper,
            Function<? super F, ? extends Result<? extends S2, ? extends F2>> failureMapper);

    /**
     * If this is a successful result, returns a new result produced by the given, {@code Result}-bearing mapping
     * function; otherwise returns a failed result with this result's failure value.
     * <p>
     * The mapping function will be applied to this result's success value to produce a new result. State and success
     * type may be different from this result's.
     *
     * @param <S2> the success type of the result returned by {@code mapper}
     * @param mapper the mapping function that produces a new result
     * @return the result produced by {@code mapper} if this is a successful result; otherwise a failed result with this
     *     result's failure value.
     * @throws NullPointerException if this is a successful result and {@code mapper} is {@code null} or returns
     *     {@code null}
     * @see #flatMap(Function, Function)
     * @see #flatMapFailure(Function)
     */
    <S2> Result<S2, F> flatMapSuccess(Function<? super S, ? extends Result<? extends S2, ? extends F>> mapper);

    /**
     * If this is a failed result, returns a new result produced by the given, {@code Result}-bearing mapping function;
     * otherwise returns a successful result with this result's success value.
     * <p>
     * The mapping function will be applied to this result's failure value to produce a new result. State and failure
     * type may be different from this result's.
     *
     * @param <F2> the failure type of the result returned by {@code mapper}
     * @param mapper the mapping function that produces a new result
     * @return the result produced by {@code mapper} if this is a failed result; otherwise a successful result with this
     *     result's success value.
     * @throws NullPointerException if this is a failed result and {@code mapper} is {@code null} or returns
     *     {@code null}
     * @see #flatMap(Function, Function)
     * @see #flatMapSuccess(Function)
     */
    <F2> Result<S, F2> flatMapFailure(Function<? super F, ? extends Result<? extends S, ? extends F2>> mapper);
}
