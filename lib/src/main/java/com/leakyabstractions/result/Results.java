
package com.leakyabstractions.result;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
     * @param success the success value
     * @return the new successful result
     * @throws NullPointerException if {@code success} is {@code null}
     */
    public static <S, F> Result<S, F> success(S success) {
        return new Success<>(requireNonNull(success));
    }

    /**
     * Creates a new failed result with a given value.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param failure the failure value
     * @return the new failed result
     * @throws NullPointerException if {@code failure} is {@code null}
     */
    public static <S, F> Result<S, F> failure(F failure) {
        return new Failure<>(requireNonNull(failure));
    }

    /**
     * If the given {@code value} is not {@code null}, returns a new successful result with it; otherwise returns a new
     * failed result with {@code failure}.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param value the value to check if {@code null}
     * @param failure the failure value to use in case {@code value} is {@code null}
     * @return the new result
     * @throws NullPointerException if both {@code value} and {@code failure} are {@code null}
     */
    public static <S, F> Result<S, F> ofNullable(S value, F failure) {
        return value != null ? new Success<>(value) : new Failure<>(requireNonNull(failure));
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
     * @throws NullPointerException if both {@code value} and {@code failureSupplier} are {@code null}, or if
     *             {@code failureSupplier} returns {@code null}
     */
    public static <S, F> Result<S, F> ofNullable(S value, Supplier<? extends F> failureSupplier) {
        return value != null ? new Success<>(value) : new Failure<>(requireNonNull(failureSupplier.get()));
    }

    /**
     * If the given {@code optional} is not empty, returns a new successful result with its value; otherwise returns a
     * new failed result with {@code failure}.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param optional the optional to check if empty
     * @param failure the failure value to use in case {@code optional} is empty
     * @return the new result
     * @throws NullPointerException if {@code optional} is {@code null}; or if {@code optional} is empty and
     *             {@code failure} is {@code null}
     */
    public static <S, F> Result<S, F> ofOptional(Optional<S> optional, F failure) {
        return optional.isPresent() ? new Success<>(optional.get()) : new Failure<>(requireNonNull(failure));
    }

    /**
     * If the given {@code optional} is not empty, returns a new successful result with its value; otherwise returns a
     * new failed result with a value produced by the given supplier function.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param optional the optional to check if empty
     * @param failureSupplier the supplier function that produces a failure value
     * @return the new result
     * @throws NullPointerException if {@code optional} is {@code null}; or if {@code optional} is empty and
     *             {@code failureSupplier} is {@code null}; or if {@code failureSupplier} returns {@code null}
     */
    public static <S, F> Result<S, F> ofOptional(Optional<S> optional, Supplier<? extends F> failureSupplier) {
        return optional.isPresent() ? new Success<>(optional.get())
                : new Failure<>(requireNonNull(failureSupplier.get()));
    }

    /**
     * If the given {@code callable} produces a success value, returns a new successful result with it; otherwise
     * returns a new failed result with the exception thrown by {@code callable}.
     *
     * @apiNote If {@code callable} returns {@code null} then a new failed result with a {@link NullPointerException}
     *          will be returned.
     * @param <S> the success type of the result
     * @param callable the task that produces a success value, or throws an exception if unable to do so
     * @return the new result
     * @throws NullPointerException if {@code callable} is {@code null}
     */
    public static <S> Result<S, Exception> wrap(Callable<S> callable) {
        Objects.requireNonNull(callable);
        try {
            return new Success<>(requireNonNull(callable.call()));
        } catch (Exception exception) {
            return new Failure<>(exception);
        }
    }

    /**
     * If the given {@code callable} produces a success value, returns a new successful result with it; otherwise
     * returns a new failed result with a value produced by the given mapping function.
     * <p>
     * The mapping function will be applied to the exception thrown by {@code callable} to produce the failure value.
     *
     * @apiNote If {@code callable} returns {@code null} then the mapping function will be applied to an instance of
     *          {@link NullPointerException}.
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param callable the task that produces a success value, or throws an exception if unable to do so
     * @param exceptionMapper the mapping function that produces a failure value
     * @return the new result
     * @throws NullPointerException if {@code callable} is {@code null}, or if {@code callable} throws an exception and
     *             {@code exceptionMapper} is {@code null}, or if {@code exceptionMapper} returns {@code null}
     */
    public static <S, F> Result<S, F> wrap(Callable<S> callable, Function<? super Exception, F> exceptionMapper) {
        Objects.requireNonNull(callable);
        try {
            return new Success<>(requireNonNull(callable.call()));
        } catch (Exception exception) {
            return new Failure<>(requireNonNull(exceptionMapper.apply(exception)));
        }
    }

    /**
     * If the given result is successful and its success value is not {@code null}, returns an optional with it;
     * otherwise returns an empty optional.
     *
     * @param <S> the type of the optional value
     * @param result the result to check if successful
     * @return an optional containing the success value if the given result is successful; otherwise an empty optional
     * @throws NullPointerException if {@code result} is {@code null}
     */
    public static <S> Optional<S> toOptional(Result<S, ?> result) {
        return result.isSuccess() ? Optional.of(result.orElseThrow()) : Optional.empty();
    }

    /**
     * If the given result is failed and its failure value is not {@code null}, returns an optional with it; otherwise
     * returns an empty optional.
     *
     * @param <F> the type of the optional value
     * @param result the result to check if failed
     * @return an optional containing the failure value if the given result is failed; otherwise an empty optional
     * @throws NullPointerException if {@code result} is {@code null}
     */
    public static <F> Optional<F> toOptionalFailure(Result<?, F> result) {
        return result.isFailure() ? Optional.of(result.getFailureOrElseThrow()) : Optional.empty();
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
     * <li>{@link Result#stream stream}</li>
     * <li>{@link Result#streamFailure streamFailure}</li>
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
     *
     * @apiNote The supplier is guaranteed to be invoked at most once. If it throws an exception or returns {@code null}
     *          then the behavior of the lazy result will be undefined.
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param supplier the function that supplies the actual result
     * @return the new lazy result
     * @throws NullPointerException if {@code supplier} is {@code null}
     * @see #lazy(Consumer) lazy(Consumer)
     */
    public static <S, F> Result<S, F> lazy(Supplier<Result<S, F>> supplier) {
        return new LazyResult<>(requireNonNull(supplier));
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
