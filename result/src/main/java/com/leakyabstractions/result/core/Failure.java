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

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.leakyabstractions.result.api.Result;

/**
 * Default implementation of a failed {@link Result}.
 *
 * @author Guillermo Calvo
 * @param <S> the type of the success value
 * @param <F> the type of the failure value
 */
final class Failure<S, F> implements Result<S, F> {

    private final F value;

    Failure(F value) {
        this.value = value;
    }

    @Override
    public Optional<S> getSuccess() {
        return Optional.empty();
    }

    @Override
    public Optional<F> getFailure() {
        return Optional.of(this.value);
    }

    @Override
    public boolean hasSuccess() {
        return false;
    }

    @Override
    public boolean hasFailure() {
        return true;
    }

    @Override
    public S orElse(S other) {
        return other;
    }

    @Override
    public S orElseMap(Function<? super F, ? extends S> mapper) {
        requireNonNull(mapper, "mapper"); // NOSONAR
        return mapper.apply(this.value);
    }

    @Override
    public Stream<S> streamSuccess() {
        return Stream.empty();
    }

    @Override
    public Stream<F> streamFailure() {
        return Stream.of(this.value);
    }

    @Override
    public Result<S, F> ifSuccess(Consumer<? super S> action) {
        return this;
    }

    @Override
    public Result<S, F> ifSuccessOrElse(
            Consumer<? super S> successAction, Consumer<? super F> failureAction) {
        requireNonNull(failureAction, "failure action");
        failureAction.accept(this.value);
        return this;
    }

    @Override
    public Result<S, F> ifFailure(Consumer<? super F> action) {
        requireNonNull(action, "action");
        action.accept(this.value);
        return this;
    }

    @Override
    public Result<S, F> filter(
            Predicate<? super S> isAcceptable, Function<? super S, ? extends F> mapper) {
        return this;
    }

    @Override
    public Result<S, F> recover(
            Predicate<? super F> isRecoverable, Function<? super F, ? extends S> mapper) {
        requireNonNull(isRecoverable, "isRecoverable");
        if (!isRecoverable.test(this.value)) return this;
        requireNonNull(mapper, "mapper"); // NOSONAR
        final S success = requireNonNull(mapper.apply(this.value), "success value returned by mapper");
        return new Success<>(success);
    }

    @Override
    public <S2, F2> Result<S2, F2> map(
            Function<? super S, ? extends S2> successMapper,
            Function<? super F, ? extends F2> failureMapper) {
        requireNonNull(failureMapper, "failure mapper");
        final F2 failure = requireNonNull(failureMapper.apply(this.value), "failure value returned by failure mapper");
        return new Failure<>(failure);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S2> Result<S2, F> mapSuccess(Function<? super S, ? extends S2> mapper) {
        return (Result<S2, F>) this;
    }

    @Override
    public <F2> Result<S, F2> mapFailure(Function<? super F, ? extends F2> mapper) {
        requireNonNull(mapper, "mapper"); // NOSONAR
        final F2 failure = requireNonNull(mapper.apply(this.value), "failure value returned by mapper");
        return new Failure<>(failure);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S2, F2> Result<S2, F2> flatMap(
            Function<? super S, ? extends Result<? extends S2, ? extends F2>> successMapper,
            Function<? super F, ? extends Result<? extends S2, ? extends F2>> failureMapper) {
        requireNonNull(failureMapper, "failure mapper");
        final Result<?, ?> result = failureMapper.apply(this.value);
        return (Result<S2, F2>) requireNonNull(result, "result object returned by failure mapper");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S2> Result<S2, F> flatMapSuccess(
            Function<? super S, ? extends Result<? extends S2, ? extends F>> mapper) {
        return (Result<S2, F>) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F2> Result<S, F2> flatMapFailure(
            Function<? super F, ? extends Result<? extends S, ? extends F2>> mapper) {
        requireNonNull(mapper, "mapper"); // NOSONAR
        final Result<?, ?> result = requireNonNull(mapper.apply(this.value), "result object returned by mapper");
        return (Result<S, F2>) result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Failure)) return false;
        return Objects.equals(this.value, ((Failure<?, ?>) obj).value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public String toString() {
        return "Failure[" + this.value + "]";
    }
}
