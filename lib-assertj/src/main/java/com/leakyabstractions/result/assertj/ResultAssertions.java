
package com.leakyabstractions.result.assertj;

import org.assertj.core.api.Assertions;

import com.leakyabstractions.result.Result;

/**
 * Entry point for {@link Result} assertions and AssertJ ones.
 *
 * @author Guillermo Calvo
 */
public class ResultAssertions extends Assertions {

    /**
     * Create assertion for {@link Result}.
     *
     * @param <S> type of the success value contained in the {@link Result}.
     * @param <F> type of the failure value contained in the {@link Result}.
     * @param actual the actual value.
     * @return the created assertion object.
     */
    public static <S, F> ResultAssert<S, F> assertThat(Result<S, F> actual) {
        return ResultAssert.assertThatResult(actual);
    }
}
