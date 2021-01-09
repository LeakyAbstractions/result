
package com.leakyabstractions.result;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public S orElse(S other) {
        return this.value;
    }

    @Override
    public S orElseMap(Function<? super F, ? extends S> failureMapper) {
        return this.value;
    }

    @Override
    public S orElseThrow() {
        return this.value;
    }

    @Override
    public <E extends Throwable> S orElseThrow(Function<? super F, E> failureMapper) throws E {
        return this.value;
    }

    @Override
    public F getFailureOrElseThrow() {
        throw new NoSuchElementException("Not a failed result");
    }

    @Override
    public Result<S, F> ifSuccess(Consumer<? super S> successAction) {
        Objects.requireNonNull(successAction);
        successAction.accept(this.value);
        return this;
    }

    @Override
    public Result<S, F> ifSuccessOrElse(Consumer<? super S> successAction, Consumer<? super F> failureAction) {
        Objects.requireNonNull(successAction);
        successAction.accept(this.value);
        return this;
    }

    @Override
    public Result<S, F> ifFailure(Consumer<? super F> failureAction) {
        return this;
    }

    @Override
    public Result<S, F> filter(Predicate<? super S> predicate, Function<? super S, ? extends F> failureMapper) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(failureMapper);
        return predicate.test(this.value) ? this : new Failure<>(failureMapper.apply(this.value));
    }

    @Override
    public <S2, F2> Result<S2, F2> map(
            Function<? super S, S2> successMapper,
            Function<? super F, F2> failureMapper) {
        Objects.requireNonNull(successMapper);
        return new Success<>(successMapper.apply(this.value));
    }

    @Override
    public <S2> Result<S2, F> mapSuccess(Function<? super S, S2> successMapper) {
        Objects.requireNonNull(successMapper);
        return new Success<>(successMapper.apply(this.value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F2> Result<S, F2> mapFailure(Function<? super F, F2> failureMapper) {
        return (Result<S, F2>) this;
    }

    @Override
    public <S2, F2> Result<S2, F2> flatMap(
            Function<? super S, Result<S2, F2>> successFlatMapper,
            Function<? super F, Result<S2, F2>> failureFlatMapper) {
        Objects.requireNonNull(successFlatMapper);
        return successFlatMapper.apply(this.value);
    }

    @Override
    public <S2> Result<S2, F> flatMapSuccess(Function<? super S, Result<S2, F>> successFlatMapper) {
        Objects.requireNonNull(successFlatMapper);
        return successFlatMapper.apply(this.value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F2> Result<S, F2> flatMapFailure(Function<? super F, Result<S, F2>> failureFlatMapper) {
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
        return String.format("success[%s]", this.value);
    }
}
