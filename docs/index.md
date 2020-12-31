
# Result Library for Java

The purpose of this library is to provide a type-safe encapsulation of operation results that may have succeeded or
failed, instead of throwing exceptions.

If you like `Optional` but feel that it sometimes falls too short, you'll love `Result`.


## Adding Result to Your Build

The library has no external dependencies and it is very lightweight. Adding it to your build should be very easy.

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

And make sure [**Bintray JCenter**](https://bintray.com/bintray/jcenter) is in your repository list:

```xml
<repositories>
    <repository>
        <id>central</id>
        <name>bintray</name>
        <url>https://jcenter.bintray.com</url>
    </repository>
</repositories>
```

To add the dependency using [**Gradle**](https://gradle.org/), if you are building an application that will use _Result_
internally:

```gradle
dependencies {
    implementation 'com.leakyabstractions:result:{{ site.current_version }}'
}
```

And make sure **Bintray JCenter** is in your repository list:

```gradle
repositories {
    jcenter()
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

To create a successful result, we simply need to use static method [`DefaultResult.success()`][NEW_SUCCESS]:

```java
@Test
void should_be_success() {
    // When
    Result<Integer, ?> result = DefaultResult.success(123);
    // Then
    assertThat(result.isSuccess()).isTrue();
}
```

Note that we can use methods [`isSuccess()`][IS_SUCCESS]  or [`isFailure()`][IS_FAILURE] to check if the result was
successful or not.

On the other hand, if we want to create a failed result, we can use static method
[`DefaultResult.failure()`][NEW_FAILURE]:

```java
@Test
void should_not_be_success() {
    // When
    Result<?, String> result = DefaultResult.failure("The operation failed");
    // Then
    assertThat(result.isSuccess()).isFalse();
}
```

We can also use static method [`DefaultResult.ofOptional()`][OF_OPTIONAL] to create results that depend on an optional
value:

```java
@Test
void should_be_failure() {
    // Given
    Optional<String> optional = Optional.empty();
    // When
    Result<String, Void> result = DefaultResult.ofOptional(optional);
    // Then
    assertThat(result.isFailure()).isTrue();
}
```

And sometimes it might come in handy to encapsulate actual thrown exceptions inside a result object via static method
[`DefaultResult.wrap()`][WRAP]:

```java
@Test
void should_be_failure_too() {
    // Given
    Callable<String> callable = () -> { throw new RuntimeException("Whoops!") };
    // When
    Result<String, Exception> result = DefaultResult.wrap(callable);
    // Then
    assertThat(result.isFailure()).isTrue();
}
```


## Conditional Actions

The `if...` family of methods enable us to run some code on the wrapped success/failure value. Before _Result_, we
would do:

```java
String result = null;
try {
    result = this.someMethod();
    this.commit(result);
} catch(SomeException problem) {
    this.rollback(problem);
}
return result != null;
```

This code wraps `someMethod` invocation inside a `try` block so that errors can be handled inside the `catch` block.
This approach is lengthy, and that's not the only problem -- it's also slow. Conventional wisdom says that exceptional
logic shouldn't be used for normal program flow.

_Result_ makes us deal with expected, non-exceptional error situations explicitly as a way of enforcing good programming
practices.

Let's now look at how the above code could be refactored with _Result_:

```java
Result<String, SomeFailure> result = this.someMethod();
result.ifSuccessOrElse(this::commit, this::rollback);
return result.isSuccess();
```

The first action passed to [`ifSuccessOrElse()`][IF_SUCCESS_OR_ELSE] will be performed if `someMethod` succeeded;
otherwise the second one will.

The above example not only shorter but also faster. We can make it even shorter by chaining methods in typical
functional programming style:

```java
return this.someMethod().ifSuccessOrElse(this::commit, this::rollback).isSuccess();
```

There are other methods [`ifSuccess()`][IF_SUCCESS] and [`ifFailure()`][IF_FAILURE] to handle either one of the success/
failure cases only:

```java
return this.someMethod(123)
    .ifSuccess(this::commit) // commits only if the result is success
    .ifFailure(this::rollback) // rolbacks only if the result is failure
    .isSuccess();
```


## Unwrapping Values

The [`Optional::orElse`](https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/util/Optional.html#orElse(T))
method is used to retrieve the value wrapped inside an `Optional` instance, or a _default value_ in case the optional is
empty.

Similarly you can use [`orElse()`][OR_ELSE] to obtain the success value held by a _Result_ object; or a _default success
value_ in case the result is failed.

```java
@Test
void should_return_the_success_value() {
    // Given
    Result<String, Integer> result = success("HELLO");
    // When
    String greeting = result.orElse("HI");
    // Then
    assertThat(greeting).isEqualTo("HELLO");
}

@Test
void should_return_the_default_value() {
    // Given
    Result<String, Integer> result = failure(1024);
    // When
    String greeting = result.orElse("HI");
    // Then
    assertThat(greeting).isEqualTo("HI");
}
```

The [`orElseMap()`][OR_ELSE_MAP] method is similar to `orElse`. However, instead of taking a value to return if the
_Result_ object is failed, it takes a mapping function, which is invoked with the failure value and returns the mapped
value:

```java
@Test
void should_map_the_failure_value() {
    // Given
    Result<String, Boolean> result = failure(false);
    // When
    String greeting = result.orElseMap(s -> s ? "HI" : "HOWDY");
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
    Result<Integer, String> result = failure("Could not compute result");
    // When
    ThrowingCallable callable = () -> result.orElseThrow();
    // Then
    assertThatThrownBy(callable).isInstanceOf(NoSuchElementException.class);
}

@Test
void should_return_success_value() {
    // Given
    Result<Integer, String> result = success(0);
    // When
    Integer number = result.orElseThrow(IllegalArgumentException::new);
    // Then
    assertThat(number).isZero();
}
```

Method [`getFailureOrElseThrow()`][GET_FAILURE_OR_ELSE_THROW] is the counterpart of `orElseThrow`; it can only return a
value if the result is failure:

```java
@Test
void should_return_failure_value() {
    // Given
    Result<Integer, String> result = failure("NETWORK PROBLEM");
    // When
    String error = result.getFailureOrElseThrow();
    // Then
    assertThat(error).isEqualTo("NETWORK PROBLEM");
}

@Test
void should_throw_exception() {
    // Given
    Result<Integer, String> result = success(0);
    // When
    ThrowingCallable callable = () -> result.getFailureOrElseThrow();
    // Then
    assertThatThrownBy(callable).isInstanceOf(NoSuchElementException.class);
}
```


## Filtering Success Values

We can run an inline test on our wrapped success value with the [`filter()`][FILTER] method. It takes a predicate and a
mapping function as arguments and returns a _Result_ object:

- If result is failure or the wrapped success value passes testing by the predicate then the _Result_ is returned as-is.
- If the predicate returns `false` then the mapping function will be invoked with the success value and the returned
  failure value will be wrapped in a new failed result.

```java
@Test
void should_pass_test() {
    // Given
    Result<Integer, String> result = success(-1);
    Predicate<Integer> isPositive = i -> i >= 0;
    // When
    Result<Integer, String> filtered = result.filter(isPositive, i -> "NEGATIVE NUMBER");
    // Then
    assertThat(filtered.isFailure()).isTrue();
}
```

The `filter` method is normally used to reject wrapped success values based on a predefined rule.


## Transforming Values

In the previous section, we looked at how to reject or accept a success value based on a filter.

We can use a similar syntax to transform the _Result_ value with the `map` family of methods:

```java
@Test
void should_return_string_length() {
    // Given
    Result<String, Integer> result = success("ABCD");
    // When
    Result<Integer, String> mapped = result.map(String::length);
    // Then
    assertThat(mapped.orElseThrow()).isEqualTo(4);
}
```

In this example, we wrap a string inside a _Result_ object and use its [`map()`][MAP] method to perform an action on the
contained string. The action we perform is to retrieve the length of the string.

The `map` method returns the result of the computation wrapped inside _Result_. We then have to call an appropriate
method on the returned result to retrieve its value.

There is another [`map()`][MAP_SUCCESS] method to transform either success/failure value at once:

```java
@Test
void should_return_upper_case() {
    // Given
    Result<String, String> result = success("Hello World!");
    // When
    Result<String, String> mapped = result
        .map(String::toUpperCase, String::toLowerCase);
    // Then
    assertThat(mapped.orElseThrow()).isEqualTo("HELLO WORLD!");
}

@Test
void should_return_lower_case() {
    // Given
    Result<String, String> result = failure("Hello World!");
    // When
    Result<String, String> mapped = result
        .map(String::toUpperCase, String::toLowerCase);
    // Then
    assertThat(mapped.getFailure()).isEqualTo("hello world!");
}
```

And the [`mapFailure()`][MAP_FAILURE] method allows us to transform failure values only:

```java
@Test
void should_return_is_empty() {
    // Given
    Result<Integer, String> result = failure("");
    // When
    Result<Integer, Boolean> mapped = result.mapFailure(String::isEmpty);
    // Then
    assertThat(mapped.getFailure()).isTrue();
}
```

Just like the `map` methods, we also have the `flatMap` family of methods as an alternative for transforming values. The
difference is that `map` does not alter the success/failure state of the result, whereas with `flatMap`, you can start
with a successful result and end up with a failed one, and _vice versa_.

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

Now suposse we have a method `openFile` which checks if a given file exists and returns a result containingvthe file
object or a `Problem` object explaining why the file cannot be retrieved:

```java
Result<File, Problem> openFile(String path) {
    File file = new File(path);
    return file.exists() ? success(file) : failure(new FileProblem("File does not exist"));
}
```

If we wanted to obtain the file path from the user _and then_ invoke the above method to get the file object, we could
use [`flatMap`][FLATMAP_SUCCESS] to fluently transform one result into another:

```java
@Test
void should_contain_file() {
    // Given
    User user = new User("Rachel", true);
    // When
    Result<File, Problem> result = user.getCustomConfigPath()
        .flatMap(this::openFile);
    // Then
    assertThat(result.orElseThrow()).isAbsolute();
}

@Test
void should_contain_user_problem() {
    // Given
    User user = new User("Monica", false);
    // When
    Result<File, Problem> result = user.getCustomConfigPath()
        .flatMap(this::openFile);
    // Then
    assertThat(result.getFailure()).isInstanceOf(UserProblem.class);
}

@Test
void should_contain_file_problem() {
    // Given
    User user = new User("../../wrong//path/", true);
    // When
    Result<File, Problem> result = user.getCustomConfigPath()
        .flatMap(this::openFile);
    // Then
    assertThat(result.getFailure()).isInstanceOf(FileProblem.class);
}
```

There is another [`flatMap`][FLATMAP] method to transform either success/failure values at once:

```java
@Test
void should_contain_false() {
    // Given
    User user = new User("Phoebe", false);
    // When
    Result<File, Boolean> result = user.getCustomConfigPath()
        .flatMap(this::openFile, Objects::isNull);
    // Then
    assertThat(result.getFailure()).isFalse();
}
```

And the [`flatMapFailure()`][FLATMAP_FAILURE] method allows us to transform failure values only:

```java
@Test
void should_contain_true() {
    // Given
    User user = new User("Joey", false);
    // When
    Result<String, Boolean> result = user.getCustomConfigPath()
        .flatMapFailure(Objects::nonNull);
    // Then
    assertThat(result.getFailure()).isTrue();
}
```


## Asserting Result objects

You can use fluent assertions (based on [AssertJ](https://assertj.github.io/)) for Result objects in your unit tests.

To add a dependency on Result using **Maven**, use the following:

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
    int number = someMethodReturningInt();
    // When
    Result<String, Integer> result = someMethodReturningResult(number);
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
    int number = anotherMethodReturningInt();
    // When
    Result<String, Integer> result = anotherMethodReturningResult(number);
    // Then
    assertThat(number).isOne();
    assertThatResult(result).hasFailure(1);
}
```


## Javadoc

Here's the full [Result API documentation](https://dev.leakyabstractions.com/result/api/).


## Looking for Support?

We'd love to help. Check out the [support guidelines](SUPPORT.md).


## Contributions Welcome

If you'd like to contribute to this project, please [start here](CONTRIBUTING.md).


## Code of Conduct

This project is governed by the [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are
expected to uphold this code.


[NEW_SUCCESS]: api/com/leakyabstractions/result/DefaultResult.html#success(S)
[NEW_FAILURE]: api/com/leakyabstractions/result/DefaultResult.html#failure(F)
[OF_OPTIONAL]: api/com/leakyabstractions/result/DefaultResult.html#ofOptional(java.util.Optional)
[WRAP]: api/com/leakyabstractions/result/DefaultResult.html#wrap(java.util.concurrent.Callable)
[IS_SUCCESS]: api/com/leakyabstractions/result/Result.html#isSuccess()
[IS_FAILURE]: api/com/leakyabstractions/result/Result.html#isFailure()
[IF_SUCCESS_OR_ELSE]: api/com/leakyabstractions/result/Result.html#ifSuccessOrElse(java.util.function.Consumer,java.util.function.Consumer)
[IF_SUCCESS]: api/com/leakyabstractions/result/Result.html#ifSuccess(java.util.function.Consumer)
[IF_FAILURE]: api/com/leakyabstractions/result/Result.html#ifFailure(java.util.function.Consumer)
[OR_ELSE]: api/com/leakyabstractions/result/Result.html#orElse(S)
[OR_ELSE_MAP]: api/com/leakyabstractions/result/Result.html#orElseMap(java.util.function.Function)
[OR_ELSE_THROW]: api/com/leakyabstractions/result/Result.html#orElseThrow()
[OR_ELSE_THROW_WITH_MAPPER]: api/com/leakyabstractions/result/Result.html#orElseThrow(java.util.function.Function)
[GET_FAILURE_OR_ELSE_THROW]: api/com/leakyabstractions/result/Result.html#getFailureOrElseThrow()
[FILTER]: api/com/leakyabstractions/result/Result.html#filter(java.util.function.Predicate,java.util.function.Function)
[MAP]: api/com/leakyabstractions/result/Result.html#map(java.util.function.Function,java.util.function.Function)
[MAP_SUCCESS]: api/com/leakyabstractions/result/Result.html#map(java.util.function.Function)
[MAP_FAILURE]: api/com/leakyabstractions/result/Result.html#mapFailure(java.util.function.Function)
[FLATMAP]: api/com/leakyabstractions/result/Result.html#flatMap(java.util.function.Function,java.util.function.Function)
[FLATMAP_SUCCESS]: api/com/leakyabstractions/result/Result.html#flatMap(java.util.function.Function)
[FLATMAP_FAILURE]: api/com/leakyabstractions/result/Result.html#flatMapFailure(java.util.function.Function)
