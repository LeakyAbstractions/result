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
 * <li><strong>Success</strong>: the operation completed entirely.
 * <li><strong>Failure</strong>: the operation could not get through.
 * </ul>
 * <p>
 * On the other hand, it also holds a non-{@code null} value whose meaning totally depends on the semantics defined by
 * the operation:
 * <ul>
 * <li>A <em>successful</em> result wraps a value of type {@code S}.
 * <li>A <em>failed</em> result wraps a value of type {@code F}.
 * </ul>
 * <p>
 * Result state can be determined via {@link #hasSuccess() hasSuccess()} or {@link #hasFailure() hasFailure()}.
 * Additional methods to unwrap the included value are provided, such as {@link #orElse(Object) orElse()} (return an
 * <em>alternative success value</em> if the operation failed) and {@link #ifSuccess(Consumer) ifSuccess()} (execute a
 * block of code if the operation succeeded).
 *
 * @apiNote {@code Result} is primarily, but not only, intended for use as a method return type whenever failure is
 *     expected and recoverable, and where using {@code null} is likely to cause errors. A variable whose type is
 *     {@code Result} should never itself be {@code null}; it should always point to a {@code Result} instance.
 * @implSpec This is a
 *     <a href="https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/lang/doc-files/ValueBased.html">
 *     value-based</a> type. Classes that implement {@code Result}:
 *     <ul>
 *     <li>MUST declare only {@code final} instance fields (though these may contain references to mutable objects);
 *     <li>MUST have implementations of {@code equals}, {@code hashCode}, and {@code toString} which are computed solely
 *     from the values of the class's instance fields (and the members of the objects they reference), not from the
 *     instance's identity;
 *     <li>MUST treat instances as <em>freely substitutable</em> when equal, meaning that interchanging any two
 *     instances {@code x} and {@code y} that are equal according to {@code equals()} produces no visible change in the
 *     behavior of the class's methods;
 *     <li>SHOULD NOT perform any synchronization using an instance's monitor;
 *     <li>MUST NOT declare (or has deprecated any) accessible constructors;
 *     <li>MUST NOT provide any instance creation mechanism that promises a unique identity on each method call - in
 *     particular, any factory method's contract must allow for the possibility that if two independently-produced
 *     instances are equal according to {@code
 *           equals()}, they may also be equal according to {@code ==};
 *     <li>MUST be {@code final}, and extend either {@code Object} or a hierarchy of abstract classes that declare no
 *     instance fields or instance initializers and whose constructors are empty.
 *     </ul>
 * @author Guillermo Calvo
 * @see com.leakyabstractions.result
 * @see Results
 * @param <S> the type of the success value
 * @param <F> the type of the failure value
 */
public interface Result<S, F> {

    /**
     * If this is a successful result, returns {@code true}; otherwise {@code false}.
     * <p>
     * <img src="doc-files/hasSuccess.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testHasSuccess(Result&lt;Integer, String&gt; result) {
     *   final boolean x = result.hasSuccess();
     * }</code>
     * </pre>
     *
     * @return {@code true} if successful; otherwise {@code false}
     * @see #hasFailure()
     */
    boolean hasSuccess();

    /**
     * If this is a failed result, returns {@code true}; otherwise {@code false}.
     * <p>
     * <img src="doc-files/hasFailure.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testHasFailure(Result&lt;Integer, String&gt; result) {
     *   boolean x = result.hasFailure();
     * }</code>
     * </pre>
     *
     * @return {@code true} if failed; otherwise {@code false}
     * @see #hasSuccess()
     */
    boolean hasFailure();

    /**
     * If this is a successful result, returns an optional containing its success value; otherwise returns an empty
     * optional.
     * <p>
     * <img src="doc-files/getSuccess.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testGetSuccess(Result&lt;Integer, String&gt; result) {
     *   Optional&lt;Integer&gt; x = result.getSuccess();
     * }</code>
     * </pre>
     *
     * @return this result's success value as an optional if successful; otherwise an empty optional.
     * @see #getFailure()
     */
    Optional<S> getSuccess();

    /**
     * If this is a failed result, returns an optional containing its failure value; otherwise returns an empty
     * optional.
     * <p>
     * <img src="doc-files/getFailure.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testGetFailure(Result&lt;Integer, String&gt; result) {
     *   Optional&lt;String&gt; x = result.getFailure();
     * }</code>
     * </pre>
     *
     * @return this result's failure value as an optional if failed; otherwise an empty optional.
     * @see #getSuccess()
     */
    Optional<F> getFailure();

    /**
     * If this is a successful result, returns its success value; otherwise returns {@code other}.
     * <p>
     * <img src="doc-files/orElse.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testOrElse(Result&lt;Integer, String&gt; result) {
     *   Integer other = 8;
     *   Integer x = result.orElse(other);
     * }</code>
     * </pre>
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
     * <p>
     * <img src="doc-files/orElseMap.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testOrElseMap(Result&lt;Integer, String&gt; result) {
     *   Function&lt;String, Integer&gt; mapper = f -&gt; 8;
     *   Integer x = result.orElseMap(mapper);
     * }</code>
     * </pre>
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
     * <p>
     * <img src="doc-files/streamSuccess.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testStreamSuccess(Result&lt;Integer, String&gt; result) {
     *   Stream&lt;Integer&gt; x = result.streamSuccess();
     * }</code>
     * </pre>
     *
     * @return this result's success value as a stream if successful; otherwise an empty stream.
     */
    Stream<S> streamSuccess();

    /**
     * If this is a failed result, returns a sequential stream containing only its failure value; otherwise returns an
     * empty stream.
     * <p>
     * <img src="doc-files/streamFailure.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testStreamFailure(Result&lt;Integer, String&gt; result) {
     *   Stream&lt;String&gt; x = result.streamFailure();
     * }</code>
     * </pre>
     *
     * @return this result's failure value as a stream if failed; otherwise an empty stream.
     */
    Stream<F> streamFailure();

    /**
     * If this is a successful result, performs the given action with its success value; otherwise does nothing.
     * <p>
     * <img src="doc-files/ifSuccess.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testIfSuccess(Result&lt;Integer, String&gt; result) {
     *   Consumer&lt;Integer&gt; task = s -&gt; {};
     *   Result&lt;Integer, String&gt; x = result.ifSuccess(task);
     * }</code>
     * </pre>
     *
     * @param action the action to be applied to this result's success value
     * @throws NullPointerException if this is a successful result and {@code action} is {@code null}
     * @return this result
     * @see #ifFailure(Consumer)
     * @see #ifSuccessOrElse(Consumer, Consumer)
     */
    Result<S, F> ifSuccess(Consumer<? super S> action);

    /**
     * If this is a failed result, performs the given action with its failure value; otherwise does nothing.
     * <p>
     * <img src="doc-files/ifFailure.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testIfFailure(Result&lt;Integer, String&gt; result) {
     *   Consumer&lt;Integer&gt; task = f -&gt; {};
     *   Result&lt;Integer, String&gt; x = result.ifFailure(task);
     * }</code>
     * </pre>
     *
     * @param action the action to be applied to this result's failure value
     * @return this result
     * @throws NullPointerException if this is a failed result and {@code action} is {@code null}
     * @see #ifSuccess(Consumer)
     * @see #ifSuccessOrElse(Consumer, Consumer)
     */
    Result<S, F> ifFailure(Consumer<? super F> action);

    /**
     * If this is a successful result, performs the given success action; otherwise performs the given failure action.
     * <p>
     * <img src="doc-files/ifSuccessOrElse.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testIfSuccessOrElse(Result&lt;Integer, String&gt; result) {
     *   Consumer&lt;Integer&gt; task1 = s -&gt; {};
     *   Consumer&lt;String&gt; task2 = f -&gt; {};
     *   Result&lt;Integer, String&gt; x = result.ifSuccessOrElse(task1, task2);
     * }</code>
     * </pre>
     *
     * @param successAction the action to be applied to this result's success value
     * @param failureAction the action to be applied to this result's failure value
     * @return this result
     * @throws NullPointerException if this is a successful result and {@code successAction} is {@code
     *     null}; or if it is failed and {@code failureAction} is {@code null}
     * @see #ifFailure(Consumer)
     * @see #ifSuccess(Consumer)
     */
    Result<S, F> ifSuccessOrElse(
            Consumer<? super S> successAction, Consumer<? super F> failureAction);

    /**
     * If this is a successful result whose value does not match the given predicate, returns a new failed result with a
     * value produced by the given mapping function; otherwise returns this result.
     * <p>
     * The mapping function will be applied to this result's success value to produce the failure value.
     * <p>
     * <img src="doc-files/filter.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testFilter(Result&lt;Integer, String&gt; result) {
     *   Predicate&lt;Integer&gt; isAcceptable = s -&gt; s &lt; 3;
     *   Function&lt;Integer, String&lt; mapper = s -&gt; "E";
     *   Result&lt;Integer, String&gt; x = result.filter(isAcceptable, mapper);
     * }</code>
     * </pre>
     *
     * @param isAcceptable the predicate to apply to this result's success value
     * @param mapper the mapping function that produces the failure value
     * @return a new failed result with the value produced by {@code mapper} if this is a successful result whose value
     *     does not match the given predicate; otherwise this result
     * @throws NullPointerException if this is a successful result and {@code isAcceptable} is {@code
     *     null}; or if its success value is not acceptable and {@code mapper} is {@code null} or returns {@code null}
     * @see #fallBack(Predicate, Function)
     */
    Result<S, F> filter(Predicate<? super S> isAcceptable, Function<? super S, ? extends F> mapper);

    /**
     * If this is a failed result whose value matches the given predicate, returns a new successful result with a value
     * produced by the given mapping function; otherwise returns this result.
     * <p>
     * The mapping function will be applied to this result's failure value to produce the success value.
     * <p>
     * <img src="doc-files/fallBack.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testFallBack(Result&lt;Integer, String&gt; result) {
     *   Predicate&lt;String&gt; isRecoverable = "B"::equals;
     *   Function&lt;String, Integer&gt; mapper = f -&gt; 5;
     *   Result&lt;Integer, String&gt; x = result.fallBack(isRecoverable, mapper);
     * }</code>
     * </pre>
     *
     * @param isRecoverable the predicate to apply to this result's failure value
     * @param mapper the mapping function that produces the success value
     * @return a new successful result with the value produced by {@code mapper} if this is a failed result whose value
     *     matches the given predicate; otherwise this result
     * @throws NullPointerException if this is a failed result and {@code isRecoverable} is {@code
     *     null}; or if its failure value is recoverable and {@code mapper} is {@code null} or returns {@code null}
     * @see #filter(Predicate, Function)
     */
    Result<S, F> fallBack(
            Predicate<? super F> isRecoverable, Function<? super F, ? extends S> mapper);

    /**
     * If this is a successful result, returns a new successful result with the value produced by the given mapping
     * function; otherwise returns a failed result with this result's failure value.
     * <p>
     * The mapping function will be applied to this result's success value to produce the new success value. The type of
     * the new success value may be different from this result's.
     * <p>
     * <img src="doc-files/mapSuccess.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testMapSuccess(Result&lt;Integer, String&gt; result) {
     *   Function&lt;Integer, Fruit&gt; mapper = s -&gt; CHERRIES;
     *   Result&lt;Fruit, String&gt; x = result.mapSuccess(mapper);
     * }</code>
     * </pre>
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
     * <p>
     * <img src="doc-files/mapFailure.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testMapFailure(Result&lt;Integer, String&gt; result) {
     *   Function&lt;String, Suit&gt; mapper = f -&gt; CLUBS;
     *   Result&lt;Integer, Suit&gt; x = result.mapFailure(mapper);
     * }</code>
     * </pre>
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
     * Returns a new result with the value produced by the appropriate mapping function.
     * <p>
     * Depending on this result's state, one of the two given functions will be applied to its success or failure value
     * to produce a new value. The types of the new <em>success/failure</em> values may be different from this result's.
     * <p>
     * <img src="doc-files/map.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testMap(Result&lt;Integer, String&gt; result) {
     *   Function&lt;Integer, Fruit&gt; mapper1 = s -&gt; CHERRIES;
     *   Function&lt;String, Suit&gt; mapper2 = f -&gt; CLUBS;
     *   Result&lt;Fruit, Suit&gt; x = result.map(mapper1, mapper2);
     * }</code>
     * </pre>
     *
     * @param <S2> the type of the value returned by {@code successMapper}
     * @param <F2> the type of the value returned by {@code failureMapper}
     * @param successMapper the mapping function that produces a success value
     * @param failureMapper the mapping function that produces a failure value
     * @return a new result with a value produced by either {@code successMapper} or {@code
     *     failureMapper}
     * @throws NullPointerException if this is a successful result and {@code successMapper} is {@code
     *     null} or returns {@code null}; or if this is a failed result and {@code failureMapper} is {@code null} or
     *     returns {@code null}
     * @see #mapFailure(Function)
     * @see #mapSuccess(Function)
     */
    <S2, F2> Result<S2, F2> map(
            Function<? super S, ? extends S2> successMapper,
            Function<? super F, ? extends F2> failureMapper);

    /**
     * If this is a successful result, returns a new result produced by the given, {@code
     * Result}-bearing mapping function; otherwise returns a failed result with this result's failure value.
     * <p>
     * The mapping function will be applied to this result's success value to produce a new result. State and success
     * type may be different from this result's.
     * <p>
     * <img src="doc-files/flatMapSuccess.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testFlatMapSuccess(Result&lt;Integer, String&gt; result) {
     *   Function&lt;Integer, Result&lt;Fruit, String&gt;&gt; mapper =
     *       s -&gt; s &lt; 3 ? Results.success(CHERRIES) : Results.failure("E");
     *   Result&lt;Fruit, String&gt; x = result.flatMapSuccess(mapper);
     * }</code>
     * </pre>
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
    <S2> Result<S2, F> flatMapSuccess(
            Function<? super S, ? extends Result<? extends S2, ? extends F>> mapper);

    /**
     * If this is a failed result, returns a new result produced by the given, {@code Result}-bearing mapping function;
     * otherwise returns a successful result with this result's success value.
     * <p>
     * The mapping function will be applied to this result's failure value to produce a new result. State and failure
     * type may be different from this result's.
     * <p>
     * <img src="doc-files/flatMapFailure.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testFlatMapFailure(Result&lt;Integer, String&gt; result) {
     *   Function&lt;String, Result&lt;Integer, Suit&gt;&gt; mapper =
     *       f -&gt; f.equals("B") ? Results.success(5) : Results.failure(CLUBS);
     *   Result&lt;Integer, Suit&gt; x = result.flatMapFailure(mapper);
     * }</code>
     * </pre>
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
    <F2> Result<S, F2> flatMapFailure(
            Function<? super F, ? extends Result<? extends S, ? extends F2>> mapper);

    /**
     * Returns a new result produced by the appropriate {@code Result}-bearing mapping function.
     * <p>
     * Depending on this result's state, one of the two given functions will be applied to its success or failure value
     * to produce a new result. State and types may be different from this result's.
     * <p>
     * <img src="doc-files/flatMap.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>// Example
     * void testFlatMap(Result&lt;Integer, String&gt; result) {
     *   Function&lt;Integer, Result&lt;Fruit, Suit&gt;&gt; mapper1 =
     *       s -&gt; s &lt; 3 ? success(CHERRIES) : failure(SPADES);
     *   Function&lt;String, Result&lt;Fruit, Suit&gt;&gt; mapper2 =
     *       f -&gt; f.equals("B") ? success(WATERMELON) : failure(CLUBS);
     *   Result&lt;Fruit, Suit&gt; x = result.flatMap(mapper1, mapper2);
     * }</code>
     * </pre>
     *
     * @param <S2> the success type of the result returned by the given functions
     * @param <F2> the failure type of the result returned by the given functions
     * @param successMapper the mapping function that produces a new result if this is a successful result
     * @param failureMapper the mapping function that produces a new result if this is a failed result
     * @return the result produced by either {@code successMapper} or {@code failureMapper}
     * @throws NullPointerException if this is a successful result and {@code successMapper} is {@code
     *     null} or returns {@code null}; or if this is a failed result and {@code failureMapper} is {@code null} or
     *     returns {@code null}
     * @see #flatMapFailure(Function)
     * @see #flatMapSuccess(Function)
     */
    <S2, F2> Result<S2, F2> flatMap(
            Function<? super S, ? extends Result<? extends S2, ? extends F2>> successMapper,
            Function<? super F, ? extends Result<? extends S2, ? extends F2>> failureMapper);
}
