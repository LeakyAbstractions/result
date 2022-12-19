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

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Default implementation of a successful {@link Result}.
 *
 * @author Guillermo Calvo
 * @param <S> the type of the success value
 * @param <F> the type of the failure value
 */
final class Success<S, F> implements Result<S, F> {

    private final S value;

    Success(S value) {
        this.value = value;
    }

    @Override
    public Optional<S> getSuccess() {
        return Optional.of(this.value);
    }

    @Override
    public Optional<F> getFailure() {
        return Optional.empty();
    }

    @Override
    public boolean hasSuccess() {
        return true;
    }

    @Override
    public boolean hasFailure() {
        return false;
    }

    @Override
    public S orElse(S other) {
        return this.value;
    }

    @Override
    public S orElseMap(Function<? super F, ? extends S> mapper) {
        return this.value;
    }

    @Override
    public Stream<S> streamSuccess() {
        return Stream.of(this.value);
    }

    @Override
    public Stream<F> streamFailure() {
        return Stream.empty();
    }

    @Override
    public Result<S, F> ifSuccess(Consumer<? super S> action) {
        requireNonNull(action, "action");
        action.accept(this.value);
        return this;
    }

    @Override
    public Result<S, F> ifSuccessOrElse(
            Consumer<? super S> successAction, Consumer<? super F> failureAction) {
        requireNonNull(successAction, "success action");
        successAction.accept(this.value);
        return this;
    }

    @Override
    public Result<S, F> ifFailure(Consumer<? super F> action) {
        return this;
    }

    @Override
    public Result<S, F> filter(
            Predicate<? super S> isAcceptable, Function<? super S, ? extends F> mapper) {
        requireNonNull(isAcceptable, "isAcceptable");
        if (isAcceptable.test(this.value)) return this;
        requireNonNull(mapper, "mapper"); // NOSONAR
        final F failure = requireNonNull(mapper.apply(this.value), "failure value returned by mapper");
        return new Failure<>(failure);
    }

    @Override
    public Result<S, F> recover(
            Predicate<? super F> isRecoverable, Function<? super F, ? extends S> mapper) {
        return this;
    }

    @Override
    public <S2, F2> Result<S2, F2> map(
            Function<? super S, ? extends S2> successMapper,
            Function<? super F, ? extends F2> failureMapper) {
        requireNonNull(successMapper, "success mapper");
        final S2 success = requireNonNull(successMapper.apply(this.value), "success value returned by success mapper");
        return new Success<>(success);
    }

    @Override
    public <S2> Result<S2, F> mapSuccess(Function<? super S, ? extends S2> mapper) {
        requireNonNull(mapper, "mapper"); // NOSONAR
        final S2 success = requireNonNull(mapper.apply(this.value), "success value returned by mapper");
        return new Success<>(success);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F2> Result<S, F2> mapFailure(Function<? super F, ? extends F2> mapper) {
        return (Result<S, F2>) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S2, F2> Result<S2, F2> flatMap(
            Function<? super S, ? extends Result<? extends S2, ? extends F2>> successMapper,
            Function<? super F, ? extends Result<? extends S2, ? extends F2>> failureMapper) {
        requireNonNull(successMapper, "success mapper");
        final Result<?, ?> result = successMapper.apply(this.value);
        return (Result<S2, F2>) requireNonNull(result, "result object returned by success mapper");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S2> Result<S2, F> flatMapSuccess(
            Function<? super S, ? extends Result<? extends S2, ? extends F>> mapper) {
        requireNonNull(mapper, "mapper"); // NOSONAR
        final Result<?, ?> result = requireNonNull(mapper.apply(this.value), "result object returned by mapper");
        return (Result<S2, F>) result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F2> Result<S, F2> flatMapFailure(
            Function<? super F, ? extends Result<? extends S, ? extends F2>> mapper) {
        return (Result<S, F2>) this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Success)) return false;
        return Objects.equals(this.value, ((Success<?, ?>) obj).value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public String toString() {
        return new StringBuilder("Success[").append(this.value).append("]").toString();
    }
}
