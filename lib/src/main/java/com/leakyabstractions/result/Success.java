
package com.leakyabstractions.result;

import static java.util.Objects.requireNonNull;

import java.util.NoSuchElementException;
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
    public S orElseMap(Function<? super F, ? extends S> mapper) {
        return this.value;
    }

    @Override
    public S orElseThrow() {
        return this.value;
    }

    @Override
    public <E extends Throwable> S orElseThrow(Function<? super F, E> mapper) throws E {
        return this.value;
    }

    @Override
    public F getFailureOrElseThrow() {
        throw new NoSuchElementException("Not a failed result");
    }

    @Override
    public Stream<S> stream() {
        return Stream.of(this.value);
    }

    @Override
    public Stream<F> streamFailure() {
        return Stream.empty();
    }

    @Override
    public Optional<S> optional() {
        return Optional.of(this.value);
    }

    @Override
    public Optional<F> optionalFailure() {
        return Optional.empty();
    }

    @Override
    public Result<S, F> ifSuccess(Consumer<? super S> action) {
        action.accept(this.value);
        return this;
    }

    @Override
    public Result<S, F> ifSuccessOrElse(Consumer<? super S> successAction, Consumer<? super F> failureAction) {
        successAction.accept(this.value);
        return this;
    }

    @Override
    public Result<S, F> ifFailure(Consumer<? super F> action) {
        return this;
    }

    @Override
    public Result<S, F> filter(Predicate<? super S> predicate, Function<? super S, ? extends F> mapper) {
        return predicate.test(this.value) ? this : new Failure<>(requireNonNull(mapper.apply(this.value)));
    }

    @Override
    public <S2, F2> Result<S2, F2> map(
            Function<? super S, ? extends S2> successMapper,
            Function<? super F, ? extends F2> failureMapper) {
        return new Success<>(requireNonNull(successMapper.apply(this.value)));
    }

    @Override
    public <S2> Result<S2, F> mapSuccess(Function<? super S, ? extends S2> mapper) {
        return new Success<>(requireNonNull(mapper.apply(this.value)));
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
        return (Result<S2, F2>) requireNonNull(successMapper.apply(this.value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S2> Result<S2, F> flatMapSuccess(Function<? super S, ? extends Result<? extends S2, ? extends F>> mapper) {
        return (Result<S2, F>) requireNonNull(mapper.apply(this.value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F2> Result<S, F2> flatMapFailure(Function<? super F, ? extends Result<? extends S, ? extends F2>> mapper) {
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
