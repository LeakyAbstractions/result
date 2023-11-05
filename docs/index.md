---
title: Result Library for Java
description: A Java library to handle success and failure without exceptions
image: /result-magic-ball.png
---

![Result Library for Java](result-magic-ball.png)

> *If you seek advice... then this Java library is for you! `Result` objects have all the answers you need. Start by
> "asking the ball" a Yes or No question, then turn it over and let the answers magically appear. Results can be either
> positive ("Success") or negative ("Failure"). Great fun for software developers and adults alike. Colors and
> decorations may vary.*


# Getting Started

Instances of `Result` represent either the *success* or *failure* of an operation.

Result objects are immutable and type-safe. Operations that succeed produce results encapsulating a success value;
operations that fail produce results with a failure value.

Treating failed operations as regular values allows for a functional approach to error handling -- instead of throwing
exceptions and using try-catch blocks. In terms of functional programming, `Result` is just a monadic container type.

The best way to think of `Result` is as a super-powered version of `Optional`. The only difference is that, whereas
`Optional` may contain a value or be _empty_, `Result` contains either a _success_ value or a _failure_ value.

## Adding Results to Your Build

The library requires JDK 1.8 or higher. Other than that, it has no external dependencies and it is very lightweight.
Adding it to your build should be very easy.

Artifact coordinates:

- Group ID: `com.leakyabstractions`
- Artifact ID: `result`
- Version: `{{ site.current_version }}`

To add the dependency using [**Maven**][MAVEN], use the following:

```xml
<dependency>
    <groupId>com.leakyabstractions</groupId>
    <artifactId>result</artifactId>
    <version>{{ site.current_version }}</version>
    <scope>test</scope>
</dependency>
```

To add the dependency using [**Gradle**][GRADLE], if you are building an application that will use <tt>Result</tt>
internally:

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

## Creating Result Objects

- [`Results.success`][NEW_SUCCESS]
  creates a new successful result containing the given value.
- [`Results.failure`][NEW_FAILURE]
  creates a new failed result containing the given value.
- [`Results.ofNullable`][OF_NULLABLE]
  creates a new result based on the given possibly-null value.
- [`Results.ofOptional`][OF_OPTIONAL]
  creates a new result based on the given possibly-empty optional.
- [`Results.ofCallable`][OF_CALLABLE]
  creates a new result based on the given possibly-throwing task.


# Basic Usage

## Unwrapping Values

- [`hasSuccess`][HAS_SUCCESS]
  checks if a result contains a success value.
- [`hasFailure`][HAS_FAILURE]
  checks if a result contains a failure value.
- [`getSuccess`][GET_SUCCESS]
  returns a result's success value as a possibly-empty optional.
- [`getFailure`][GET_FAILURE]
  returns a result's failure value as a possibly-empty optional.
- [`orElse`][OR_ELSE]
  returns a result's success value, or the given one.
- [`orElseMap`][OR_ELSE_MAP]
  returns a result's success value, or maps its failure value.
- [`streamSuccess`][STREAM_SUCCESS]
  returns a result's success value as a possibly-empty stream.
- [`streamFailure`][STREAM_FAILURE]
  returns a result's failure value as a possibly-empty stream.


## Conditional Actions

- [`ifSuccess`][IF_SUCCESS]
  performs the given action with a successful result's value.
- [`ifFailure`][IF_FAILURE]
  performs the given action with a failed result's value.
- [`ifSuccessOrElse`][IF_SUCCESS_OR_ELSE]
  performs either of the given actions with a result's value.


# Advanced Usage

## Screening Results

- [`filter`][FILTER]
  transforms a successful result into a failed one, based on the given condition.
- [`recover`][RECOVER]
  transforms a failed result into a successful one, based on the given condition.

## Transforming Values

- [`mapSuccess`][MAP_SUCCESS]
  transforms the value of a successful result.
- [`mapFailure`][MAP_FAILURE]
  transforms the value of a failed result.
- [`map`][MAP]
  transforms either the success or the failure value of a result.
- [`flatMapSuccess`][FLATMAP_SUCCESS]
  transforms a successful result into a different one.
- [`flatMapFailure`][FLATMAP_FAILURE]
  transforms a failed result into a different one.
- [`flatMap`][FLATMAP]
  transforms a result into a different one.


# Additional Info

## Releases

This library adheres to [Pragmatic Versioning][PRAGVER].

Artifacts are available in [Maven Central][ARTIFACTS].


## Javadoc

Here you can find the full [Javadoc documentation][JAVADOC].


## Benchmarks

You may want to visualize the latest [benchmark report][BENCHMARK].


## Looking for Support?

We'd love to help. Check out the [support guidelines][SUPPORT].


