
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
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.success("yay")).isSuccess();
     * assertThat(DefaultResult.success(null)).isSuccess();
     * assertThat(DefaultResult.success(1234)).isSuccess();
     * assertThat(DefaultResult.success(false)).isSuccess();
     * </code>
     * </pre>
     *
     * Assertions will fail:
     * 
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.failure("nay")).isSuccess();
     * assertThat(DefaultResult.failure(null)).isSuccess();
     * assertThat(DefaultResult.failure(1234)).isSuccess();
     * assertThat(DefaultResult.failure(false)).isSuccess();
     * </code>
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
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.success("yay")).hasSuccess("yay");
     * assertThat(DefaultResult.success(1234)).hasSuccess(1234);
     * </code>
     * </pre>
     *
     * Assertions will fail:
     * 
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.success("yay")).hasSuccess("nay");
     * assertThat(DefaultResult.success(null)).hasSuccess("null");
     * assertThat(DefaultResult.success(1234)).hasSuccess(4321);
     * assertThat(DefaultResult.failure("yay")).hasSuccess("yay");
     * </code>
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
     * Assertions will pass:
     *
     * <pre>
     * <code class='java'>
     * String foobar = "foobar";
     * assertThat(DefaultResult.success(foobar)).hasSuccessSameAs(foobar);
     * assertThat(DefaultResult.success(10)).hasSuccessSameAs(10);
     * </code>
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre>
     * <code class='java'>
     * // not equal:
     * assertThat(DefaultResult.success("yay")).hasSuccessSameAs("nay");
     * assertThat(DefaultResult.success(1234)).hasSuccessSameAs(4321);
     *
     * // equal but not successful:
     * String foo = "FOO";
     * assertThat(DefaultResult.failure(foo)).hasSuccessSameAs(foo);
     * 
     * // equal but not the same:
     * assertThat(DefaultResult.success(new String("bar"))).hasSuccessSameAs(new String("bar"));
     * assertThat(DefaultResult.success(new Integer(1234))).hasSuccessSameAs(new Integer(1234));
     * </code>
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
     * success value (may be {@code null}) for further assertions.
     * <p>
     * Should be used as a way of deeper asserting on the containing object, as further requirement(s) for the value.
     * <p>
     * Assertions will pass:
     * 
     * <pre>
     * <code class='java'>
     * // one requirement
     * assertThat(DefaultResult.success(10)).hasSuccessSatisfying(s -&gt; { assertThat(s).isGreaterThan(9); });
     * assertThat(DefaultResult.success(null)).hasSuccessSatisfying(s -&gt; { assertThat(s).isNull(); });
     *
     * // multiple requirements
     * assertThat(DefaultResult.success("hello")).hasSuccessSatisfying(s -&gt; {
     *   assertThat(s).isEqualTo("hello");
     *   assertThat(s).startsWith("h");
     *   assertThat(s).endsWith("o");
     * });
     * </code>
     * </pre>
     *
     * Assertions will fail:
     * 
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.success("hello")).hasSuccessSatisfying(s -&gt; assertThat(s).isEqualTo("hello"); });
     * assertThat(DefaultResult.failure("hello")).hasSuccessSatisfying(o -&gt; {});
     * </code>
     * </pre>
     *
     * @param requirement to further assert on the success value held by the {@link Result}; can't be {@code null}.
     * @return this assertion object.
     */
    public SELF hasSuccessSatisfying(Consumer<S> requirement) {
        final S value = this.assertIsSuccess();
        requirement.accept(value);
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a successful result whose success value (may be {@code null})
     * satisfies the given {@link Condition}.
     * <p>
     * Assertions will pass:
     * 
     * <pre>
     * <code class='java'>
     * Condition&lt;Integer&gt; isNegative = new Condition&lt;&gt;(i -&gt; i &lt; 0, "a negative number");
     * Condition&lt;Object&gt; isNull = new Condition&lt;&gt;(o -&gt; o == null, "a null reference");
     * assertThat(DefaultResult.success(-1)).hasSuccessSatisfying(isNegative);
     * assertThat(DefaultResult.success(null)).hasSuccessSatisfying(isNull);
     * </code>
     * </pre>
     *
     * Assertions will fail:
     * 
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.success(1234)).hasSuccessSatisfying(isNegative);
     * assertThat(DefaultResult.failure(-123)).hasSuccessSatisfying(isNegative);
     * assertThat(DefaultResult.success("hello")).hasSuccessSatisfying(isNull);
     * assertThat(DefaultResult.failure(null)).hasSuccessSatisfying(isNull);
     * </code>
     * </pre>
     *
     * @param condition the given condition; can't be {@code null}.
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
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.success("hello"))
     *         .hasSuccessInstanceOf(String.class)
     *         .hasSuccessInstanceOf(Object.class);
     * assertThat(DefaultResult.success(1234)).hasSuccessInstanceOf(Integer.class);
     * </code>
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.success("hello")).hasSuccessInstanceOf(Integer.class);
     * assertThat(DefaultResult.failure("hello")).hasSuccessInstanceOf(String.class);
     * </code>
     * </pre>
     *
     * @param clazz the expected class of the success value inside the {@link Result}; can't be {@code null}.
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
     * <pre>
     * <code class='java'>
     * Foo foo = new Foobar("Hello", "world");
     * 
     * assertThat(DefaultResult.success(foo)).hasSuccessThat().hasNoNullFieldsOrProperties();
     * assertThat(DefaultResult.success(null)).hasSuccessThat().isNull();
     * </code>
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre>
     * <code class='java'>
     * Foo bar = new Foobar("Hello", null);
     * 
     * assertThat(DefaultResult.success(bar)).hasSuccessThat().hasNoNullFieldsOrProperties();
     * assertThat(DefaultResult.success(null)).hasSuccessThat().isNotNull();
     * </code>
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
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.success(0)).hasSuccessThat(as(InstanceOfAssertFactories.INTEGER)).isZero();
     * assertThat(DefaultResult.success("hello")).hasSuccessThat(as(InstanceOfAssertFactories.STRING)).startsWith("h");
     * </code>
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.success("hello")).hasSuccessThat(as(InstanceOfAssertFactories.INTEGER)).isZero();
     * assertThat(DefaultResult.failure("hello")).hasSuccessThat(as(InstanceOfAssertFactories.STRING)).startsWith("h");
     * </code>
     * </pre>
     *
     * @param <T> the type of the resulting {@code Assert}
     * @param assertFactory the factory which verifies the type and creates the new {@code Assert}; can't be
     *            {@code null}.
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
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.failure("yay")).isFailure();
     * assertThat(DefaultResult.failure(null)).isFailure();
     * assertThat(DefaultResult.failure(1234)).isFailure();
     * assertThat(DefaultResult.failure(false)).isFailure();
     * </code>
     * </pre>
     *
     * Assertions will fail:
     * 
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.success("nay")).isFailure();
     * assertThat(DefaultResult.success(null)).isFailure();
     * assertThat(DefaultResult.success(1234)).isFailure();
     * assertThat(DefaultResult.success(false)).isFailure();
     * </code>
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
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.failure("yay")).hasFailure("yay");
     * assertThat(DefaultResult.failure(1234)).hasFailure(1234);
     * </code>
     * </pre>
     *
     * Assertions will fail:
     * 
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.failure("yay")).hasFailure("nay");
     * assertThat(DefaultResult.failure(null)).hasFailure("null");
     * assertThat(DefaultResult.failure(1234)).hasFailure(4321);
     * assertThat(DefaultResult.success("yay")).hasFailure("yay");
     * </code>
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
     * Assertions will pass:
     *
     * <pre>
     * <code class='java'>
     * String foobar = "foobar";
     * assertThat(DefaultResult.failure(foobar)).hasFailureSameAs(foobar);
     * assertThat(DefaultResult.failure(10)).hasFailureSameAs(10);
     * </code>
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre>
     * <code class='java'>
     * // not equal:
     * assertThat(DefaultResult.failure("yay")).hasFailureSameAs("nay");
     * assertThat(DefaultResult.failure(1234)).hasFailureSameAs(4321);
     *
     * // equal but not failed:
     * String foo = "FOO";
     * assertThat(DefaultResult.success(foo)).hasFailureSameAs(foo);
     * 
     * // equal but not the same:
     * assertThat(DefaultResult.failure(new String("bar"))).hasFailureSameAs(new String("bar"));
     * assertThat(DefaultResult.failure(new Integer(1234))).hasFailureSameAs(new Integer(1234));
     * </code>
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
     * failure value (may be {@code null}) for further assertions.
     * <p>
     * Should be used as a way of deeper asserting on the containing object, as further requirement(s) for the value.
     * <p>
     * Assertions will pass:
     * 
     * <pre>
     * <code class='java'>
     * // one requirement
     * assertThat(DefaultResult.failure(10)).hasFailureSatisfying(s -&gt; { assertThat(s).isGreaterThan(9); });
     * assertThat(DefaultResult.failure(null)).hasFailureSatisfying(s -&gt; { assertThat(s).isNull(); });
     *
     * // multiple requirements
     * assertThat(DefaultResult.failure("hello")).hasFailureSatisfying(s -&gt; {
     *   assertThat(s).isEqualTo("hello");
     *   assertThat(s).startsWith("h");
     *   assertThat(s).endsWith("o");
     * });
     * </code>
     * </pre>
     *
     * Assertions will fail:
     * 
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.success("hello")).hasSuccessSatisfying(s -&gt; assertThat(s).isEqualTo("hello"); });
     * assertThat(DefaultResult.failure("hello")).hasSuccessSatisfying(o -&gt; {});
     * </code>
     * </pre>
     *
     * @param requirement to further assert on the failure value held by the {@link Result}; can't be {@code null}.
     * @return this assertion object.
     */
    public SELF hasFailureSatisfying(Consumer<F> requirement) {
        final F value = this.assertIsFailure();
        requirement.accept(value);
        return myself;
    }

    /**
     * Verifies that the actual {@link Result} is a failed result whose failure value (may be {@code null}) satisfies
     * the given {@link Condition}.
     * <p>
     * Assertions will pass:
     * 
     * <pre>
     * <code class='java'>
     * Condition&lt;Integer&gt; isNegative = new Condition&lt;&gt;(i -&gt; i &lt; 0, "a negative number");
     * Condition&lt;Object&gt; isNull = new Condition&lt;&gt;(o -&gt; o == null, "a null reference");
     * assertThat(DefaultResult.failure(-1)).hasFailureSatisfying(isNegative);
     * assertThat(DefaultResult.failure(null)).hasFailureSatisfying(isNull);
     * </code>
     * </pre>
     *
     * Assertions will fail:
     * 
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.failure(1234)).hasFailureSatisfying(isNegative);
     * assertThat(DefaultResult.success(-123)).hasFailureSatisfying(isNegative);
     * assertThat(DefaultResult.failure("hello")).hasFailureSatisfying(isNull);
     * assertThat(DefaultResult.success(null)).hasFailureSatisfying(isNull);
     * </code>
     * </pre>
     *
     * @param condition the given condition; can't be {@code null}.
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
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.failure("hello"))
     *         .hasFailureInstanceOf(String.class)
     *         .hasFailureInstanceOf(Object.class);
     * assertThat(DefaultResult.failure(123)).hasSuccessInstanceOf(Integer.class);
     * </code>
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre>
     * <code class='java'>
     * assertThat(DefaultResult.failure("hello")).hasFailureInstanceOf(Integer.class);
     * assertThat(DefaultResult.failure("hello")).hasFailureInstanceOf(String.class);
     * </code>
     * </pre>
     *
     * @param clazz the expected class of the failure value inside the {@link Result}; can't be {@code null}.
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
     * <pre>
     * <code class='java'>
     * Foo foo = new Foobar("Hello", "world");
     * 
     * assertThat(DefaultResult.failure(foo)).hasFailureThat().hasNoNullFieldsOrProperties();
     * assertThat(DefaultResult.failure(null)).hasFailureThat().isNull();
     * </code>
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre>
     * <code class='java'>
     * Foo bar = new Foobar("Hello", null);
     * 
     * assertThat(DefaultResult.failure(bar)).hasFailureThat().hasNoNullFieldsOrProperties();
     * assertThat(DefaultResult.success(null)).hasFailureThat().isNotNull();
     * </code>
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
     * <pre>
     * <code class='java'>
     * assertThat(failure(0)).hasFailureThat(as(InstanceOfAssertFactories.INTEGER)).isZero();
     * assertThat(DefaultResult.failure("hello")).hasFailureThat(as(InstanceOfAssertFactories.STRING)).startsWith("h");
     * </code>
     * </pre>
     *
     * Assertions will fail:
     *
     * <pre>
     * <code class='java'>
     * assertThat(failure("hello")).hasFailureThat(as(InstanceOfAssertFactories.INTEGER)).isZero();
     * assertThat(DefaultResult.success("hello")).hasFailureThat(as(InstanceOfAssertFactories.STRING)).startsWith("h");
     * </code>
     * </pre>
     *
     * @param <T> the type of the resulting {@code Assert}
     * @param assertFactory the factory which verifies the type and creates the new {@code Assert}; can't be
     *            {@code null}.
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
