
package com.leakyabstractions.result.assertj;

import org.assertj.core.error.BasicErrorMessageFactory;

import com.leakyabstractions.result.Result;

/**
 * Build error message when a {@link Result} should have a specific success/failure value.
 *
 * @author Guillermo Calvo
 */
class ResultShouldHave extends BasicErrorMessageFactory {

    private static final String EXPECTING_VALUE_BUT_DIFFERENT = "%nExpecting result:%n  <%s>%nto contain:%n  <%s>%nbut did contain:%n  <%s>.";
    private static final String EXPECTING_VALUE_BUT_WAS_EMPTY = "%nExpecting result:%n  <%s>%nto contain:%n  <%s>%nbut was empty.";
    private static final String EXPECTING_CLASS_BUT_DIFFERENT = "%nExpecting result:%n  <%s>%nto contain a value that is an instance of:%n <%s>%nbut did contain an instance of:%n  <%s>";
    private static final String EXPECTING_CLASS_BUT_WAS_EMPTY = "%nExpecting result:%n  <%s>%nto contain a value that is an instance of:%n <%s>%nbut was empty";
    private static final String EXPECTING_EXACT_BUT_DIFFERENT = "%nExpecting result:%n  <%s>%nto contain the instance (i.e. compared with ==):%n  <%s>%nbut did not.";
    private static final String EXPECTING_EXACT_BUT_WAS_EMPTY = "%nExpecting result:%n  <%s>%nto contain the instance (i.e. compared with ==):%n  <%s>%nbut was empty.";

    private ResultShouldHave(String message, Result<?, ?> result, Object argument) {
        super(message, result, argument);
    }

    private ResultShouldHave(String message, Result<?, ?> result, Object argument1, Object argument2) {
        super(message, result, argument1, argument2);
    }

    static ResultShouldHave shouldHave(Result<?, ?> result, Object expectedValue, Object actualValue) {
        if (actualValue == null) {
            return new ResultShouldHave(EXPECTING_VALUE_BUT_WAS_EMPTY, result, expectedValue);
        }
        return new ResultShouldHave(EXPECTING_VALUE_BUT_DIFFERENT, result, expectedValue, actualValue);
    }

    static ResultShouldHave shouldHaveSame(Result<?, ?> result, Object expectedValue, Object actualValue) {
        if (actualValue == null) {
            return new ResultShouldHave(EXPECTING_EXACT_BUT_WAS_EMPTY, result, expectedValue);
        }
        return new ResultShouldHave(EXPECTING_EXACT_BUT_DIFFERENT, result, expectedValue);
    }

    static ResultShouldHave shouldHaveInstanceOf(Result<?, ?> result, Class<?> clazz, Object value) {
        final String className = clazz.getName();
        if (value == null) {
            return new ResultShouldHave(EXPECTING_CLASS_BUT_WAS_EMPTY, result, className);
        }
        return new ResultShouldHave(EXPECTING_CLASS_BUT_DIFFERENT, result, className, value.getClass().getName());
    }
}
