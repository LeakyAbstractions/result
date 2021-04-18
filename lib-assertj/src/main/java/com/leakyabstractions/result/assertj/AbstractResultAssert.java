
package com.leakyabstractions.result.assertj;

import static com.leakyabstractions.result.assertj.ResultShouldBe.shouldBeFailure;
import static com.leakyabstractions.result.assertj.ResultShouldBe.shouldBeSuccess;
import static com.leakyabstractions.result.assertj.ResultShouldHave.shouldHave;
import static com.leakyabstractions.result.assertj.ResultShouldHave.shouldHaveInstanceOf;
import static com.leakyabstractions.result.assertj.ResultShouldHave.shouldHaveSame;
import static org.assertj.core.api.ObjectAssertProxy.assertWithAssertionState;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.function.Consumer;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertProxy;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.Conditions;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;

import com.leakyabstractions.result.Result;

/**
 * Assertions for {@link Result}.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <S> type of the success value contained in the {@link Result}.
 * @param <F> type of the failure value contained in the {@link Result}.
 * @author Guillermo Calvo
 */
@SuppressWarnings("java:S119") // Type parameter names should comply with a naming convention
abstract class AbstractResultAssert<SELF extends AbstractResultAssert<SELF, S, F>, S, F>
        extends AbstractAssert<SELF, Result<S, F>> {

    protected AbstractResultAssert(Result<S, F> actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link Result} is successful.
     * <p>
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success("yay")).isSuccess();
     * assertThat(Results.success(null)).isSuccess();
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure("nay")).isSuccess();
     * assertThat(Results.failure(null)).isSuccess();
     * }
     * </pre>
     *
     * @return this assertion object.
     */
    public SELF isSuccess() {
        this.assertIsSuccess();
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a successful result containing the given value.
     * <p>
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success("yay")).hasSuccess("yay");
     * assertThat(Results.success(1234)).hasSuccess(1234);
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success("yay")).hasSuccess("nay");
     * assertThat(Results.failure("yay")).hasSuccess("yay");
     * }
     * </pre>
     *
     * @param expectedValue the expected success value inside the {@link Result}; can't be {@code null}.
     * @return this assertion object.
     */
    public SELF hasSuccess(S expectedValue) {
        final S value = this.assertIsSuccess();
        this.checkNotNull(expectedValue);
        if (value == null) {
            this.throwAssertionError(shouldHave(this.actual(), expectedValue, value));
        }
        if (!StandardComparisonStrategy.instance().areEqual(value, expectedValue)) {
            throw Failures.instance().failure(this.info(), shouldHave(this.actual(), expectedValue, value), value,
                    expectedValue);
        }
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a successful result containing the instance given as an argument (i.e.
     * it must be the same instance).
     * <p>
     * Given:
     *
     * <pre class="row-color">
     * {@code
     *     static {
     *         final String FOOBAR = "foobar";
     *     }
     * }
     * </pre>
     *
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success(FOOBAR)).hasSuccessSameAs(FOOBAR);
     * assertThat(Results.success(10)).hasSuccessSameAs(10);
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success("yay")).hasSuccessSameAs("nay");
     * assertThat(Results.failure(FOOBAR)).hasSuccessSameAs(FOOBAR);
     * }
     * </pre>
     *
     * @param expectedValue the expected success value inside the {@link Result}; can't be {@code null}.
     * @return this assertion object.
     */
    public SELF hasSuccessSameAs(S expectedValue) {
        final S value = this.assertIsSuccess();
        this.checkNotNull(expectedValue);
        if (value != expectedValue) {
            this.throwAssertionError(shouldHaveSame(this.actual(), expectedValue, value));
        }
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a successful result and invokes the given {@link Consumer} with the
     * possibly-{@code null} success value for further assertions.
     * <p>
     * Should be used as a way of deeper asserting on the containing object, as further requirement(s) for the value.
     * <p>
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * // one requirement
     * assertThat(Results.success(10)).hasSuccessSatisfying(s -> { assertThat(s).isGreaterThan(9); });
     * assertThat(Results.success(null)).hasSuccessSatisfying(s -> { assertThat(s).isNull(); });
     *
     * // multiple requirements
     * assertThat(Results.success("hello")).hasSuccessSatisfying(s -> {
     *   assertThat(s).isEqualTo("hello");
     *   assertThat(s).startsWith("h");
     *   assertThat(s).endsWith("o");
     * });
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success("yay")).hasSuccessSatisfying(s -> assertThat(s).isEqualTo("nay"));
     * assertThat(Results.failure("yay")).hasSuccessSatisfying(o -> {});
     * }
     * </pre>
     *
     * @param requirement to further assert on the success value held by the {@link Result}
     * @return this assertion object.
     */
    public SELF hasSuccessSatisfying(Consumer<S> requirement) {
        final S value = this.assertIsSuccess();
        requirement.accept(value);
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a successful result whose possibly-{@code null} success value
     * satisfies the given {@link Condition}.
     * <p>
     * Given:
     *
     * <pre class="row-color">
     * {@code
     *     static {
     *         final Condition<Integer> IS_NEGATIVE = new Condition<>(i -> i < 0, "a negative number");
     *     }
     * }
     * </pre>
     *
     * Assertion will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success(-1)).hasSuccessSatisfying(IS_NEGATIVE);
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success(1234)).hasSuccessSatisfying(IS_NEGATIVE);
     * assertThat(Results.failure(-123)).hasSuccessSatisfying(IS_NEGATIVE);
     * }
     * </pre>
     *
     * @param condition the given condition
     * @return this assertion object.
     */
    public SELF hasSuccessSatisfying(Condition<? super S> condition) {
        final S value = this.assertIsSuccess();
        Conditions.instance().assertIs(this.info(), value, condition);
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a successful result containing a non-null value that is an instance of
     * the given class.
     * <p>
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success("hello"))
     *         .hasSuccessInstanceOf(String.class)
     *         .hasSuccessInstanceOf(Object.class);
     * assertThat(Results.success(1234)).hasSuccessInstanceOf(Integer.class);
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success("hello")).hasSuccessInstanceOf(Integer.class);
     * assertThat(Results.failure("hello")).hasSuccessInstanceOf(String.class);
     * }
     * </pre>
     *
     * @param clazz the expected class of the success value inside the {@link Result}
     * @return this assertion object.
     */
    public SELF hasSuccessInstanceOf(Class<?> clazz) {
        final S value = this.assertIsSuccess();
        this.checkNotNull(clazz);
        if (!clazz.isInstance(value)) {
            this.throwAssertionError(shouldHaveInstanceOf(this.actual(), clazz, value));
        }
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a successful result and returns an Object assertion that allows
     * chaining (object) assertions on its value.
     * <p>
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success(0)).hasSuccessThat().isZero();
     * assertThat(Results.success(100)).hasSuccessThat().isGreaterThan(10);
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure(0)).hasSuccessThat().isZero();
     * assertThat(Results.success(1)).hasSuccessThat().isGreaterThan(10);
     * }
     * </pre>
     *
     * @return a new {@link ObjectAssert} for assertions chaining on the success value.
     * @see #hasSuccessThat(InstanceOfAssertFactory)
     */
    @CheckReturnValue
    public ObjectAssert<S> hasSuccessThat() {
        final S value = this.assertIsSuccess();
        return assertWithAssertionState(myself, value);
    }

    /**
     * Verifies that the actual {@link Result} is a successful result containing a non-null value and returns an new
     * assertion instance to chain assertions on it.
     * <p>
     * The {@code assertFactory} parameter allows to specify an {@link InstanceOfAssertFactory}, which is used to get
     * the assertions narrowed to the factory type.
     * <p>
     * Wrapping the given {@link InstanceOfAssertFactory} with {@link Assertions#as(InstanceOfAssertFactory)} makes the
     * assertion more readable.
     * <p>
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success(0)).hasSuccessThat(as(InstanceOfAssertFactories.INTEGER)).isZero();
     * assertThat(Results.success("hello")).hasSuccessThat(as(InstanceOfAssertFactories.STRING)).startsWith("h");
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success("hello")).hasSuccessThat(as(InstanceOfAssertFactories.INTEGER)).isZero();
     * assertThat(Results.failure("hello")).hasSuccessThat(as(InstanceOfAssertFactories.STRING)).startsWith("h");
     * }
     * </pre>
     *
     * @param <T> the type of the resulting {@code Assert}
     * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
     * @return a new narrowed {@link ObjectAssertProxy} instance for assertions chaining on the success value
     */
    @CheckReturnValue
    public <T extends AbstractAssert<?, ?>> T hasSuccessThat(InstanceOfAssertFactory<?, T> assertFactory) {
        final S value = this.assertIsSuccess();
        if (value == null) {
            this.throwAssertionError(shouldNotBeNull("success value"));
        }
        return assertWithAssertionState(myself, value).asInstanceOf(assertFactory);
    }

    /**
     * Verifies that the actual {@link Result} is failed.
     * <p>
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure("yay")).isFailure();
     * assertThat(Results.failure(null)).isFailure();
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success("nay")).isFailure();
     * assertThat(Results.success(null)).isFailure();
     * }
     * </pre>
     *
     * @return this assertion object.
     */
    public SELF isFailure() {
        this.assertIsFailure();
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a failed result containing the given value.
     * <p>
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure("yay")).hasFailure("yay");
     * assertThat(Results.failure(1234)).hasFailure(1234);
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure("yay")).hasFailure("nay");
     * assertThat(Results.success("yay")).hasFailure("yay");
     * }
     * </pre>
     *
     * @param expectedValue the expected failure value inside the {@link Result}; can't be {@code null}.
     * @return this assertion object.
     */
    public SELF hasFailure(F expectedValue) {
        final F value = this.assertIsFailure();
        this.checkNotNull(expectedValue);
        if (value == null) {
            this.throwAssertionError(shouldHave(this.actual(), expectedValue, value));
        }
        if (!StandardComparisonStrategy.instance().areEqual(value, expectedValue)) {
            throw Failures.instance().failure(this.info(), shouldHave(this.actual(), expectedValue, value), value,
                    expectedValue);
        }
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a failed result containing the instance given as an argument (i.e. it
     * must be the same instance).
     * <p>
     * Given:
     *
     * <pre class="row-color">
     * {@code
     *     static {
     *         final String FOOBAR = "foobar";
     *     }
     * }
     * </pre>
     *
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure(FOOBAR)).hasFailureSameAs(FOOBAR);
     * assertThat(Results.failure(10)).hasFailureSameAs(10);
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * // not equal:
     * assertThat(Results.failure("yay")).hasFailureSameAs("nay");
     * assertThat(Results.success(FOOBAR)).hasFailureSameAs(FOOBAR);
     * }
     * </pre>
     *
     * @param expectedValue the expected failure value inside the {@link Result}; can't be {@code null}.
     * @return this assertion object.
     */
    public SELF hasFailureSameAs(F expectedValue) {
        final F value = this.assertIsFailure();
        this.checkNotNull(expectedValue);
        if (value != expectedValue) {
            this.throwAssertionError(shouldHaveSame(this.actual(), expectedValue, value));
        }
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a failed result and invokes the given {@link Consumer} with the
     * possibly-{@code null} failure value for further assertions.
     * <p>
     * Should be used as a way of deeper asserting on the containing object, as further requirement(s) for the value.
     * <p>
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * // one requirement
     * assertThat(Results.failure(10)).hasFailureSatisfying(s -> { assertThat(s).isGreaterThan(9); });
     * assertThat(Results.failure(null)).hasFailureSatisfying(s -> { assertThat(s).isNull(); });
     *
     * // multiple requirements
     * assertThat(Results.failure("hello")).hasFailureSatisfying(s -> {
     *   assertThat(s).isEqualTo("hello");
     *   assertThat(s).startsWith("h");
     *   assertThat(s).endsWith("o");
     * });
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success("hello")).hasSuccessSatisfying(s -> assertThat(s).isEqualTo("hello"));
     * assertThat(Results.failure("hello")).hasSuccessSatisfying(o -> {});
     * }
     * </pre>
     *
     * @param requirement to further assert on the failure value held by the {@link Result}
     * @return this assertion object.
     */
    public SELF hasFailureSatisfying(Consumer<F> requirement) {
        final F value = this.assertIsFailure();
        requirement.accept(value);
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a failed result whose possibly-{@code null} failure value satisfies
     * the given {@link Condition}.
     * <p>
     * Given:
     *
     * <pre class="row-color">
     * {@code
     *     static {
     *         final Condition<Integer> IS_NEGATIVE = new Condition<>(i -> i < 0, "a negative number");
     *     }
     * }
     * </pre>
     *
     * Assertion will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure(-1)).hasFailureSatisfying(IS_NEGATIVE);
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure(1234)).hasFailureSatisfying(IS_NEGATIVE);
     * assertThat(Results.success(-123)).hasFailureSatisfying(IS_NEGATIVE);
     * }
     * </pre>
     *
     * @param condition the given condition
     * @return this assertion object.
     */
    public SELF hasFailureSatisfying(Condition<? super F> condition) {
        final F value = this.assertIsFailure();
        Conditions.instance().assertIs(this.info(), value, condition);
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a failed result containing a non-null value that is an instance of the
     * given class.
     * <p>
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure("hello"))
     *         .hasFailureInstanceOf(String.class)
     *         .hasFailureInstanceOf(Object.class);
     * assertThat(Results.failure(123)).hasSuccessInstanceOf(Integer.class);
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure("hello")).hasFailureInstanceOf(Integer.class);
     * assertThat(Results.success("hello")).hasFailureInstanceOf(String.class);
     * }
     * </pre>
     *
     * @param clazz the expected class of the failure value inside the {@link Result}
     * @return this assertion object.
     */
    public SELF hasFailureInstanceOf(Class<?> clazz) {
        final F value = this.assertIsFailure();
        this.checkNotNull(clazz);
        if (!clazz.isInstance(value)) {
            this.throwAssertionError(shouldHaveInstanceOf(this.actual(), clazz, value));
        }
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a failed result and returns an Object assertion that allows chaining
     * (object) assertions on its value.
     * <p>
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure(10)).hasFailureThat().isZero();
     * assertThat(Results.failure(100)).hasFailureThat().isGreaterThan(10);
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.success(0)).hasFailureThat().isZero();
     * assertThat(Results.failure(1)).hasFailureThat().isGreaterThan(10);
     * }
     * </pre>
     *
     * @return a new {@link ObjectAssert} for assertions chaining on the success value.
     * @see #hasSuccessThat(InstanceOfAssertFactory)
     */
    @CheckReturnValue
    public ObjectAssert<F> hasFailureThat() {
        final F value = this.assertIsFailure();
        return assertWithAssertionState(myself, value);
    }

    /**
     * Verifies that the actual {@link Result} is a failed result containing a non-null value and returns an new
     * assertion instance to chain assertions on it.
     * <p>
     * The {@code assertFactory} parameter allows to specify an {@link InstanceOfAssertFactory}, which is used to get
     * the assertions narrowed to the factory type.
     * <p>
     * Wrapping the given {@link InstanceOfAssertFactory} with {@link Assertions#as(InstanceOfAssertFactory)} makes the
     * assertion more readable.
     * <p>
     * Assertions will pass:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure(0)).hasFailureThat(as(InstanceOfAssertFactories.INTEGER)).isZero();
     * assertThat(Results.failure("hello")).hasFailureThat(as(InstanceOfAssertFactories.STRING)).startsWith("h");
     * }
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre class="row-color">
     * {@code
     * assertThat(Results.failure("hello")).hasFailureThat(as(InstanceOfAssertFactories.INTEGER)).isZero();
     * assertThat(Results.success("hello")).hasFailureThat(as(InstanceOfAssertFactories.STRING)).startsWith("h");
     * }
     * </pre>
     *
     * @param <T> the type of the resulting {@code Assert}
     * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
     * @return a new narrowed {@link ObjectAssertProxy} instance for assertions chaining on the success value
     */
    @CheckReturnValue
    public <T extends AbstractAssert<?, ?>> T hasFailureThat(InstanceOfAssertFactory<?, T> assertFactory) {
        final F value = this.assertIsFailure();
        if (value == null) {
            this.throwAssertionError(shouldNotBeNull("failure value"));
        }
        return assertWithAssertionState(myself, value).asInstanceOf(assertFactory);
    }

    private void checkNotNull(Object expectedValue) {
        checkArgument(expectedValue != null, "The expected value should not be <null>.");
    }

    private S assertIsSuccess() {
        isNotNull();
        if (this.actual().isFailure()) {
            this.throwAssertionError(shouldBeSuccess(this.actual()));
        }
        return this.actual().orElseThrow();
    }

    private F assertIsFailure() {
        isNotNull();
        if (this.actual().isSuccess()) {
            this.throwAssertionError(shouldBeFailure(this.actual()));
        }
        return this.actual().getFailureOrElseThrow();
    }

    // Class members annotated with "@VisibleForTesting" should not be accessed from production code

    @SuppressWarnings("java:S5803")
    private Result<S, F> actual() {
        return this.actual;
    }

    @SuppressWarnings("java:S5803")
    private WritableAssertionInfo info() {
        return this.info;
    }
}
