
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
 */
public class Results {

    private Results() {
        /** Suppresses default constructor, ensuring non-instantiability */
    }

    /**
     * Create a new successful result with a given value.
     * 
     * @param <S> the type of the success value
     * @param <F> the type of the failure value
     * @param success the value to be contained; may be {@code null}
     * @return the new successful result
     */
    public static <S, F> Result<S, F> success(S success) {
        return new Success<>(success);
    }

    /**
     * Create a new failed result with a given value.
     * 
     * @param <S> the type of the success value
     * @param <F> the type of the failure value
     * @param failure the value to be contained; may be {@code null}
     * @return the new failed result
     */
    public static <S, F> Result<S, F> failure(F failure) {
        return new Failure<>(failure);
    }

    /**
     * Create a new result based on a possibly {@code null} value.
     * <p>
     * If the given value is not {@code null} then a new successful result containing the value will be created;
     * otherwise a new failed result will be created.
     * 
     * @param <S> the type of the success value
     * @param value the value to be contained; may be {@code null}
     * @return the new result
     */
    public static <S> Result<S, Void> ofNullable(S value) {
        return value != null ? new Success<>(value) : new Failure<>(null);
    }

    /**
     * Create a new result based on a possibly {@code null} value and a function that supplies failure values.
     * <p>
     * If the given value is not {@code null} then a new successful result containing the value will be created;
     * otherwise a new failed result containing the value produced by the {@link Supplier} will be created.
     * 
     * @param <S> the type of the success value
     * @param <F> the type of the failure value
     * @param value the value to be contained if not {@code null}
     * @param failureSupplier a {@link Supplier} that will generate the failure value if {@code value} is {@code null}
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
     * Create a new result based on a possibly empty {@link Optional}.
     * <p>
     * If the given optional is not empty then a new successful result containing the value will be created; otherwise a
     * new failed result will be created.
     * 
     * @param <S> the type of the success value
     * @param value the optional whose value is to be contained; may be empty
     * @return the new result
     */
    public static <S> Result<S, Void> ofOptional(Optional<S> value) {
        if (value.isPresent()) {
            return new Success<>(value.get());
        }
        return new Failure<>(null);
    }

    /**
     * Create a new result based on a possibly empty {@link Optional} and a function that supplies failure values.
     * <p>
     * If the given optional is not empty then a new successful result containing the value will be created; otherwise a
     * new failed result containing the value produced by the {@link Supplier} will be created.
     * 
     * @param <S> the type of the success value
     * @param <F> the type of the failure value
     * @param value the value to be contained if not {@code null}
     * @param failureSupplier a {@link Supplier} that will generate the failure value if {@code value} is empty
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
     * Create a new result based on a {@link Callable}.
     * <p>
     * If the given {@code Callable} completes then a new successful result containing the produced value will be
     * created; otherwise a new failed result containing the thrown exception will be created.
     * 
     * @param <S> the type of the success value
     * @param callable the {@code Callable} to produce the success value, may throw an exception
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
     * Create a new result based on a {@link Callable} and a mapping function that generates failure values.
     * <p>
     * If the given {code Callable} completes then a new successful result containing the produced value will be
     * created; otherwise a new failed result containing the thrown exception will be created.
     * 
     * @param <S> the type of the success value
     * @param <F> the type of the failure value
     * @param callable the {code Callable} to produce the success value, may throw an exception
     * @param exceptionMapper a mapping {@link Function} that will generate the failure value if the {@code Callable}
     *            throws an exception
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
     * Create a new result based on a {@link Collection} of {@link Result} objects.
     * <p>
     * If there's any failed result inside the collection then a new failed result containing a stream of failure values
     * will be created; otherwise a new successful result containing a stream of success values will be created.
     * 
     * @param <S> the type of the success value
     * @param <F> the type of the failure value
     * @param results a {@link Collection} of {@link Result} objects, may be empty.
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
     * Create a new result based on a variable list of {@link Result} objects.
     * <p>
     * If there's any failed result inside the variable list then a new failed result containing a stream of failure
     * values will be created; otherwise a new successful result containing a stream of success values will be created.
     * 
     * @param <S> the type of the success value
     * @param <F> the type of the failure value
     * @param results a variable list of {@link Result} objects, may be empty.
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
     * Return an {@link Optional} object with the success value if the given result is successful and the success value
     * is not {@code null}; otherwise return an empty {@code Optional}.
     * 
     * @param <S> the type of the success value
     * @param result the result whose success value is to be
     * @return an {@code Optional} object with the success value if the given result is successful and the success value
     *         is not {@code null}; otherwise an empty {@code Optional}
     */
    public static <S> Optional<S> toOptional(Result<S, ?> result) {
        return result.isSuccess() ? Optional.ofNullable(result.orElseThrow()) : Optional.empty();
    }

