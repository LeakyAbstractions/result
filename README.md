
[![Build Status](https://github.com/leakyabstractions/result/workflows/Build/badge.svg)](https://github.com/LeakyAbstractions/result/actions?query=workflow%3ABuild)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=LeakyAbstractions_result&metric=alert_status)](https://sonarcloud.io/dashboard?id=LeakyAbstractions_result)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=LeakyAbstractions_result&metric=coverage)](https://sonarcloud.io/component_measures?id=LeakyAbstractions_result&metric=coverage&view=list)
[![Latest release](https://img.shields.io/github/release/leakyabstractions/result.svg)](https://github.com/leakyabstractions/result/releases/latest)
[![Download](https://api.bintray.com/packages/leakyabstractions/maven/result/images/download.svg)](https://bintray.com/leakyabstractions/maven/result/)

# Result Library for Java

![Result](https://dev.leakyabstractions.com/result/result-banner-centered.png)

The purpose of this library is to provide a type-safe encapsulation of operation results that may have succeeded or
failed, instead of throwing exceptions.

If you like `Optional` but feel that it sometimes falls too short, you'll love `Result`.


## Result Library in a Nutshell

Before _Result_, we would wrap `someMethod` invocation inside a `try` block so that errors can be handled inside a
`catch` block.

```java

    int length = 0;
    try {
        String result = this.someMethod();
        this.commit(result);
        length = result.length()
    } catch(SomeException problem) {
        this.rollback(problem);
    }
    return length;

```

This approach is lengthy, and that's not the only problem -- it's also slow. Conventional wisdom says that exceptional
logic shouldn't be used for normal program flow.

_Result_ makes us deal with expected, non-exceptional error situations explicitly as a way of enforcing good programming
practices.

Let's now look at how the above code could be refactored with _Result_:

```java

    Result<String, SomeFailure> result = this.someMethod();
    result.handle(this::commit, this::rollback);
    return result.map(String::length);

```

In the above example, we use only three lines of code to replace the nine that worked in the first example. But we can
make it even shorter by chaining methods in typical functional programming style:

```java

    return this.someMethod().handle(this::commit, this::rollback).map(String::length);

```


## Getting Started

Please read the [Quick Guide](https://dev.leakyabstractions.com/result/) to know how to add this library to your build
and further info about _Result_ features.


## Javadoc

Here you can find the full [Result API documentation](https://dev.leakyabstractions.com/result/api/).


## Looking for Support?

We'd love to help. Check out the [support guidelines](https://dev.leakyabstractions.com/result/SUPPORT.html).


## Contributions Welcome

If you'd like to contribute to this project, please [start here](https://dev.leakyabstractions.com/result/CONTRIBUTING.html).


## Code of Conduct

This project is governed by the
[Contributor Covenant Code of Conduct](https://dev.leakyabstractions.com/result/CODE_OF_CONDUCT.html).
By participating, you are expected to uphold this code.
