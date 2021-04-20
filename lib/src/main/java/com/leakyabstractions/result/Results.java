
package com.leakyabstractions.result;

import static java.util.Arrays.stream;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This class consists exclusively of static methods that operate on or return {@link Result} instances.
 *
 * @author Guillermo Calvo
 * @see com.leakyabstractions.result
 */
public class Results {

    private Results() {
        /** Suppresses default constructor, ensuring non-instantiability */
    }

    /**
     * Creates a new successful result with a given value.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param success the possibly-{@code null} success value
     * @return the new successful result
     */
    public static <S, F> Result<S, F> success(S success) {
        return new Success<>(success);
    }

    /**
     * Creates a new failed result with a given value.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param failure the possibly-{@code null} failure value
     * @return the new failed result
     */
    public static <S, F> Result<S, F> failure(F failure) {
        return new Failure<>(failure);
    }

    /**
     * If the given {@code value} is not {@code null}, returns a new successful result with it; otherwise returns a new
     * failed result.
     *
     * @param <S> the success type of the result
     * @param value the value to check if {@code null}
     * @return the new result
     */
    public static <S> Result<S, Void> ofNullable(S value) {
        return value != null ? new Success<>(value) : new Failure<>(null);
    }

    /**
     * If the given {@code value} is not {@code null}, returns a new successful result with it; otherwise returns a new
     * failed result with a value produced by the given supplier function.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param value the value to check if {@code null}
     * @param failureSupplier the supplier function that produces a failure value
     * @return the new result
     */
    public static <S, F> Result<S, F> ofNullable(S value, Supplier<? extends F> failureSupplier) {
        if (value != null) {
            return new Success<>(value);
        }
        Objects.requireNonNull(failureSupplier);
        return new Failure<>(failureSupplier.get());
    }

    /**
     * If the given {@code optional} is not empty, returns a new successful result with its value; otherwise returns a
     * new failed result.
     *
     * @param <S> the success type of the result
     * @param optional the optional to check if empty
     * @return the new result
     */
    public static <S> Result<S, Void> ofOptional(Optional<S> optional) {
        if (optional.isPresent()) {
            return new Success<>(optional.get());
        }
        return new Failure<>(null);
    }

    /**
     * If the given {@code optional} is not empty, returns a new successful result with its value; otherwise returns a
     * new failed result with a value produced by the given supplier function.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param value the optional to check if empty
     * @param failureSupplier the supplier function that produces a failure value
     * @return the new result
     */
    public static <S, F> Result<S, F> ofOptional(Optional<S> value, Supplier<? extends F> failureSupplier) {
        if (value.isPresent()) {
            return new Success<>(value.get());
        }
        Objects.requireNonNull(failureSupplier);
        return new Failure<>(failureSupplier.get());
    }

    /**
     * If the given {@code callable} produces a value, return a new successful result with it; otherwise returns a new
     * failed result with the exception thrown by {@code callable}.
     *
     * @param <S> the success type of the result
     * @param callable the task that produces a success value, or throws an exception if unable to do so
     * @return the new result
     */
    public static <S> Result<S, Exception> wrap(Callable<S> callable) {
        Objects.requireNonNull(callable);
        try {
            return new Success<>(callable.call());
        } catch (Exception exception) {
            return new Failure<>(exception);
        }
    }

    /**
     * If the given {@code callable} produces a value, return a new successful result with it; otherwise returns a new
     * failed result with a value produced by the given mapping function.
     * <p>
     * The mapping function will be applied to the exception thrown by {@code callable} to produce the
     * possibly-{@code null} failure value.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param callable the task that produces a success value, or throws an exception if unable to do so
     * @param exceptionMapper the mapping function that produces a failure value
     * @return the new result
     */
    public static <S, F> Result<S, F> wrap(Callable<S> callable, Function<? super Exception, F> exceptionMapper) {
        Objects.requireNonNull(callable);
        try {
            return new Success<>(callable.call());
        } catch (Exception exception) {
            Objects.requireNonNull(exceptionMapper);
            return new Failure<>(exceptionMapper.apply(exception));
        }
    }

    /**
     * Combine a collection of results into a new one.
     * <p>
     * If there's any failed results inside the collection, then a new failed result containing a stream of failure
     * values will be created; otherwise a new successful result containing a stream of success values will be created.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param results a possibly-empty {@link Collection collection} of results
     * @return the new result
     */
    public static <S, F> Result<Stream<S>, Stream<F>> combine(Collection<Result<S, F>> results) {
        Objects.requireNonNull(results);
        if (results.stream().anyMatch(Result::isFailure)) {
            return new Failure<>(results.stream().filter(Result::isFailure).map(Result::getFailureOrElseThrow));
        }
        return new Success<>(results.stream().filter(Result::isSuccess).map(Result::orElseThrow));
    }

