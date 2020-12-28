
package com.leakyabstractions.result.assertj;

import com.leakyabstractions.result.Result;

/**
 * Assertions for {@link Result}.
 *
 * @param <S> type of the success value contained in the {@link Result}.
 * @param <F> type of the failure value contained in the {@link Result}.
 * @author Guillermo Calvo
 */
public class ResultAssert<S, F> extends AbstractResultAssert<ResultAssert<S, F>, S, F> {

    ResultAssert(Result<S, F> actual) {
        super(actual, ResultAssert.class);
    }

    /**
     * Create assertion for {@link Result}.
     * <p>
     * This static method is provided for convenience, in case {@link ResultAssertions#assertThat(Result)} can't be
     * statically imported.
     *
     * @param <S> type of the success value contained in the {@link Result}.
     * @param <F> type of the failure value contained in the {@link Result}.
     * @param actual the actual value.
     * @return the created assertion object.
     */
    public static <S, F> ResultAssert<S, F> assertThatResult(Result<S, F> actual) {
        return new ResultAssert<>(actual);
    }
}
