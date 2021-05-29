
[![Build Status](https://github.com/leakyabstractions/result/workflows/Build/badge.svg)](https://github.com/LeakyAbstractions/result/actions?query=workflow%3ABuild)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=LeakyAbstractions_result&metric=alert_status)](https://sonarcloud.io/dashboard?id=LeakyAbstractions_result)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=LeakyAbstractions_result&metric=coverage)](https://sonarcloud.io/component_measures?id=LeakyAbstractions_result&metric=coverage&view=list)
[![Maven Central](https://img.shields.io/endpoint?url=https://dev.leakyabstractions.com/result/badge.json&logo=java&label=maven-central&labelColor=555)](https://search.maven.org/artifact/com.leakyabstractions/result)
[![Latest Release](https://img.shields.io/github/release/leakyabstractions/result.svg?logo=github)](https://github.com/leakyabstractions/result/releases/latest)
[![Javadoc](https://img.shields.io/endpoint?url=https://dev.leakyabstractions.com/result/badge.json&label=javadoc&color=blue)](https://dev.leakyabstractions.com/result/javadoc/)

# Result Library for Java

![Result](https://dev.leakyabstractions.com/result/result-banner-centered.png)

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

Here you can find the full [Result API documentation](https://dev.leakyabstractions.com/result/javadoc/).


## Looking for Support?

We'd love to help. Check out the [support guidelines](https://dev.leakyabstractions.com/result/SUPPORT.html).


## Contributions Welcome

If you'd like to contribute to this project, please [start here](https://dev.leakyabstractions.com/result/CONTRIBUTING.html).


## Code of Conduct

This project is governed by the
[Contributor Covenant Code of Conduct](https://dev.leakyabstractions.com/result/CODE_OF_CONDUCT.html).
By participating, you are expected to uphold this code.


## Author

Copyright 2021 [Guillermo Calvo](https://github.com/guillermocalvo)

[![](https://guillermo.dev/assets/images/thumb.png)](https://guillermo.dev/)


## License

This is free software: you can redistribute it and/or modify it under the terms of the
**GNU Lesser General Public License** as published by the *Free Software Foundation*,
either version 3 of the License, or (at your option) any later version.

This software is distributed in the hope that it will be useful, but **WITHOUT ANY WARRANTY**;
without even the implied warranty of **MERCHANTABILITY** or **FITNESS FOR A PARTICULAR PURPOSE**.
See the [GNU Lesser General Public License](http://www.gnu.org/licenses/lgpl.html) for more details.

You should have received a copy of the GNU Lesser General Public License along with this software.
If not, see <http://www.gnu.org/licenses/>.

### Permitted

- **Library Use**: Applications using this library can be released under any license.
- **Commercial Use**: You may use this library and derivatives for commercial purposes.
- **Modification**: You may modify this library.
- **Distribution**: You may distribute this library.
- **Private Use**: You may use and modify this library without distributing it.
- **Patent Grant**: This license provides an express grant of patent rights from contributors.

### Required

- **License and Copyright Notice**: If you distribute this library you must include a copy of the license and copyright
  notice.
- **Disclose Source**: If you distribute this library you must make available its source code (not the entire program
  using the library).
- **State Changes**: If you modify and distribute this library you must document changes made to this library.
- **Same License**: If you modify and distribute this library you must release it (not the entire program using the
  library) under the same license.

### Forbidden

- **Warranty**: This library is provided without any warranty.
- **Liability**: The library author cannot be held liable for damages.
