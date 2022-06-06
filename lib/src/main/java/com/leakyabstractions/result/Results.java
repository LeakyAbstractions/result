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

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.concurrent.Callable;
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
        requireNonNull(success, "success value");
        return new Success<>(success);
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
        requireNonNull(failure, "failure value");
        return new Failure<>(failure);
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
        return value != null ? new Success<>(value) : failure(failure);
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
     *     {@code failureSupplier} returns {@code null}
     */
    public static <S, F> Result<S, F> ofNullable(S value, Supplier<? extends F> failureSupplier) {
        if (value != null) return new Success<>(value);
        requireNonNull(failureSupplier, "failure supplier");
        final F failure = failureSupplier.get();
        requireNonNull(failure, "failure value returned by supplier");
        return new Failure<>(failure);
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
     *     {@code failure} is {@code null}
     */
    public static <S, F> Result<S, F> ofOptional(Optional<S> optional, F failure) {
        requireNonNull(optional, "optional");
        return optional.map((Function<S, Result<S, F>>) Success::new).orElseGet(() -> failure(failure));
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
     *     {@code failureSupplier} is {@code null}; or if {@code failureSupplier} returns {@code null}
     */
    public static <S, F> Result<S, F> ofOptional(Optional<S> optional, Supplier<? extends F> failureSupplier) {
        requireNonNull(optional, "optional");
        return optional.map((Function<S, Result<S, F>>) Success::new).orElseGet(() -> {
            requireNonNull(failureSupplier, "failure supplier");
            final F failure = failureSupplier.get();
            requireNonNull(failure, "failure value returned by supplier");
            return new Failure<>(failure);
        });
    }

    /**
     * If the given {@code callable} produces a success value, returns a new successful result with it; otherwise
     * returns a new failed result with the exception thrown by {@code callable}.
     *
     * @param <S> the success type of the result
     * @param callable the task that produces a success value, or throws an exception if unable to do so
     * @return the new result
     * @throws NullPointerException if {@code callable} is {@code null}; or if {@code callable} returns {@code null}
     */
    public static <S> Result<S, Exception> ofCallable(Callable<? extends S> callable) {
        requireNonNull(callable, "callable");
        final S success;
        try {
            success = callable.call();
        } catch (Exception exception) {
            return new Failure<>(exception);
        }
        requireNonNull(success, "success value returned by callable");
        return new Success<>(success);
    }
}
