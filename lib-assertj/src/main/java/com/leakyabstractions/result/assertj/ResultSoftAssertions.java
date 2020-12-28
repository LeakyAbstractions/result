
package com.leakyabstractions.result.assertj;

import org.assertj.core.api.SoftAssertions;

import com.leakyabstractions.result.Result;

/**
 * Soft assertions for {@link Result}.
 *
 * @author Guillermo Calvo
 */
public class ResultSoftAssertions extends SoftAssertions {

    /**
     * Create soft assertion for {@link Result}.
     *
     * @param <S> type of the success value contained in the {@link Result}.
     * @param <F> type of the failure value contained in the {@link Result}.
     * @param actual the actual value.
     * @return the created soft assertion object.
     */
    @SuppressWarnings("unchecked")
    public <S, F> ResultAssert<S, F> assertThat(Result<S, F> actual) {
        return this.proxy(ResultAssert.class, Result.class, actual);
    }
}
