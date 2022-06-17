---
title: Result Library for Java
description: A Java library to handle success and failure without exceptions
image: /result-banner-centered.png
---

# Getting Started

The purpose of this library is to provide a type-safe encapsulation of operation results that may have succeeded or
failed, instead of throwing exceptions.

If you like <tt>Optional</tt> but feel that it sometimes falls too short, you'll love <tt>Result</tt>.

The best way to think of <tt>Result</tt> is as a super-powered version of <tt>Optional</tt>. The only difference is that
whereas <tt>Optional</tt> may contain a successful value or express the absence of a value, <tt>Result</tt> contains
either a successful value or a failure value that explains what went wrong.

<details style="margin-bottom: 20px">
 <summary style="display: list-item; cursor: pointer"><code>Result</code> objects have methods equivalent to those in
 <code>Optional</code>, plus a few more to handle failure values.</summary>
 <div markdown="1">

| Optional                | Result                  |
|-------------------------|-------------------------|
| `isPresent`             | `hasSuccess`            |
| `isEmpty`               | `hasFailure`            |
| `get`                   | `getSuccess`            |
|                         | `getFailure`            |
| `orElse`                | `orElse`                |
| `orElseGet`             | `orElseMap`             |
| `orElseThrow`           |                         |
|                         | `optional`              |
| `stream`                | `stream`                |
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

