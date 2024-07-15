/**
 * A Java library to handle success and failure without exceptions
 * <p>
 * <img src="https://dev.leakyabstractions.com/result/result.svg" alt="Result Library">
 * <h2>Result Library for Java</h2>
 * <p>
 * Wave goodbye to slow exceptions and embrace clean, efficient error handling by encapsulating operations that may
 * succeed or fail in a type-safe way.
 * <ul>
 * <li><strong>Boost Performance</strong>: Avoid exception overhead and benefit from faster operations.
 * <li><strong>Simple API</strong>: Leverage a familiar interface for a smooth learning curve.
 * <li><strong>Streamlined Error Handling</strong>: Handle failure explicitly to simplify error propagation.
 * <li><strong>Safe Execution</strong>: Ensure safer and more predictable operation outcomes.
 * <li><strong>Enhanced Readability</strong>: Reduce complexity to make your code easier to understand.
 * <li><strong>Functional Style</strong>: Embrace elegant, functional programming paradigms.
 * <li><strong>Lightweight</strong>: Keep your project slim with no extra dependencies.
 * <li><strong>Open Source</strong>: Enjoy transparent, permissive Apache 2 licensing.
 * <li><strong>Pure Java</strong>: Seamless compatibility from JDK8 to the latest versions.
 * </ul>
 * <p>
 * {@link com.leakyabstractions.result.api.Result} objects represent the outcome of an operation, removing the need to
 * check for {@code null}. Operations that succeed produce results encapsulating a <em>success</em> value; operations
 * that fail produce results with a <em>failure</em> value. Success and failure can be represented by whatever types
 * make the most sense for each operation.
 * <h3>Results in a Nutshell</h3>
 * <p>
 * In Java, methods that can fail typically do so by throwing exceptions. Then, exception-throwing methods are called
 * from inside a {@code try} block to handle errors in a separate {@code catch} block.
 *
 * <pre class="row-color rowColor">
 * <code>&nbsp;
 * int getServerUptime() {
 *     int hours;
 *     try {
 *         Server server = connect();
 *         server.refresh();
 *         hours = server.getUptime();
 *     } catch (ConnectionException exception) {
 *         logger.error(exception.getMessage());
 *         hours = -1;
 *     }
 *     return hours;
 * }</code>
 * </pre>
 * <p>
 * This approach is lengthy, and that's not the only problem &mdash; it's also very slow.
 * <p>
 * Conventional wisdom says <strong> exceptional logic shouldn't be used for normal program flow</strong>. Results make
 * us deal with expected error situations explicitly to enforce good practices and make our programs
 * <a href="https://result.leakyabstractions.com/extra/benchmark">run faster</a>.
 * <p>
 * Let's now look at how the above code could be refactored if {@code connect()} returned a
 * {@link com.leakyabstractions.result.api.Result} object instead of throwing an exception.
 *
 * <pre class="row-color rowColor">
 * <code>&nbsp;
 * int getServerUptime() {
 *     final Result&lt;Server, String&gt; result = connect();
 *     result.ifSuccess(Server::refresh);
 *     result.ifFailure(logger::error);
 *     return result.mapSuccess(Server::getUptime).orElse(-1);
 * }</code>
 * </pre>
 * <p>
 * In the example above, we used only 4 lines of code to replace the 10 that worked for the first one. But we can
 * effortlessly make it shorter by chaining methods. In fact, since we were returning {@code -1} just to signal that the
 * underlying operation failed, we are better off returning a {@link com.leakyabstractions.result.api.Result} object
 * upstream. This will allow us to compose operations on top of {@code getServerUptime()} just like we did with {@code
 * connect()}.
 *
 * <pre class="row-color rowColor">
 * <code>&nbsp;
 * int getServerUptime() {
 *     return connect().ifSuccessOrElse(Server::refresh, logger::error)
 *         .mapSuccess(Server::getUptime);
 * }</code>
 * </pre>
 * <p>
 * {@link com.leakyabstractions.result.api.Result} objects are immutable, providing thread safety without the need for
 * synchronization. This makes them ideal for multi-threaded applications, ensuring predictability and eliminating side
 * effects.
 *
 * @author <a href="https://guillermo.dev/">Guillermo Calvo</a>
 * @see <a href="https://result.leakyabstractions.com/docs/start/creating-results">Quick guide</a>
 * @see <a href="https://github.com/LeakyAbstractions/result/">Source code</a>
 * @see com.leakyabstractions.result.core.Results
 */

package com.leakyabstractions.result.core;
