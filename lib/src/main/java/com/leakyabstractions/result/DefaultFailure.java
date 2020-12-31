
package com.leakyabstractions.result;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Default implementation of a failed {@link Result}.
 * 
 * @author Guillermo Calvo
 * @param <S> the type of the success value
 * @param <F> the type of the failure value
 */
final class DefaultFailure<S, F> implements Result<S, F> {

    private final F value;

    DefaultFailure(F value) {
        this.value = value;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public S orElse(S other) {
        return other;
    }

    @Override
    public S orElseMap(Function<? super F, ? extends S> failureMapper) {
        Objects.requireNonNull(failureMapper);
        return failureMapper.apply(this.value);
    }

    @Override
    public S orElseThrow() {
        throw new NoSuchElementException("Not a successful result");
    }

    @Override
    public <E extends Throwable> S orElseThrow(Function<? super F, E> failureMapper) throws E {
        Objects.requireNonNull(failureMapper);
        throw failureMapper.apply(this.value);
    }

    @Override
    public F getFailureOrElseThrow() {
        return this.value;
    }

    @Override
    public Result<S, F> ifSuccess(Consumer<? super S> successAction) {
        return this;
    }

    @Override
    public Result<S, F> ifSuccessOrElse(Consumer<? super S> successAction, Consumer<? super F> failureAction) {
        Objects.requireNonNull(failureAction);
        failureAction.accept(this.value);
        return this;
    }

    @Override
    public Result<S, F> ifFailure(Consumer<? super F> failureAction) {
        Objects.requireNonNull(failureAction);
        failureAction.accept(this.value);
        return this;
    }

    @Override
    public Result<S, F> filter(Predicate<? super S> predicate, Function<? super S, ? extends F> failureMapper) {
        return this;
    }

    @Override
    public <S2, F2> Result<S2, F2> map(
            Function<? super S, S2> successMapper,
            Function<? super F, F2> failureMapper) {
        Objects.requireNonNull(failureMapper);
        return new DefaultFailure<>(failureMapper.apply(this.value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S2> Result<S2, F> map(Function<? super S, S2> successMapper) {
        return (Result<S2, F>) this;
    }

    @Override
    public <F2> Result<S, F2> mapFailure(Function<? super F, F2> failureMapper) {
        Objects.requireNonNull(failureMapper);
        return new DefaultFailure<>(failureMapper.apply(this.value));
    }

    @Override
    public <S2, F2> Result<S2, F2> flatMap(
            Function<? super S, Result<S2, F2>> successFlatMapper,
            Function<? super F, Result<S2, F2>> failureFlatMapper) {
        Objects.requireNonNull(failureFlatMapper);
        return failureFlatMapper.apply(this.value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S2> Result<S2, F> flatMap(Function<? super S, Result<S2, F>> successFlatMapper) {
        return (Result<S2, F>) this;
    }

    @Override
    public <F2> Result<S, F2> flatMapFailure(Function<? super F, Result<S, F2>> failureFlatMapper) {
        Objects.requireNonNull(failureFlatMapper);
        return failureFlatMapper.apply(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Result)) return false;
        final Result<?, ?> other = (Result<?, ?>) obj;
        return other.isFailure() && Objects.equals(this.value, other.getFailureOrElseThrow());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public String toString() {
        return String.format("failure[%s]", this.value);
    }
}
