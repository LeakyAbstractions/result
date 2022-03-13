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
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Lazy implementation of a {@link Result}.
 *
 * @author Guillermo Calvo
 * @param <S> the type of the success value
 * @param <F> the type of the failure value
 */
final class LazyResult<S, F> implements Result<S, F> {

    private final Supplier<Result<S, F>> supplier;
    private volatile boolean isLazy = true;
    private Result<S, F> backingResult;

    LazyResult(Supplier<Result<S, F>> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean isSuccess() {
        return this.getBackingResult().isSuccess();
    }

    @Override
    public boolean isFailure() {
        return this.getBackingResult().isFailure();
    }

    @Override
    public S orElse(S other) {
        return this.getBackingResult().orElse(other);
    }

    @Override
    public S orElseMap(Function<? super F, ? extends S> mapper) {
        return this.getBackingResult().orElseMap(mapper);
    }

    @Override
    public Stream<S> stream() {
        return this.getBackingResult().stream();
    }

    @Override
    public Stream<F> streamFailure() {
        return this.getBackingResult().streamFailure();
    }

    @Override
    public Optional<S> optional() {
        return this.getBackingResult().optional();
    }

    @Override
    public Optional<F> optionalFailure() {
        return this.getBackingResult().optionalFailure();
    }

    @Override
    public Result<S, F> ifSuccess(Consumer<? super S> action) {
        return lazily(this.isLazy && action instanceof LazyConsumer,
                () -> this.getBackingResult().ifSuccess(action));
    }

    @Override
    public Result<S, F> ifSuccessOrElse(Consumer<? super S> successAction, Consumer<? super F> failureAction) {
        return lazily(this.isLazy && successAction instanceof LazyConsumer && failureAction instanceof LazyConsumer,
                () -> this.getBackingResult().ifSuccessOrElse(successAction, failureAction));
    }

    @Override
    public Result<S, F> ifFailure(Consumer<? super F> action) {
        return lazily(this.isLazy && action instanceof LazyConsumer,
                () -> this.getBackingResult().ifFailure(action));
    }

    @Override
    public Result<S, F> filter(Predicate<? super S> predicate, Function<? super S, ? extends F> mapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().filter(predicate, mapper));
    }

    @Override
    public <S2, F2> Result<S2, F2> map(
            Function<? super S, ? extends S2> successMapper,
            Function<? super F, ? extends F2> failureMapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().map(successMapper, failureMapper));
    }

    @Override
    public <S2> Result<S2, F> mapSuccess(Function<? super S, ? extends S2> mapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().mapSuccess(mapper));
    }

    @Override
    public <F2> Result<S, F2> mapFailure(Function<? super F, ? extends F2> mapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().mapFailure(mapper));
    }

    @Override
    public <S2, F2> Result<S2, F2> flatMap(
            Function<? super S, ? extends Result<? extends S2, ? extends F2>> successMapper,
            Function<? super F, ? extends Result<? extends S2, ? extends F2>> failureMapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().flatMap(successMapper, failureMapper));
    }

    @Override
    public <S2> Result<S2, F> flatMapSuccess(Function<? super S, ? extends Result<? extends S2, ? extends F>> mapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().flatMapSuccess(mapper));
    }

    @Override
    public <F2> Result<S, F2> flatMapFailure(Function<? super F, ? extends Result<? extends S, ? extends F2>> mapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().flatMapFailure(mapper));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LazyResult)) return false;
        return Objects.equals(this.supplier, ((LazyResult<?, ?>) obj).supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.isLazy ? this.supplier : this.backingResult);
    }

    @Override
    public String toString() {
        return String.format("lazy-result[%s]", this.isLazy ? this.supplier : this.backingResult);
    }

    private Result<S, F> getBackingResult() {
        if (this.isLazy) {
            synchronized (this) {
                // Double-checked locking, functional style
                Optional.of(this.isLazy).filter(Boolean::booleanValue).ifPresent(this::supply);
            }
        }
        return this.backingResult;
    }

    private void supply(Boolean ignore) {
        this.isLazy = false;
        this.backingResult = requireNonNull(this.supplier.get());
    }

    private static <S2, F2> Result<S2, F2> lazily(boolean lazily, Supplier<Result<S2, F2>> supplier) {
        return lazily ? new LazyResult<>(supplier) : supplier.get();
    }
}