To add a dependency on <tt>Result</tt> using [**Maven**](https://maven.apache.org/), use the following:

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

To add the dependency using [**Gradle**](https://gradle.org/), if you are building an application that will use
<tt>Result</tt> internally:

```gradle
dependencies {
    implementation 'com.leakyabstractions:result:{{ site.current_version }}'
}
```

If you are building a library that will use <tt>Result</tt> type in its public API, you should use instead:

```gradle
dependencies {
    api 'com.leakyabstractions:result:{{ site.current_version }}'
}
```

For more information on when to use <tt>api</tt> and <tt>implementation</tt>, read the [Gradle documentation on API and
implementation separation](https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_separation).


## Creating Result Objects

There are several ways of creating <tt>Result</tt> objects.


### Successful Results

To create a successful result, we simply need to use static method [`Results.success()`][NEW_SUCCESS]:

```java
@Test
void should_be_success() {
    // When
    final Result<Integer, ?> result = Results.success(123);
    // Then
    assertThat(result.hasSuccess()).isTrue();
}
```

Note that we can use methods [`hasSuccess()`][HAS_SUCCESS]  or [`hasFailure()`][HAS_FAILURE] to check if the result was
successful or not.


### Failed Results

On the other hand, if we want to create a failed result, we can use static method [`Results.failure()`][NEW_FAILURE]:

```java
@Test
void should_be_failure() {
    // When
    final Result<?, String> result = Results.failure("The operation failed");
    // Then
    assertThat(result.hasFailure()).isTrue();
}
```


### Results Based on Nullable Value

We can use static method [`Results.ofNullable()`][OF_NULLABLE] to create results that depend on a possibly-null value:

```java
@Test
void should_not_be_failure() {
    // Given
    final String nullable = "Hello world!";
    // When
    final Result<String, Integer> result = Results.ofOptional(nullable, 0);
    // Then
    assertThat(result.hasFailure()).isFalse();
}
```


### Results Based on Optional Value

We can also use static method [`Results.ofOptional()`][OF_OPTIONAL] to create results that depend on an optional value:

```java
@Test
void should_not_be_success() {
    // Given
    final Optional<String> optional = Optional.empty();
    // When
    final Result<String, Integer> result = Results.ofOptional(optional, -1);
    // Then
    assertThat(result.hasSuccess()).isFalse();
}
```


### Results Based on Callable Value

And sometimes it might come in handy to wrap actual thrown exceptions inside a result object via static method
[`Results.ofCallable()`][OF_CALLABLE]:

```java
@Test
void should_wrap_exception() {
    // Given
    final Callable<String> callable = () -> { throw new RuntimeException("Whoops!") };
    // When
    final Result<String, Exception> result = Results.ofCallable(callable);
    // Then
    assertThat(result.hasFailure()).isTrue();
}
```


# Basic Usage

When we have an <tt>Result</tt> instance, often we want to execute a specific action on the underlying success value. On
the other hand, if it's a failed result we may have some recovery strategy or alternative actions to take.

Other simple use-case scenarios include unwrapping the success/failure value held by the <tt>Result</tt> object.


## Conditional Actions

The <tt>if...</tt> family of methods enables us to run some code on the wrapped success/failure value. Before
<tt>Result</tt>, we would wrap exception-throwing <tt>foobar</tt> method invocation inside a <tt>try</tt> block so that
errors can be handled inside a <tt>catch</tt> block:

```java
try {
    final String result = foobar();
    this.commit(result);
} catch(SomeException problem) {
    this.rollback(problem);
}
```

Let's now look at how the above code could be refactored if method <tt>foobar</tt> returned a <tt>Result</tt> object
instead of throwing an exception:

```java
final Result<String, SomeFailure> result = foobar();
result.ifSuccessOrElse(this::commit, this::rollback);
```

The first action passed to [`ifSuccessOrElse()`][IF_SUCCESS_OR_ELSE] will be performed if <tt>foobar</tt> succeeded;
otherwise, the second one will.

The above example is not only shorter but also faster. We can make it even shorter by chaining methods in typical
functional programming style:

```java
foobar().ifSuccessOrElse(this::commit, this::rollback);
```

There are other methods [`ifSuccess()`][IF_SUCCESS] and [`ifFailure()`][IF_FAILURE] to handle either one of the success/
failure cases only:

```java
foobar()
    .ifSuccess(this::commit) // commits only if the result is success
    .ifFailure(this::rollback); // rolls back only if the result is failure
```


## Unwrapping Values

The [`Optional::orElse`](https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/util/Optional.html#orElse(T))
method is used to retrieve the value wrapped inside an <tt>Optional</tt> instance, or a _default value_ in case the
<tt>Optional</tt> is empty.

Similarly, you can use [`orElse()`][OR_ELSE] to obtain the success value held by a <tt>Result</tt> object; or a _default
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

The [`orElseMap()`][OR_ELSE_MAP] method is similar to <tt>orElse()</tt>. However, instead of taking a value to return if
the <tt>Result</tt> object is failed, it takes a mapping function, which would be applied to the failure value to
produce an alternative success value:

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

Finally, the method [`optional()`][OPTIONAL] and [`stream()`][STREAM] can be used to wrap success values held by an
instance of <tt>Result</tt> in <tt>Optional</tt> or <tt>Stream</tt> objects.

```java
@Test
void success_should_be_present() {
    // Given
    final Result<String, Integer> result = success("HI");
    // When
    final Optional<String> optional = result.optional();
    // Then
    assertThat(optional).isPresent();
}

@Test
void success_should_be_streamed() {
    // Given
    final Result<Integer, String> result = success(123);
    // When
    final Stream<Integer> stream = result.stream();
    // Then
    assertThat(stream).containsExactly(123);
}
```


# Advanced Usage

While retrieving success/failure values out of <tt>Result</tt> objects can be convenient sometimes, what's in fact more
idiomatic is to manipulate the value inside a result without actually unwrapping it. Most of the times, we will apply
transformations to a _Result_ instance, obtaining a possibly different result object in return. This allows us to
compose behavior in a [monadic way](https://en.wikipedia.org/wiki/Monad_(functional_programming)).


## Filtering Success Values

We can run an inline test on our wrapped success value with the [`filter()`][FILTER] method. It takes a predicate and a
mapping function as arguments and returns a <tt>Result</tt> object:

- If it is a failed result, or it is a successful result whose success value passes testing by the predicate then the
  <tt>Result</tt> is returned as-is.
- If the predicate returns <tt>false</tt> then the mapping function will be applied to the success value to produce a
  failure value that will be wrapped in a new failed result.

```java
@Test
void should_pass_test() {
    // Given
    final Result<Integer, String> result = success(-1);
    final Predicate<Integer> isPositive = i -> i >= 0;
    // When
    final Result<Integer, String> filtered = result.filter(isPositive, i -> "Negative number");
    // Then
    assertThat(filtered.hasFailure()).isTrue();
}
```

The <tt>filter</tt> method is normally used to reject wrapped success values based on a predefined rule.


## Transforming Values

In the previous section, we looked at how to reject or accept a success value based on a filter.

We can also transform success/failure values held by <tt>Result</tt> objects with the <tt>map...</tt> family of methods:

```java
@Test
void should_return_string_length() {
    // Given
    final Result<String, Integer> result = success("ABCD");
    // When
    final Result<Integer, String> mapped = result.mapSuccess(String::length);
    // Then
    assertThat(mapped.optional()).contains(4);
}
```

In this example, we wrap a <tt>String</tt> inside a <tt>Result</tt> object and use its [`mapSuccess()`][MAP_SUCCESS]
method to manipulate it (here we calculate its length). Note that we can specify the action as a method reference, or a
lambda. In any case, the result of this action gets wrapped inside a new <tt>Result</tt> object. And then we call the
appropriate method on the returned result to retrieve its value.

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
    assertThat(mapped.optional()).contains("HELLO WORLD!");
}

@Test
void should_return_lower_case() {
    // Given
    final Result<String, String> result = failure("Hello World!");
    // When
    final Result<String, String> mapped = result
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
    final Result<Integer, String> result = failure("");
    // When
    final Result<Integer, Boolean> mapped = result.mapFailure(String::isEmpty);
    // Then
    assertThat(mapped.getFailure()).isTrue();
}
```

Just like the <tt>map...</tt> methods, we also have the <tt>flatMap...</tt> family of methods as an alternative for
transforming values. The difference is that <tt>map...</tt> methods don't alter the success/failure state of the result,
whereas with <tt>flatMap...</tt> ones, you can start with a successful result and end up with a failed one, and _vice
versa_.

Previously, we created simple <tt>String</tt> and <tt>Integer</tt> objects for wrapping in a <tt>Result</tt> instance.
However, frequently, we will receive these objects as we invoke third-party methods.

To get a clearer picture of the difference, let's have a look at a <tt>User</tt> object that takes a <tt>name</tt> and a
boolean flag that determines if the user has custom configuration. It also has a method <tt>getCustomConfigPath</tt>
which returns a <tt>Result</tt> containing either the path to the user configuration file, or a <tt>Problem</tt> object
describing why the path cannot be obtained:

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

Now suppose we have a method <tt>openFile</tt> which checks if a given file exists and returns a result containing the
file object or a <tt>Problem</tt> object explaining why the file cannot be retrieved:

```java
Result<File, Problem> openFile(String path) {
    final File file = new File(path);
    return file.exists() ? success(file) : failure(new FileProblem("File does not exist"));
}
```

If we wanted to obtain the file path from the user _and then_ invoke the above method to get the file object, we could
use [`flatMapSuccess()`][FLATMAP_SUCCESS] to fluently transform one result into another:

```java
@Test
void should_contain_file() {
    // Given
    final User user = new User("Rachel", true);
    // When
    final Result<File, Problem> result = user.getCustomConfigPath()
        .flatMapSuccess(this::openFile);
    // Then
    assertThat(result.orElse(null)).isAbsolute();
}

@Test
void should_contain_user_problem() {
    // Given
    final User user = new User("Monica", false);
    // When
    final Result<File, Problem> result = user.getCustomConfigPath()
        .flatMapSuccess(this::openFile);
    // Then
    assertThat(result.getFailure()).isInstanceOf(UserProblem.class);
}

@Test
void should_contain_file_problem() {
    // Given
    final User user = new User("../../wrong//path/", true);
    // When
    final Result<File, Problem> result = user.getCustomConfigPath()
        .flatMapSuccess(this::openFile);
    // Then
    assertThat(result.getFailure()).isInstanceOf(FileProblem.class);
}
```

There is another [`flatMap()`][FLATMAP] method to transform either success/failure values at once:

```java
@Test
void should_contain_123() {
    // Given
    final User user = new User("Phoebe", false);
    // When
    final Result<File, Integer> result = user.getCustomConfigPath()
        .flatMap(this::openFile, f -> 123);
    // Then
    assertThat(result.getFailure()).isEqualTo(123);
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
    assertThat(result.getFailure()).isEqualTo("error");
}
```


## Fluent Assertions

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

This allows you to use <tt>Result</tt> assertions in your tests via <tt>assertThat</tt>:

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

If, for some reason, you cannot statically import static method <tt>ResultAssertions.assertThat</tt> you can use static
method <tt>ResultAssert.assertThatResult</tt> instead:

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


# Additional Info

Here's the full
[Result API documentation](https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/).


## Releases

This library adheres to [Pragmatic Versioning](https://pragver.github.io/).

Artifacts are available in [Maven Central](https://search.maven.org/artifact/com.leakyabstractions/result).


## Looking for Support?

We'd love to help. Check out the [support guidelines](SUPPORT.md).


## Contributions Welcome

If you'd like to contribute to this project, please [start here](CONTRIBUTING.md).

This project is governed by the [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are
expected to uphold this code.


[NEW_SUCCESS]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Results.html#success-S-
[NEW_FAILURE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Results.html#failure-F-
[OF_NULLABLE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Results.html#ofNullable-S-F-
[OF_OPTIONAL]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Results.html#ofOptional-java.util.Optional-F-
[OF_CALLABLE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Results.html#ofCallable-java.util.concurrent.Callable-
[HAS_SUCCESS]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#hasSuccess--
[HAS_FAILURE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#hasFailure--
[IF_SUCCESS_OR_ELSE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#ifSuccessOrElse-java.util.function.Consumer,java.util.function.Consumer-
[IF_SUCCESS]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#ifSuccess-java.util.function.Consumer-
[IF_FAILURE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#ifFailure-java.util.function.Consumer-
[OR_ELSE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#orElse-S-
[OR_ELSE_MAP]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#orElseMap-java.util.function.Function-
[OPTIONAL]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#optional--
[STREAM]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#stream--
[FILTER]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#filter-java.util.function.Predicate,java.util.function.Function-
[MAP]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#map-java.util.function.Function,java.util.function.Function-
[MAP_SUCCESS]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#mapSuccess-java.util.function.Function-
[MAP_FAILURE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#mapFailure-java.util.function.Function-
[FLATMAP]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#flatMap-java.util.function.Function,java.util.function.Function-
[FLATMAP_SUCCESS]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#flatMapSuccess-java.util.function.Function-
[FLATMAP_FAILURE]: https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Result.html#flatMapFailure-java.util.function.Function-
