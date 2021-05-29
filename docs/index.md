---
title: Result Library for Java
description: A Java library to handle success and failure without exceptions
image: /result-banner-centered.png
---

# Result Library for Java

The purpose of this library is to provide a type-safe encapsulation of operation results that may have succeeded or
failed, instead of throwing exceptions.

If you like `Optional` but feel that it sometimes falls too short, you'll love `Result`.

The best way to think of _Result_ is as a super-powered version of _Optional_. The only difference is that whereas
_Optional_ may contain a successful value or express the absence of a value, _Result_ contains either a successful value
or a failure value that explains what went wrong.

<details style="margin-bottom: 20px">
 <summary style="display: list-item"><code>Result</code> objects have methods equivalent to those in
 <code>Optional</code>, plus a few more to handle failure values.</summary>
 <div markdown="1">

| Optional                | Result                  |
|-------------------------|-------------------------|
| `isPresent`             | `isSuccess`             |
| `isEmpty`               | `isFailure`             |
| `get`                   |                         |
| `orElse`                | `orElse`                |
| `orElseGet`             | `orElseMap`             |
| `orElseThrow`           | `orElseThrow`           |
| `orElseThrow(Supplier)` | `orElseThrow(Function)` |
|                         | `getFailureOrElseThrow` |
|                         | `optional`              |
|                         | `optionalFailure`       |
| `stream`                | `stream`                |
|                         | `streamFailure`         |
| `ifPresent`             | `ifSuccess`             |
|                         | `ifFailure`             |
| `ifPresentOrElse`       | `ifSuccessOrElse`       |
| `filter`                | `filter`                |
| `map`                   | `mapSuccess`            |
|                         | `mapFailure`            |
|                         | `map`                   |
| `flatMap`               | `flatMapSuccess`        |
| `or`                    | `flatMapFailure`        |
|                         | `flatMap`               |

 </div>
</details>

---


## Adding Result to Your Build

The library requires JDK 1.8 or higher. Other than that, it has no external dependencies and it is very lightweight.
Adding it to your build should be very easy.

Artifact coordinates:

- Group ID: `com.leakyabstractions`
- Artifact ID: `result`
- Version: `{{ site.current_version }}`

