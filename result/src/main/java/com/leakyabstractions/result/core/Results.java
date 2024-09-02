/*
 * Copyright 2024 Guillermo Calvo
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

package com.leakyabstractions.result.core;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import com.leakyabstractions.result.api.Result;

/**
 * This class consists exclusively of static methods that return {@link Result} instances.
 *
 * @author <a href="https://guillermo.dev/">Guillermo Calvo</a>
 * @see com.leakyabstractions.result.core Creating Results
 * @see com.leakyabstractions.result.api.Result
 */
public class Results {

    private Results() {
        /** Suppresses default constructor, ensuring non-instantiability */
    }

    /**
     * Creates a new successful {@link Result}.
     *
     * @param <S> the success type of the {@code Result}
     * @param <F> the failure type of the {@code Result}
     * @param success the success value
     * @return a successful {@code Result} holding {@code success}
     * @throws NullPointerException if {@code success} is {@code null}
     */
    public static <S, F> Result<S, F> success(S success) {
        requireNonNull(success, "success value");
        return new Success<>(success);
    }

    /**
     * Creates a new failed {@link Result}.
     *
     * @param <S> the success type of the {@code Result}
     * @param <F> the failure type of the {@code Result}
     * @param failure the failure value
     * @return a failed {@code Result} holding {@code failure}
     * @throws NullPointerException if {@code failure} is {@code null}
     */
    public static <S, F> Result<S, F> failure(F failure) {
        requireNonNull(failure, "failure value");
        return new Failure<>(failure);
    }

    /**
     * Creates a new {@link Result} based on a possibly-null success value and a non-null failure value.
     *
     * @param <S> the success type of the {@code Result}
     * @param <F> the failure type of the {@code Result}
     * @param success the success value to use if non-null
     * @param failure the failure value to use if {@code success} is {@code null}
     * @return a successful {@code Result} holding {@code success} if non-null; otherwise, a failed {@code Result}
     *     holding {@code failure}
     * @throws NullPointerException if both {@code success} and {@code failure} are {@code null}
     */
    public static <S, F> Result<S, F> ofNullable(S success, F failure) {
        return success != null ? new Success<>(success) : failure(failure);
    }

    /**
     * Creates a new {@link Result} based on a possibly-null success value and a failure {@link Supplier}.
     *
     * @param <S> the success type of the {@code Result}
     * @param <F> the failure type of the {@code Result}
     * @param success the success value to use if non-null
     * @param failureSupplier the {@code Supplier} that produces a failure value
     * @return a successful {@code Result} holding {@code success} if non-null; otherwise, a failed {@code Result}
     *     holding the value produced by {@code failureSupplier}
     * @throws NullPointerException if both {@code success} and {@code failureSupplier} are {@code
     *     null}, or if {@code failureSupplier} returns {@code null}
     */
    public static <S, F> Result<S, F> ofNullable(S success, Supplier<? extends F> failureSupplier) {
        if (success != null) return new Success<>(success);
        requireNonNull(failureSupplier, "failure supplier");
        final F failure = failureSupplier.get();
        requireNonNull(failure, "failure value returned by supplier");
        return new Failure<>(failure);
    }

    /**
     * Creates a new {@link Result} based on an {@link Optional} success value and a non-null failure value.
     *
     * @param <S> the success type of the {@code Result}
     * @param <F> the failure type of the {@code Result}
     * @param success the {@code Optional} success value to use if non-empty
     * @param failure the failure value to use if {@code success} is empty
     * @return a successful {@code Result} holding the {@code Optional} success value if non-empty; otherwise, a failed
     *     {@code Result} holding {@code failure}
     * @throws NullPointerException if {@code success} is {@code null}; or if {@code success} is empty and
     *     {@code failure} is {@code null}
     */
    public static <S, F> Result<S, F> ofOptional(Optional<S> success, F failure) {
        requireNonNull(success, "optional");
        return success.map((Function<S, Result<S, F>>) Success::new).orElseGet(() -> failure(failure));
    }

    /**
     * Creates a new {@link Result} based on an {@link Optional} success value and a failure {@link Supplier}.
     *
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param success the {@code Optional} success value to use if non-empty
     * @param failureSupplier the {@code Supplier} that produces a failure value
     * @return a successful {@code Result} holding the {@code Optional} success value if non-empty; otherwise, a failed
     *     {@code Result} holding the value produced by {@code failureSupplier}
     * @throws NullPointerException if {@code success} is {@code null}; or if {@code success} is empty and
     *     {@code failureSupplier} is {@code null} or returns {@code null}
     */
    public static <S, F> Result<S, F> ofOptional(
            Optional<S> success, Supplier<? extends F> failureSupplier) {
        requireNonNull(success, "optional");
        return success
                .map((Function<S, Result<S, F>>) Success::new)
                .orElseGet(
                        () -> {
                            requireNonNull(failureSupplier, "failure supplier");
                            final F failure = failureSupplier.get();
                            requireNonNull(failure, "failure value returned by supplier");
                            return new Failure<>(failure);
                        });
    }

    /**
     * Creates a new {@link Result} based on a {@link Callable} task.
     *
     * @param <S> the success type of the {@code Result}
     * @param task the {@code Callable} that produces a success value, or throws an {@link Exception} if unable to do so
     * @return a successful {@code Result} holding the value produced by {@code task} if it completed as intended;
     *     otherwise a failed {@code Result} holding the {@code Exception} thrown by {@code task}
     * @throws NullPointerException if {@code task} is {@code null} or returns {@code null}
     */
    public static <S> Result<S, Exception> ofCallable(Callable<? extends S> task) {
        requireNonNull(task, "callable");
        final S success;
        try {
            success = task.call();
        } catch (Exception exception) {
            return new Failure<>(exception);
        }
        requireNonNull(success, "success value returned by callable");
        return new Success<>(success);
    }
}
