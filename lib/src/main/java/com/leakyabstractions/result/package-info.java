/**
 * A Java library to handle success and failure without exceptions
 * <h2>Result Library for Java</h2>
 * <p>
 * The purpose of this library is to provide a type-safe encapsulation of operation results that may have succeeded or
 * failed, instead of throwing exceptions.
 * <p>
 * If you like {@link java.util.Optional} but feel that it sometimes falls too short, you'll love {@link Result}.
 * <p>
 * The best way to think of {@code Result} is as a super-powered version of {@code Optional}. The only difference is
 * that whereas {@code Optional} may contain a successful value or express the absence of a value, {@code Result}
 * contains either a successful value or a failure value that explains what went wrong.
 * <div class="overview-summary overviewSummary">
 * <table class="summary-table memberSummary">
 * <caption> Result objects have methods equivalent to those in Optional, plus a few more to handle failure values.
 * </caption> <thead>
 * <tr>
 * <th class="col-first colFirst">Optional</th>
 * <th class="col-last colLast">Result</th>
 * </tr>
 * </thead> <tbody>
 * <tr class="row-color rowColor">
 * <td>{@link java.util.Optional#isPresent() isPresent}</td>
 * <td>{@link Result#hasSuccess() hasSuccess}</td>
 * </tr>
 * <tr class="alt-color altColor">
 * <td>{@link java.util.Optional#isEmpty() isEmpty}</td>
 * <td>{@link Result#hasFailure() hasFailure}</td>
 * </tr>
 * <tr class="row-color rowColor">
 * <td>{@link java.util.Optional#get() get}</td>
 * <td>{@link Result#getSuccess() getSuccess}</td>
 * </tr>
 * <tr class="alt-color altColor">
 * <td>&nbsp;</td>
 * <td>{@link Result#getFailure() getFailure}</td>
 * </tr>
 * <tr class="row-color rowColor">
 * <td>{@link java.util.Optional#orElse(Object) orElse}</td>
 * <td>{@link Result#orElse(Object) orElse}</td>
 * </tr>
 * <tr class="alt-color altColor">
 * <td>{@link java.util.Optional#orElseGet(Supplier) orElseGet}</td>
 * <td>{@link Result#orElseMap(Function) orElseMap}</td>
 * </tr>
 * <tr class="row-color rowColor">
 * <td>{@link java.util.Optional#orElseThrow() orElseThrow}</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr class="alt-color altColor">
 * <td>&nbsp;</td>
 * <td>{@link Result#optional() optional}</td>
 * </tr>
 * <tr class="row-color rowColor">
 * <td>{@link java.util.Optional#stream() stream}</td>
 * <td>{@link Result#stream() stream}</td>
 * </tr>
 * <tr class="alt-color altColor">
 * <td>{@link java.util.Optional#ifPresent(Consumer) isPresent}</td>
 * <td>{@link Result#ifSuccess(Consumer) ifSuccess}</td>
 * </tr>
 * <tr class="row-color rowColor">
 * <td>&nbsp;</td>
 * <td>{@link Result#ifFailure(Consumer) ifFailure}</td>
 * </tr>
 * <tr class="alt-color altColor">
 * <td>{@link java.util.Optional#ifPresentOrElse(Consumer, Runnable) ifPresentOrElse}</td>
 * <td>{@link Result#ifSuccessOrElse(Consumer, Consumer) ifSuccessOrElse}</td>
 * </tr>
 * <tr class="row-color rowColor">
 * <td>{@link java.util.Optional#filter(Predicate) filter}</td>
 * <td>{@link Result#filter(Predicate, Function) filter}</td>
 * </tr>
 * <tr class="alt-color altColor">
 * <td>{@link java.util.Optional#map(Function) map}</td>
 * <td>{@link Result#mapSuccess(Function) mapSuccess}</td>
 * </tr>
 * <tr class="row-color rowColor">
 * <td>&nbsp;</td>
 * <td>{@link Result#mapFailure(Function) mapFailure}</td>
 * </tr>
 * <tr class="alt-color altColor">
 * <td>&nbsp;</td>
 * <td>{@link Result#map(Function, Function) map}</td>
 * </tr>
 * <tr class="row-color rowColor">
 * <td>{@link java.util.Optional#flatMap(Function) flatMap}</td>
 * <td>{@link Result#flatMapSuccess(Function) flatMapSuccess}</td>
 * </tr>
 * <tr class="alt-color altColor">
 * <td>{@link java.util.Optional#or(Supplier) or}</td>
 * <td>{@link Result#flatMapFailure(Function) flatMapFailure}</td>
 * </tr>
 * <tr class="row-color rowColor">
 * <td>&nbsp;</td>
 * <td>{@link Result#flatMap(Function, Function) flatMap}</td>
 * </tr>
 * </tbody>
 * </table>
 * </div>
 * <h3>Result Library in a Nutshell</h3>
 * <p>
 * Before {@code Result}, we would wrap exception-throwing {@code foobar} method invocation inside a {@code try} block
 * so that errors can be handled inside a {@code catch} block.
 *
 * <pre class="row-color rowColor">
 * {@code
 * public int getFoobarLength() {
 *     int length;
 *     try {
 *         final String result = foobar();
 *         this.commit(result);
 *         length = result.length();
 *     } catch (SomeException problem) {
 *         this.rollback(problem);
 *         length = -1;
 *     }
 *     return length;
 * }
 * }
 * </pre>
 * <p>
 * This approach is lengthy, and that's not the only problem -- it's also slow. Conventional wisdom says that
 * exceptional logic shouldn't be used for normal program flow. {@code Result} makes us deal with expected,
 * non-exceptional error situations explicitly as a way of enforcing good programming practices.
 * <p>
 * Let's now look at how the above code could be refactored if method {@code foobar} returned a {@code Result} object
 * instead of throwing an exception:
 *
 * <pre class="row-color rowColor">
 * {@code
 * public int getFoobarLength() {
 *     final Result<String, SomeFailure> result = foobar();
 *     result.ifSuccessOrElse(this::commit, this::rollback);
 *     final Result<Integer, SomeFailure> resultLength = result.mapSuccess(String::length);
 *     return resultLength.orElse(-1);
 * }
 * }
 * </pre>
 * <p>
 * In the above example, we use only four lines of code to replace the ten that worked in the first example. But we can
 * make it even shorter by chaining methods in typical functional programming style:
 *
 * <pre class="row-color rowColor">
 * {@code
 * public int getFoobarLength() {
 *     return foobar().ifSuccessOrElse(this::commit, this::rollback).mapSuccess(String::length).orElse(-1);
 * }
 * }
 * </pre>
 * <p>
 * In fact, since we are using {@code -1} here just to signal that the underlying operation failed, we'd be better off
 * returning a {@code Result} object upstream:
 *
 * <pre class="row-color rowColor">
 * {@code
 * public Result<Integer, SomeFailure> getFoobarLength() {
 *     return foobar().ifSuccessOrElse(this::commit, this::rollback).mapSuccess(String::length);
 * }
 * }
 * </pre>
 * <p>
 * This allows others to easily compose operations on top of ours, just like we did with foobar.
 *
 * @author Guillermo Calvo
 * @see Result
 */

package com.leakyabstractions.result;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
