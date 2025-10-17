/**
 * A Java library to handle success and failure without exceptions
 * <p>
 * <img src="https://dev.leakyabstractions.com/result/result.svg" alt="Result Library">
 * <h2>Result Library for Java</h2>
 * <h3>Creating Results</h3>
 * <p>
 * There are several ways to create result objects.
 * <h4>Successful Results</h4>
 * <p>
 * A successful result contains a non-null value produced by an operation when everything works as intended.
 * <ul>
 * <li>{@link com.leakyabstractions.result.core.Results#success(java.lang.Object) Results.success(S)}
 * </ul>
 * <h4>Failed Results</h4>
 * <p>
 * On the other hand, a failed result holds a value representing the problem that prevented the operation from
 * completing.
 * <ul>
 * <li>{@link com.leakyabstractions.result.core.Results#failure(java.lang.Object) Results.failure(S)}
 * </ul>
 * <h4>Results Based on Nullable Values</h4>
 * <p>
 * When we need to create results that depend on a possibly null value. If the first argument is {@code null}, then the
 * second one will be used to create a failed result. The second argument can be either a failure value or a function
 * that produces a failure value.
 * <ul>
 * <li>{@link com.leakyabstractions.result.core.Results#ofNullable(java.lang.Object, java.lang.Object)
 * Results.ofNullable(S, F)}
 * <li>{@link com.leakyabstractions.result.core.Results#ofNullable(java.lang.Object, java.util.function.Supplier)
 * Results.ofNullable(S, Supplier&lt;F&gt;)}
 * </ul>
 * <h4>Results Based on Optionals</h4>
 * <p>
 * We can also create results that depend on an {@link java.util.Optional Optional} value. If the first argument is an
 * empty optional, then the second one will be used to create a failed result. The second argument can be a
 * {@link java.util.function.Supplier Supplier} too.
 * <ul>
 * <li>{@link com.leakyabstractions.result.core.Results#ofOptional(java.util.Optional, java.lang.Object)
 * Results.ofOptional(Optional&lt;S&gt;, F)}
 * <li>{@link com.leakyabstractions.result.core.Results#ofOptional(java.util.Optional, java.util.function.Supplier)
 * Results.ofNullable(Optional&lt;S&gt;, Supplier&lt;F&gt;)}
 * </ul>
 * <h4>Results Based on Callables</h4>
 * <p>
 * Finally, if we have a task that may either return a success value or throw an exception, we can encapsulate it as a
 * result so we don't need to use a <em>try-catch</em> block.
 * <ul>
 * <li>{@link com.leakyabstractions.result.core.Results#ofCallable(java.util.concurrent.Callable)
 * Results.ofCallable(Callable&lt;S&gt;)}.
 * </ul>
 *
 * @author <a href="https://guillermo.dev/">Guillermo Calvo</a>
 * @see <a href="https://result.leakyabstractions.com/docs/start/creating-results">Quick guide</a>
 * @see <a href="https://leanpub.com/result/">Free book</a>
 * @see <a href="https://github.com/LeakyAbstractions/result/">Source code</a>
 * @see com.leakyabstractions.result.core.Results
 * @see com.leakyabstractions.result.api.Result Result
 */

package com.leakyabstractions.result.core;
