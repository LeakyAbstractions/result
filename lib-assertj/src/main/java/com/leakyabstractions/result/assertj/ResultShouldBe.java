
package com.leakyabstractions.result.assertj;

import org.assertj.core.error.BasicErrorMessageFactory;

import com.leakyabstractions.result.Result;

/**
 * Build error message when a {@link Result} should be a success or a failure.
 *
 * @author Guillermo Calvo
 */
class ResultShouldBe extends BasicErrorMessageFactory {

    private static final String EXPECTING_SUCCESS = "%nExpecting result:%n  <%s>%nto be a success but was not.";
    private static final String EXPECTING_FAILURE = "%nExpecting result:%n  <%s>%nto be a failure but was not.";

    private ResultShouldBe(String message, Result<?, ?> result) {
        super(message, result);
    }

    /**
     * Indicates that a {@link Result} should be a success.
     *
     * @return a error message factory.
     * @param result the result instance
     */
    static ResultShouldBe shouldBeSuccess(Result<?, ?> result) {
        return new ResultShouldBe(EXPECTING_SUCCESS, result);
    }

    /**
     * Indicates that a {@link Result} should be a failure.
     *
     * @return a error message factory.
     * @param result the result instance
     */
    static ResultShouldBe shouldBeFailure(Result<?, ?> result) {
        return new ResultShouldBe(EXPECTING_FAILURE, result);
    }
}
