
package com.leakyabstractions.result;

import static java.util.Objects.requireNonNull;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
    public S orElseMap(Function<? super F, ? extends S> mapper) {
        return requireNonNull(mapper).apply(this.value);
    }

    @Override
    public S orElseThrow() {
        throw new NoSuchElementException("Not a successful result");
    }

    @Override
    public <E extends Throwable> S orElseThrow(Function<? super F, E> mapper) throws E {
        throw requireNonNull(mapper).apply(this.value);
    }

    @Override
    public F getFailureOrElseThrow() {
        return this.value;
    }

    @Override
    public Stream<S> stream() {
        return Stream.empty();
    }

    @Override
    public Stream<F> streamFailure() {
        return Stream.of(this.value);
    }

    @Override
    public Result<S, F> ifSuccess(Consumer<? super S> successAction) {
        return this;
    }

    @Override
    public Result<S, F> ifSuccessOrElse(Consumer<? super S> successAction, Consumer<? super F> failureAction) {
        requireNonNull(failureAction).accept(this.value);
        return this;
    }

    @Override
    public Result<S, F> ifFailure(Consumer<? super F> failureAction) {
        requireNonNull(failureAction).accept(this.value);
        return this;
    }

    @Override
    public Result<S, F> filter(Predicate<? super S> predicate, Function<? super S, ? extends F> mapper) {
        return this;
    }

    @Override
    public <S2, F2> Result<S2, F2> map(
            Function<? super S, S2> successMapper,
            Function<? super F, F2> failureMapper) {
        return new Failure<>(requireNonNull(failureMapper).apply(this.value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S2> Result<S2, F> mapSuccess(Function<? super S, S2> mapper) {
        return (Result<S2, F>) this;
    }

    @Override
    public <F2> Result<S, F2> mapFailure(Function<? super F, F2> mapper) {
        return new Failure<>(requireNonNull(mapper).apply(this.value));
    }

    @Override
    public <S2, F2> Result<S2, F2> flatMap(
            Function<? super S, Result<S2, F2>> successMapper,
            Function<? super F, Result<S2, F2>> failureMapper) {
        return requireNonNull(failureMapper).apply(this.value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S2> Result<S2, F> flatMapSuccess(Function<? super S, Result<S2, F>> mapper) {
        return (Result<S2, F>) this;
    }

    @Override
    public <F2> Result<S, F2> flatMapFailure(Function<? super F, Result<S, F2>> mapper) {
        return requireNonNull(mapper).apply(this.value);
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
        return String.format("failure[%s]", this.value);
    }
}