## Contributions Welcome

If you'd like to contribute to this project, please [start here][CONTRIBUTING].


## Code of Conduct

This project is governed by the [Contributor Covenant Code of Conduct][CODE_OF_CONDUCT].
By participating, you are expected to uphold this code.


## Author

Copyright 2023 [Guillermo Calvo][AUTHOR].

[![][GUILLERMO_IMAGE]][GUILLERMO]


## License

This library is licensed under the *Apache License, Version 2.0* (the "License");
you may not use it except in compliance with the License.

You may obtain a copy of the License at <http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software distributed under the License
is distributed on an "AS IS" BASIS, **WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND**, either express or implied.

See the License for the specific language governing permissions and limitations under the License.


**Permitted:**

- **Commercial Use**: You may use this library and derivatives for commercial purposes.
- **Modification**: You may modify this library.
- **Distribution**: You may distribute this library.
- **Patent Use**: This license provides an express grant of patent rights from contributors.
- **Private Use**: You may use and modify this library without distributing it.

**Required:**

- **License and Copyright Notice**: If you distribute this library you must include a copy of the license and copyright
  notice.
- **State Changes**: If you modify and distribute this library you must document changes made to this library.

**Forbidden:**

- **Trademark use**: This license does not grant any trademark rights.
- **Liability**: The library author cannot be held liable for damages.
- **Warranty**: This library is provided without any warranty.


[ARTIFACTS]:                    https://search.maven.org/artifact/com.leakyabstractions/result/
[AUTHOR]:                       https://github.com/guillermocalvo/
[BENCHMARK]:                    https://dev.leakyabstractions.com/result-benchmark/
[CODE_OF_CONDUCT]:              https://dev.leakyabstractions.com/result/CODE_OF_CONDUCT.html
[CONTRIBUTING]:                 https://dev.leakyabstractions.com/result/CONTRIBUTING.html
[FILTER]:                       https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/api/result/Result.html#filter-java.util.function.Predicate-java.util.function.Function-
[FLATMAP]:                      https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/api/result/Result.html#flatMap-java.util.function.Function-java.util.function.Function-
[FLATMAP_FAILURE]:              https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/api/result/Result.html#flatMapFailure-java.util.function.Function-
[FLATMAP_SUCCESS]:              https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/api/result/Result.html#flatMapSuccess-java.util.function.Function-
[GET_FAILURE]:                  https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/api/result/Result.html#getFailure--
[GET_SUCCESS]:                  https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/api/result/Result.html#getSuccess--
[GRADLE]:                       https://gradle.org/
[GUILLERMO]:                    https://guillermo.dev/
[GUILLERMO_IMAGE]:              https://guillermo.dev/assets/images/thumb.png
[HAS_FAILURE]:                  https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#hasFailure--
[HAS_SUCCESS]:                  https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#hasSuccess--
[IF_FAILURE]:                   https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#ifFailure-java.util.function.Consumer-
[IF_SUCCESS]:                   https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#ifSuccess-java.util.function.Consumer-
[IF_SUCCESS_OR_ELSE]:           https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#ifSuccessOrElse-java.util.function.Consumer-java.util.function.Consumer-
[JAVADOC]:                      https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/
[MAP]:                          https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#map-java.util.function.Function-java.util.function.Function-
[MAP_FAILURE]:                  https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#mapFailure-java.util.function.Function-
[MAP_SUCCESS]:                  https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#mapSuccess-java.util.function.Function-
[MAVEN]:                        https://maven.apache.org/
[NEW_FAILURE]:                  https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/core/Results.html#failure-F-
[NEW_SUCCESS]:                  https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/core/Results.html#success-S-
[OF_CALLABLE]:                  https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/core/Results.html#ofCallable-java.util.concurrent.Callable-
[OF_NULLABLE]:                  https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/core/Results.html#ofNullable-S-F-
[OF_OPTIONAL]:                  https://dev.leakyabstractions.com/result/javadoc/{{ site.current_version }}/com/leakyabstractions/result/core/Results.html#ofOptional-java.util.Optional-F-
[OR_ELSE]:                      https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#orElse-S-
[OR_ELSE_MAP]:                  https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#orElseMap-java.util.function.Function-
[PRAGVER]:                      https://pragver.github.io/
[RECOVER]:                      https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#recover-java.util.function.Predicate-java.util.function.Function-
[STREAM_FAILURE]:               https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#streamFailure--
[STREAM_SUCCESS]:               https://dev.leakyabstractions.com/result-api/javadoc/{{ site.api_version }}/com/leakyabstractions/result/api/Result.html#streamSuccess--
[SUPPORT]:                      https://dev.leakyabstractions.com/result/SUPPORT.html
