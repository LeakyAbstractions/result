
package com.leakyabstractions.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
        this.supplier = Objects.requireNonNull(supplier);
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
    public S orElseMap(Function<? super F, ? extends S> failureMapper) {
        return this.getBackingResult().orElseMap(failureMapper);
    }

    @Override
    public S orElseThrow() {
        return this.getBackingResult().orElseThrow();
    }

    @Override
    public <E extends Throwable> S orElseThrow(Function<? super F, E> failureMapper) throws E {
        return this.getBackingResult().orElseThrow(failureMapper);
    }

    @Override
    public F getFailureOrElseThrow() {
        return this.getBackingResult().getFailureOrElseThrow();
    }

    @Override
    public Result<S, F> ifSuccess(Consumer<? super S> successAction) {
        return lazily(this.isLazy && successAction instanceof LazyConsumer,
                () -> this.getBackingResult().ifSuccess(successAction));
    }

    @Override
    public Result<S, F> ifSuccessOrElse(Consumer<? super S> successAction, Consumer<? super F> failureAction) {
        return lazily(this.isLazy && successAction instanceof LazyConsumer && failureAction instanceof LazyConsumer,
                () -> this.getBackingResult().ifSuccessOrElse(successAction, failureAction));
    }

    @Override
    public Result<S, F> ifFailure(Consumer<? super F> failureAction) {
        return lazily(this.isLazy && failureAction instanceof LazyConsumer,
                () -> this.getBackingResult().ifFailure(failureAction));
    }

    @Override
    public Result<S, F> filter(Predicate<? super S> predicate, Function<? super S, ? extends F> failureMapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().filter(predicate, failureMapper));
    }

    @Override
    public <S2, F2> Result<S2, F2> map(Function<? super S, S2> successMapper, Function<? super F, F2> failureMapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().map(successMapper, failureMapper));
    }

    @Override
    public <S2> Result<S2, F> mapSuccess(Function<? super S, S2> successMapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().mapSuccess(successMapper));
    }

    @Override
    public <F2> Result<S, F2> mapFailure(Function<? super F, F2> failureMapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().mapFailure(failureMapper));
    }

    @Override
    public <S2, F2> Result<S2, F2> flatMap(
            Function<? super S, Result<S2, F2>> successFlatMapper,
            Function<? super F, Result<S2, F2>> failureFlatMapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().flatMap(successFlatMapper, failureFlatMapper));
    }

    @Override
    public <S2> Result<S2, F> flatMapSuccess(Function<? super S, Result<S2, F>> successFlatMapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().flatMapSuccess(successFlatMapper));
    }

    @Override
    public <F2> Result<S, F2> flatMapFailure(Function<? super F, Result<S, F2>> failureFlatMapper) {
        return lazily(this.isLazy, () -> this.getBackingResult().flatMapFailure(failureFlatMapper));
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
        this.backingResult = Objects.requireNonNull(this.supplier.get());
    }

    private static <S2, F2> Result<S2, F2> lazily(boolean lazily, Supplier<Result<S2, F2>> supplier) {
        return lazily ? new LazyResult<>(supplier) : supplier.get();
    }
}