    /**
     * Combine a variable list of results into a new one.
     * <p>
     * If there's any failed results inside the variable list, then a new failed result containing a stream of failure
     * values will be created; otherwise a new successful result containing a stream of success values will be created.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param results a variable list of results
     * @return the new result
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <S, F> Result<Stream<S>, Stream<F>> combine(Result<? extends S, ? extends F>... results) {
        Objects.requireNonNull(results);
        if (stream(results).anyMatch(Result::isFailure)) {
            return new Failure<>(stream(results).filter(Result::isFailure).map(Result::getFailureOrElseThrow));
        }
        return new Success<>(stream(results).filter(Result::isSuccess).map(Result::orElseThrow));
    }

    /**
     * If the given result is successful and its success value is not {@code null}, returns an optional with it;
     * otherwise returns an empty optional.
     *
     * @param <S> the type of the optional value
     * @param result the result to check if successful
     * @return an optional containing the success value if the given result is successful and the success value is not
     *         {@code null}; otherwise an empty optional
     */
    public static <S> Optional<S> toOptional(Result<S, ?> result) {
        return result.isSuccess() ? Optional.ofNullable(result.orElseThrow()) : Optional.empty();
    }

    /**
     * If the given result is failed and its failure value is not {@code null}, returns an optional with it; otherwise
     * returns an empty optional.
     *
     * @param <F> the type of the optional value
     * @param result the result to check if failed
     * @return an optional containing the failure value if the given result is failed and the failure value is not
     *         {@code null}; otherwise an empty optional
     */
    public static <F> Optional<F> toOptionalFailure(Result<?, F> result) {
        return result.isFailure() ? Optional.ofNullable(result.getFailureOrElseThrow()) : Optional.empty();
    }

    /**
     * Creates a new lazy result based on the given result supplier.
     * <p>
     * Lazy results can be manipulated just like any other result; they will try to defer the invocation of the given
     * supplier as long as possible. The purpose is to encapsulate an expensive operation that may be omitted if there's
     * no actual need to examine the result.
     * <p>
     * Lazy results can be <em>filtered</em> and <em>transformed</em> without actually performing the expensive
     * operation:
     * <ul>
     * <li>{@link Result#filter filter}</li>
     * <li>{@link Result#map map}</li>
     * <li>{@link Result#mapSuccess mapSuccess}</li>
     * <li>{@link Result#mapFailure mapFailure}</li>
     * <li>{@link Result#flatMap flatMap}</li>
     * <li>{@link Result#flatMapSuccess flatMapSuccess}</li>
     * <li>{@link Result#flatMapFailure flatMapFailure}</li>
     * </ul>
     * <p>
     * On the other hand, the supplier will be invoked if any of these <em>terminal operations</em> is performed on a
     * lazy result:
     * <ul>
     * <li>{@link Result#isSuccess isSuccess}</li>
     * <li>{@link Result#isFailure isFailure}</li>
     * <li>{@link Result#orElse orElse}</li>
     * <li>{@link Result#orElseMap orElseMap}</li>
     * <li>{@link Result#orElseThrow() orElseThrow}</li>
     * <li>{@link Result#orElseThrow(Function) orElseThrow(Function)}</li>
     * <li>{@link Result#getFailureOrElseThrow getFailureOrElseThrow}</li>
     * </ul>
     * <p>
     * Finally, conditional actions will be performed immediately unless they are {@link #lazy(Consumer) lazy} too:
     * <ul>
     * <li>{@link Result#ifSuccess ifSuccess}</li>
     * <li>{@link Result#ifSuccessOrElse ifSuccessOrElse}</li>
     * <li>{@link Result#ifFailure ifFailure}</li>
     * </ul>
     * <p>
     * Once a lazy result retrieves the supplied result, all future operations will be performed immediately and the
     * returned Result objects should not be lazy.
     * <p>
     * The supplier is guaranteed to be invoked at most once. It must return a non-null result object; if it throws an
     * exception or returns {@code null} then the behavior of the lazy result will be undefined.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param supplier the function that supplies the actual result
     * @return the new lazy result
     * @see #lazy(Consumer) lazy(Consumer)
     */
    public static <S, F> Result<S, F> lazy(Supplier<Result<S, F>> supplier) {
        return new LazyResult<>(supplier);
    }

    /**
     * Creates a new lazy consumer based on a regular one.
     * <p>
     * Lazy consumers encapsulate actions that depend on success or failure and can be safely deferred or even
     * completely ignored if a lazy result is never evaluated. They are intended to be passed as parameters to:
     * <ul>
     * <li>{@link Result#ifSuccess ifSuccess}</li>
     * <li>{@link Result#ifSuccessOrElse ifSuccessOrElse}</li>
     * <li>{@link Result#ifFailure ifFailure}</li>
     * </ul>
     * <p>
     * These actions will execute immediately if passed to non-lazy results.
     *
     * @param <T> the type of the input to the action
     * @param consumer the action to be applied to this result's success value
     * @return the new lazy consumer
     * @see #lazy(Supplier) lazy(Supplier)
     */
    public static <T> Consumer<T> lazy(Consumer<T> consumer) {
        return LazyConsumer.of(consumer);
    }
}