To add a dependency on _Result_ using [**Maven**](https://maven.apache.org/), use the following:

```xml
<dependencies>
    <dependency>
        <groupId>com.leakyabstractions</groupId>
        <artifactId>result</artifactId>
        <version>{{ site.current_version }}</version>
        <type>pom</type>
    </dependency>
</dependencies>
```

To add the dependency using [**Gradle**](https://gradle.org/), if you are building an application that will use _Result_
internally:

```gradle
dependencies {
    implementation 'com.leakyabstractions:result:{{ site.current_version }}'
}
```

If you are building a library that will use _Result_ type in its public API, you should use instead:

```gradle
dependencies {
    api 'com.leakyabstractions:result:{{ site.current_version }}'
}
```

For more information on when to use _api_ and _implementation_, read the [Gradle documentation on API and implementation
separation](https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_separation).


## Creating Result Objects

There are several ways of creating _Result_ objects.

To create a successful result, we simply need to use static method [`Results.success()`][NEW_SUCCESS]:

```java
@Test
void should_be_success() {
    // When
    final Result<Integer, ?> result = Results.success(123);
    // Then
    assertThat(result.isSuccess()).isTrue();
}
```

Note that we can use methods [`isSuccess()`][IS_SUCCESS]  or [`isFailure()`][IS_FAILURE] to check if the result was
successful or not.

On the other hand, if we want to create a failed result, we can use static method [`Results.failure()`][NEW_FAILURE]:

```java
@Test
void should_not_be_success() {
    // When
    final Result<?, String> result = Results.failure("The operation failed");
    // Then
    assertThat(result.isSuccess()).isFalse();
}
```

We can also use static method [`Results.ofOptional()`][OF_OPTIONAL] to create results that depend on an optional value:

```java
@Test
void should_be_failure() {
    // Given
    final Optional<String> optional = Optional.empty();
    // When
    final Result<String, Void> result = Results.ofOptional(optional);
    // Then
    assertThat(result.isFailure()).isTrue();
}
```

And sometimes it might come in handy to encapsulate actual thrown exceptions inside a result object via static method
[`Results.wrap()`][WRAP]:

```java
@Test
void should_be_failure_too() {
    // Given
    final Callable<String> callable = () -> { throw new RuntimeException("Whoops!") };
    // When
    final Result<String, Exception> result = Results.wrap(callable);
    // Then
    assertThat(result.isFailure()).isTrue();
}
```

There's also a way to encapsulate expensive operations that can be entirely omitted (as an optimization) if there's no
actual need to examine the result. To create a _lazy_ result we need to use static method [`Results.lazy()`][LAZY]:

```java
    Result<String, Void> expensiveCalculation(AtomicLong timesExecuted) {
        timesExecuted.getAndIncrement();
        return Results.success("HELLO");
    }

    @Test
    void should_not_execute_expensive_action() {
        final AtomicLong timesExecuted = new AtomicLong();
        // Given
        final Result<String, Void> lazy = Results
                .lazy(() -> this.expensiveCalculation(timesExecuted));
        // When
        final Result<Integer, Void> transformed = lazy.mapSuccess(String::length);
        // Then
        assertThat(transformed).isNotNull();
        assertThat(timesExecuted).hasValue(0);
    }
```

Lazy results can be manipulated just like any other result; they will try to defer the invocation of the given supplier
as long as possible. For example, when we actually try to determine if the operation succeeded or failed.

```java
    @Test
    void should_execute_expensive_action() {
        final AtomicLong timesExecuted = new AtomicLong();
        // Given
        final Result<String, Void> lazy = Results
                .lazy(() -> this.expensiveCalculation(timesExecuted));
        // When
        final Result<Integer, Void> transformed = lazy.mapSuccess(String::length);
        final boolean success = transformed.isSuccess();
        // Then
        assertThat(success).isTrue();
        assertThat(timesExecuted).hasValue(1);
    }
```


## Conditional Actions

The `if...` family of methods enable us to run some code on the wrapped success/failure value. Before _Result_, we would
wrap exception-throwing `foobar` method invocation inside a `try` block so that errors can be handled inside a `catch`
block:

```java
try {
    final String result = foobar();
    this.commit(result);
} catch(SomeException problem) {
    this.rollback(problem);
}
```

Let's now look at how the above code could be refactored if method `foobar` returned a _Result_ object instead of
throwing an exception:

```java
final Result<String, SomeFailure> result = foobar();
result.ifSuccessOrElse(this::commit, this::rollback);
```

The first action passed to [`ifSuccessOrElse()`][IF_SUCCESS_OR_ELSE] will be performed if `foobar` succeeded;
otherwise the second one will.

The above example is not only shorter but also faster. We can make it even shorter by chaining methods in typical
functional programming style:

```java
foobar().ifSuccessOrElse(this::commit, this::rollback);
```

There are other methods [`ifSuccess()`][IF_SUCCESS] and [`ifFailure()`][IF_FAILURE] to handle either one of the success/
failure cases only:

```java
foobar(123)
    .ifSuccess(this::commit) // commits only if the result is success
    .ifFailure(this::rollback); // rolls back only if the result is failure
```


## Unwrapping Values

The [`Optional::orElse`](https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/util/Optional.html#orElse(T))
method is used to retrieve the value wrapped inside an `Optional` instance, or a _default value_ in case the optional is
empty.

Similarly, you can use [`orElse()`][OR_ELSE] to obtain the success value held by a _Result_ object; or a _default
success value_ in case the result is failed.

```java
@Test
void should_return_the_success_value() {
    // Given
    final Result<String, Integer> result = success("HELLO");
    // When
    final String greeting = result.orElse("HI");
    // Then
    assertThat(greeting).isEqualTo("HELLO");
}

@Test
void should_return_the_default_value() {
    // Given
    final Result<String, Integer> result = failure(1024);
    // When
    final String greeting = result.orElse("HI");
    // Then
    assertThat(greeting).isEqualTo("HI");
}
```

The [`orElseMap()`][OR_ELSE_MAP] method is similar to `orElse`. However, instead of taking a value to return if the
_Result_ object is failed, it takes a mapping function, which would be applied to the failure value to produce an
alternative success value:

```java
@Test
void should_map_the_failure_value() {
    // Given
    final Result<String, Boolean> result = failure(false);
    // When
    final String greeting = result.orElseMap(s -> s ? "HI" : "HOWDY");
    // Then
    assertThat(text).isEqualTo("HOWDY");
}
```

The [`orElseThrow()`][OR_ELSE_THROW] methods follow from `orElse` and `orElseMap` and add a new approach for handling a
failed result.

Instead of returning a default value, they throw an exception. If you [provide a mapping function][OR_ELSE_THROW_WITH_MAPPER]
you can transform the failure value to the appropriate exception to be thrown. If you don't, then
[_NoSuchElementException_](https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/util/NoSuchElementException.html)
will be thrown.

```java
@Test
void should_throw_exception() {
    // Given
    final Result<Integer, String> result = failure("Could not compute result");
    // When
    final ThrowingCallable callable = () -> result.orElseThrow();
    // Then
    assertThatThrownBy(callable).isInstanceOf(NoSuchElementException.class);
}

@Test
void should_return_success_value() {
    // Given
    final Result<Integer, String> result = success(0);
    // When
    final Integer number = result.orElseThrow(IllegalArgumentException::new);
    // Then
    assertThat(number).isZero();
}
```

Method [`getFailureOrElseThrow()`][GET_FAILURE_OR_ELSE_THROW] is the counterpart of `orElseThrow`; it will return the
failure value unless the result is successful:

```java
@Test
void should_return_failure_value() {
    // Given
    final Result<Integer, String> result = failure("NETWORK PROBLEM");
    // When
    final String error = result.getFailureOrElseThrow();
    // Then
    assertThat(error).isEqualTo("NETWORK PROBLEM");
}

@Test
void should_throw_exception() {
    // Given
    final Result<Integer, String> result = success(0);
    // When
    final ThrowingCallable callable = () -> result.getFailureOrElseThrow();
    // Then
    assertThatThrownBy(callable).isInstanceOf(NoSuchElementException.class);
}
```


## Filtering Success Values

We can run an inline test on our wrapped success value with the [`filter()`][FILTER] method. It takes a predicate and a
mapping function as arguments and returns a _Result_ object:

- If it is a failed result, or it is a successful result whose success value passes testing by the predicate then the
  _Result_ is returned as-is.
- If the predicate returns `false` then the mapping function will be applied to the success value to produce a failure
  value that will be wrapped in a new failed result.

```java
@Test
void should_pass_test() {
    // Given
    final Result<Integer, String> result = success(-1);
    final Predicate<Integer> isPositive = i -> i >= 0;
    // When
    final Result<Integer, String> filtered = result.filter(isPositive, i -> "Negative number");
    // Then
    assertThat(filtered.isFailure()).isTrue();
}
```

The `filter` method is normally used to reject wrapped success values based on a predefined rule.


## Transforming Values

In the previous section, we looked at how to reject or accept a success value based on a filter.

We can also transform success/failure values held by _Result_ objects with the `map...` family of methods:

```java
@Test
void should_return_string_length() {
    // Given
    final Result<String, Integer> result = success("ABCD");
    // When
    final Result<Integer, String> mapped = result.mapSuccess(String::length);
    // Then
    assertThat(mapped.orElseThrow()).isEqualTo(4);
}
```

In this example, we wrap a string inside a _Result_ object and use its [`mapSuccess()`][MAP_SUCCESS] method to
manipulate it (here we calculate its length). Note that we can specify the action as a method reference, or a a lambda.
In any case, the result of this action gets wrapped inside a new _Result_ object. And then we call the appropriate
method on the returned result to retrieve its value.

There is another [`map()`][MAP] method to transform either success/failure value at once:

```java
@Test
void should_return_upper_case() {
    // Given
    final Result<String, String> result = success("Hello World!");
    // When
    final Result<String, String> mapped = result
        .map(String::toUpperCase, String::toLowerCase);
    // Then
    assertThat(mapped.orElseThrow()).isEqualTo("HELLO WORLD!");
}

@Test
void should_return_lower_case() {
    // Given
    final Result<String, String> result = failure("Hello World!");
    // When
    final Result<String, String> mapped = result
        .map(String::toUpperCase, String::toLowerCase);
    // Then
    assertThat(mapped.getFailureOrElseThrow()).isEqualTo("hello world!");
}
```

And the [`mapFailure()`][MAP_FAILURE] method allows us to transform failure values only:

```java
@Test
void should_return_is_empty() {
    // Given
    final Result<Integer, String> result = failure("");
    // When
    final Result<Integer, Boolean> mapped = result.mapFailure(String::isEmpty);
    // Then
    assertThat(mapped.getFailureOrElseThrow()).isTrue();
}
```

Just like the `map...` methods, we also have the `flatMap...` family of methods as an alternative for transforming
values. The difference is that `map...` methods don't alter the success/failure state of the result, whereas with
`flatMap...` ones, you can start with a successful result and end up with a failed one, and _vice versa_.

Previously, we created simple `String` and `Integer` objects for wrapping in a _Result_ instance. However, frequently,
we will receive these objects as we invoke third-party methods.

To get a clearer picture of the difference, let's have a look at a `User` object that takes a _name_ and a boolean flag
that determines if the user has custom configuration. It also has a method `getCustomConfigPath` which returns a
_Result_ containing either the path to the user configuration file, or a `Problem` object describing why the path cannot
be obtained:

```java
class User {

    final String name;
    final boolean hasCustomConfig;

    public User(String name, boolean hasCustomConfig){
        this.name = name;
        this.hasCustomConfig = hasCustomConfig;
    }

    public Result<String, Problem> getCustomConfigPath() {
        if (!this.hasCustomConfig) {
            return failure(new UserProblem("User does not have custom configuration"));
        }
        return success("/config/" + this.name + ".cfg");
    }
}
```

Now suppose we have a method `openFile` which checks if a given file exists and returns a result containing the file
object or a `Problem` object explaining why the file cannot be retrieved:

```java
Result<File, Problem> openFile(String path) {
    final File file = new File(path);
    return file.exists() ? success(file) : failure(new FileProblem("File does not exist"));
}
```

If we wanted to obtain the file path from the user _and then_ invoke the above method to get the file object, we could
use [`flatMapSuccess`][FLATMAP_SUCCESS] to fluently transform one result into another:

```java
@Test
void should_contain_file() {
    // Given
    final User user = new User("Rachel", true);
    // When
    final Result<File, Problem> result = user.getCustomConfigPath()
        .flatMapSuccess(this::openFile);
    // Then
    assertThat(result.orElseThrow()).isAbsolute();
}

@Test
void should_contain_user_problem() {
    // Given
    final User user = new User("Monica", false);
    // When
    final Result<File, Problem> result = user.getCustomConfigPath()
        .flatMapSuccess(this::openFile);
    // Then
    assertThat(result.getFailureOrElseThrow()).isInstanceOf(UserProblem.class);
}

@Test
void should_contain_file_problem() {
    // Given
    final User user = new User("../../wrong//path/", true);
    // When
    final Result<File, Problem> result = user.getCustomConfigPath()
        .flatMapSuccess(this::openFile);
    // Then
    assertThat(result.getFailureOrElseThrow()).isInstanceOf(FileProblem.class);
}
```

There is another [`flatMap`][FLATMAP] method to transform either success/failure values at once:

```java
@Test
void should_contain_123() {
    // Given
    final User user = new User("Phoebe", false);
    // When
    final Result<File, Integer> result = user.getCustomConfigPath()
        .flatMap(this::openFile, f -> 123);
    // Then
    assertThat(result.getFailureOrElseThrow()).isEqualTo(123);
}
```

And the [`flatMapFailure()`][FLATMAP_FAILURE] method allows us to transform failure values only:

```java
@Test
void should_contain_error() {
    // Given
    final User user = new User("Joey", false);
    // When
    final Result<String, String> result = user.getCustomConfigPath()
        .flatMapFailure(f -> "error");
    // Then
    assertThat(result.getFailureOrElseThrow()).isEqualTo("error");
}
```


## Asserting Result objects

You can use fluent assertions (based on [AssertJ](https://assertj.github.io/)) for Result objects in your unit tests.

To add a dependency on Result assertions using **Maven**, use the following:

```xml
<dependency>
    <groupId>com.leakyabstractions</groupId>
    <artifactId>result-assertj</artifactId>
    <version>{{ site.current_version }}</version>
    <scope>test</scope>
</dependency>
```

To add a dependency using **Gradle**:

```gradle
dependencies {
    testImplementation 'com.leakyabstractions:result-assertj:{{ site.current_version }}'
}
```

This allows you to use _Result_ assertions in your tests via `assertThat`:

```java
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;

@Test
public void should_pass() {
    // Given
    final int number = someMethodReturningInt();
    // When
    final Result<String, Integer> result = someMethodReturningResult(number);
    // Then
    assertThat(number).isZero();
    assertThat(result).hasSuccess("OK");
}
```

If, for some reason, you cannot statically import static method `ResultAssertions.assertThat()` you can use static
method `ResultAssert.assertThatResult()` instead:

```java
import static com.leakyabstractions.result.assertj.ResultAssert.assertThatResult;
import static org.assertj.core.api.Assertions.assertThat;

@Test
public void should_pass_too() {
    // Given
    final int number = anotherMethodReturningInt();
    // When
    final Result<String, Integer> result = anotherMethodReturningResult(number);
    // Then
    assertThat(number).isOne();
    assertThatResult(result).hasFailure(1);
}
```


## Releases

This library adheres to [Pragmatic Versioning](https://pragver.github.io/).

Artifacts are available in [Maven Central](https://search.maven.org/artifact/com.leakyabstractions/result).


## Javadoc

Here's the full
[Result API documentation](https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/).


## Looking for Support?

We'd love to help. Check out the [support guidelines](SUPPORT.md).


## Contributions Welcome

If you'd like to contribute to this project, please [start here](CONTRIBUTING.md).


## Code of Conduct

This project is governed by the [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are
expected to uphold this code.


[NEW_SUCCESS]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Results.html#success(S)
[NEW_FAILURE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Results.html#failure(F)
[OF_OPTIONAL]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Results.html#ofOptional(java.util.Optional)
[WRAP]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Results.html#wrap(java.util.concurrent.Callable)
[LAZY]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Results.html#lazy(java.util.function.Supplier)
[IS_SUCCESS]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#isSuccess()
[IS_FAILURE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#isFailure()
[IF_SUCCESS_OR_ELSE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#ifSuccessOrElse(java.util.function.Consumer,java.util.function.Consumer)
[IF_SUCCESS]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#ifSuccess(java.util.function.Consumer)
[IF_FAILURE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#ifFailure(java.util.function.Consumer)
[OR_ELSE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#orElse(S)
[OR_ELSE_MAP]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#orElseMap(java.util.function.Function)
[OR_ELSE_THROW]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#orElseThrow()
[OR_ELSE_THROW_WITH_MAPPER]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#orElseThrow(java.util.function.Function)
[GET_FAILURE_OR_ELSE_THROW]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#getFailureOrElseThrow()
[FILTER]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#filter(java.util.function.Predicate,java.util.function.Function)
[MAP]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#map(java.util.function.Function,java.util.function.Function)
[MAP_SUCCESS]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#mapSuccess(java.util.function.Function)
[MAP_FAILURE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#mapFailure(java.util.function.Function)
[FLATMAP]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#flatMap(java.util.function.Function,java.util.function.Function)
[FLATMAP_SUCCESS]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#flatMapSuccess(java.util.function.Function)
[FLATMAP_FAILURE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#flatMapFailure(java.util.function.Function)