    /**
     * Return an {@link Optional} object with the failure value if the given result is failed and the failure value is
     * not {@code null}; otherwise return an empty {@code Optional}.
     * 
     * @param <F> the type of the failure value
     * @param result the result whose failure value is to be
     * @return an {@code Optional} object with the failure value if the given result is failed and the failure value is
     *         not {@code null}; otherwise an empty {@code Optional}
     */
    public static <F> Optional<F> toOptionalFailure(Result<?, F> result) {
        return result.isFailure() ? Optional.ofNullable(result.getFailureOrElseThrow()) : Optional.empty();
    }

    /**
     * Create a new lazy result based on the given result supplier.
     * <p>
     * Lazy results can be manipulated just like any other result; they will try to defer the invocation of the given
     * supplier as long as possible. The purpose is to encapsulate an expensive operation that may be omitted if there's
     * no actual need to examine the result.
     * <p>
     * These results can be lazily <em>filtered</em> and <em>transformed</em> without actually performing the expensive
     * operation:
     * <ul>
     * <li>{@link Result#filter(java.util.function.Predicate, Function)}</li>
     * <li>{@link Result#map(Function, Function)}</li>
     * <li>{@link Result#mapSuccess(Function)}</li>
     * <li>{@link Result#mapFailure(Function)}</li>
     * <li>{@link Result#flatMap(Function, Function)}</li>
     * <li>{@link Result#flatMapSuccess(Function)}</li>
     * <li>{@link Result#flatMapFailure(Function)}</li>
     * </ul>
     * <p>
     * On the other hand, the supplier will be invoked if any of these <em>terminal operations</em> is performed on a
     * lazy result:
     * <ul>
     * <li>{@link Result#isSuccess()}</li>
     * <li>{@link Result#isFailure()}</li>
     * <li>{@link Result#orElse(Object)}</li>
     * <li>{@link Result#orElseMap(Function)}</li>
     * <li>{@link Result#orElseThrow()}</li>
     * <li>{@link Result#orElseThrow(Function)}</li>
     * <li>{@link Result#getFailureOrElseThrow()}</li>
     * </ul>
     * <p>
     * Finally, conditional actions can be performed lazily if they are {@link LazyConsumer} objects; otherwise they
     * will be performed immediately:
     * <ul>
     * <li>{@link Result#ifSuccess(Consumer)}</li>
     * <li>{@link Result#ifSuccessOrElse(Consumer, Consumer)}</li>
     * <li>{@link Result#ifFailure(Consumer)}</li>
     * </ul>
     * <p>
     * Once a lazy result retrieves the supplied result, all future operations will be performed immediately and the
     * returned Result objects might not be lazy.
     * <p>
     * The supplier is guaranteed to be invoked at most once. It must return a non-null result object; if it throws an
     * exception or returns {@code null} then the behavior of the lazy result will be undefined.
     * 
     * @param <S> the type of the success value
     * @param <F> the type of the failure value
     * @param supplier the function that will eventually supply the actual result
     * @return the new lazy result
     * @see Results#lazy(Consumer)
     */
    public static <S, F> Result<S, F> lazy(Supplier<Result<S, F>> supplier) {
        return new LazyResult<>(supplier);
    }

    /**
     * Creates a new lazy consumer based on a regular one.
     * <p>
     * Lazy consumers are intended to be passed as parameters to {@link Result#ifSuccess(Consumer)},
     * {@link Result#ifSuccessOrElse(Consumer, Consumer)} and {@link Result#ifFailure(Consumer)} when the action only
     * needs to be performed if the lazy result eventually retrieves an actual result from its supplier.
     * 
     * @param <T> the type of the input to the action
     * @param consumer the regular consumer that may be eventually performed
     * @return the new lazy consumer
     * @see LazyConsumer
     */
    public static <T> LazyConsumer<T> lazy(Consumer<T> consumer) {
        return Objects.requireNonNull(consumer)::accept;
    }
}
