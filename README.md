
[![Build Status](https://github.com/leakyabstractions/result/workflows/Build/badge.svg)](https://github.com/LeakyAbstractions/result/actions?query=workflow%3ABuild)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=LeakyAbstractions_result&metric=alert_status)](https://sonarcloud.io/dashboard?id=LeakyAbstractions_result)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=LeakyAbstractions_result&metric=coverage)](https://sonarcloud.io/component_measures?id=LeakyAbstractions_result&metric=coverage&view=list)
[![Maven Central](https://img.shields.io/endpoint?url=https://dev.leakyabstractions.com/result/badge.json&logo=java&label=maven-central&labelColor=555)](https://search.maven.org/artifact/com.leakyabstractions/result)
[![Latest Release](https://img.shields.io/github/release/leakyabstractions/result.svg?logo=github)](https://github.com/leakyabstractions/result/releases/latest)
[![Javadoc](https://img.shields.io/badge/javadoc-result-blue)](https://javadoc.io/doc/com.leakyabstractions/result)
[![Javadoc](https://img.shields.io/badge/javadoc-result--assertj-blue)](https://javadoc.io/doc/com.leakyabstractions/result-assertj)

# Result Library for Java

![Result](https://dev.leakyabstractions.com/result/result-banner-centered.png)

The purpose of this library is to provide a type-safe encapsulation of operation results that may have succeeded or
failed, instead of throwing exceptions.

If you like `Optional` but feel that it sometimes falls too short, you'll love `Result`.


## Result Library in a Nutshell

Before _Result_, we would wrap exception-throwing `foobar` method invocation inside a `try` block so that errors can be
handled inside a `catch` block.

```java

public int getFoobarLength() {
    int length;
    try {
        final String result = foobar();
        this.commit(result);
        length = result.length();
    } catch(SomeException problem) {
        this.rollback(problem);
        length = -1;
    }
    return length;
}

```

This approach is lengthy, and that's not the only problem -- it's also slow. Conventional wisdom says that exceptional
logic shouldn't be used for normal program flow. _Result_ makes us deal with expected, non-exceptional error situations
explicitly as a way of enforcing good programming practices.

Let's now look at how the above code could be refactored if method `foobar` returned a _Result_ object instead of
throwing an exception:

```java

public int getFoobarLength() {
    final Result<String, SomeFailure> result = foobar();
    result.ifSuccessOrElse(this::commit, this::rollback);
    final Result<Integer, SomeFailure> resultLength = result.mapSuccess(String::length);
    return resultLength.orElse(-1);
}

```

In the above example, we use only four lines of code to replace the ten that worked in the first example. But we can
make it even shorter by chaining methods in typical functional programming style:

```java

public int getFoobarLength() {
    return foobar().ifSuccessOrElse(this::commit, this::rollback).mapSuccess(String::length).orElse(-1);
}

```

In fact, since we are using `-1` here just to signal that the underlying operation failed, we'd be better off returning
a _Result_ object upstream:

```java

public Result<Integer, SomeFailure> getFoobarLength() {
    return foobar().ifSuccessOrElse(this::commit, this::rollback).mapSuccess(String::length);
}

```

This allows others to easily compose operations on top of ours, just like we did with `foobar`.


## Getting Started

Please read the [Quick Guide](https://dev.leakyabstractions.com/result/) to know how to add this library to your build
and further info about _Result_ features.


## Releases

This library adheres to [Pragmatic Versioning](https://pragver.github.io/).

Artifacts are available in [Maven Central](https://search.maven.org/artifact/com.leakyabstractions/result).


## Javadoc

Here you can find the full [Result API documentation](https://javadoc.io/doc/com.leakyabstractions/result/).


## Looking for Support?

We'd love to help. Check out the [support guidelines](https://dev.leakyabstractions.com/result/SUPPORT.html).


## Contributions Welcome

If you'd like to contribute to this project, please [start here](https://dev.leakyabstractions.com/result/CONTRIBUTING.html).


## Code of Conduct

This project is governed by the
[Contributor Covenant Code of Conduct](https://dev.leakyabstractions.com/result/CODE_OF_CONDUCT.html).
By participating, you are expected to uphold this code.
