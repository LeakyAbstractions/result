
[![Build Status][BADGE_BUILD_STATUS]][BUILD_STATUS]
[![Quality Gate Status][BADGE_QUALITY_GATE]][QUALITY_GATE]
[![Coverage][BADGE_CODE_COVERAGE]][CODE_COVERAGE]
[![Maven Central][BADGE_ARTIFACTS]][ARTIFACTS]
[![Javadoc][BADGE_JAVADOC]][JAVADOC]
[![Free ebook][BADGE_GUIDE_BOOK]][GUIDE_BOOK]

![][LOGO_DARK]
![][LOGO_LIGHT]

[![Benchmark Report][BADGE_BENCHMARK]][BENCHMARK]

## A Java library to handle success and failure without exceptions

Wave goodbye to slow exceptions and embrace clean, efficient error handling by encapsulating operations that may succeed
or fail in a type-safe way.

- **Boost Performance**: Avoid exception overhead and benefit from faster operations.
- **Simple API**: Leverage a familiar interface for a smooth learning curve.
- **Streamlined Error Handling**: Handle failure explicitly to simplify error propagation.
- **Safe Execution**: Ensure safer and more predictable operation outcomes.
- **Enhanced Readability**: Reduce complexity to make your code easier to understand.
- **Functional Style**: Embrace elegant, functional programming paradigms.
- **Lightweight**: Keep your project slim with no extra dependencies.
- **Open Source**: Enjoy transparent, permissive Apache 2 licensing.
- **Pure Java**: Seamless compatibility from JDK8 to the latest versions.

> [!NOTE]
> `Result` objects represent the outcome of an operation, removing the need to check for null. Operations that succeed
> produce results encapsulating a *success* value; operations that fail produce results with a *failure* value. Success
> and failure can be represented by whatever types make the most sense for each operation.


## Results in a Nutshell

In Java, methods that can fail typically do so by throwing exceptions. Then, exception-throwing methods are called from
inside a `try` block to handle errors in a separate `catch` block.

![Using Exceptions][USING_EXCEPTIONS]

This approach is lengthy, and that's not the only problem -- it's also very slow.

> [!TIP]
> Conventional wisdom says **exceptional logic shouldn't be used for normal program flow**. Results make us deal with
> expected error situations explicitly to enforce good practices and make our programs [run faster][BENCHMARK].

Let's now look at how the above code could be refactored if `connect()` returned a `Result` object instead of throwing
an exception.

![Using Results][USING_RESULTS]

In the example above, we used only 4 lines of code to replace the 10 that worked for the first one. But we can
effortlessly make it shorter by chaining methods. In fact, since we were returning `-1` just to signal that the
underlying operation failed, we are better off returning a `Result` object upstream. This will allow us to compose
operations on top of `getServerUptime()` just like we did with `connect()`.

![Embracing Results][EMBRACING_RESULTS]

> [!NOTE]
> `Result` objects are immutable, providing thread safety without the need for synchronization. This makes them ideal
> for multi-threaded applications, ensuring predictability and eliminating side effects.


## Ready to Tap into the Power of Results?

Read the [guide][GUIDE_HOME] and transform your error handling today.

- :seedling: [Getting Started][GUIDE_START]
- :potted_plant: [Basic Usage][GUIDE_BASIC]
- :rocket: [Advanced Usage][GUIDE_ADVANCED]

Also available as an **ebook** in multiple formats. [Download your free copy now!][GUIDE_BOOK]


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

Copyright 2024 [Guillermo Calvo][AUTHOR].

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
[BADGE_ARTIFACTS]:              https://img.shields.io/endpoint?url=https://dev.leakyabstractions.com/result/badge.json&logo=Gradle&label=Maven+Central&labelColor=555
[BADGE_BENCHMARK]:              https://img.shields.io/endpoint?url=https://dev.leakyabstractions.com/result-benchmark/badge.json
[BADGE_BUILD_STATUS]:           https://github.com/leakyabstractions/result/workflows/Build/badge.svg
[BADGE_CODE_COVERAGE]:          https://sonarcloud.io/api/project_badges/measure?project=LeakyAbstractions_result&metric=coverage
[BADGE_GUIDE_BOOK]:             https://img.shields.io/badge/Free_book-444?logo=leanpub
[BADGE_JAVADOC]:                https://javadoc.io/badge2/com.leakyabstractions/result/javadoc.svg
[BADGE_QUALITY_GATE]:           https://sonarcloud.io/api/project_badges/measure?project=LeakyAbstractions_result&metric=alert_status
[BENCHMARK]:                    https://dev.leakyabstractions.com/result-benchmark/
[BUILD_STATUS]:                 https://github.com/LeakyAbstractions/result/actions?query=workflow%3ABuild
[CODE_COVERAGE]:                https://sonarcloud.io/component_measures?id=LeakyAbstractions_result&metric=coverage&view=list
[CODE_OF_CONDUCT]:              https://github.com/LeakyAbstractions/.github/blob/main/CODE_OF_CONDUCT.md
[CONTRIBUTING]:                 https://github.com/LeakyAbstractions/.github/blob/main/CONTRIBUTING.md
[EMBRACING_RESULTS]:            docs/embracing-results.png
[GUIDE_ADVANCED]:               https://result.leakyabstractions.com/docs/advanced
[GUIDE_BASIC]:                  https://result.leakyabstractions.com/docs/basic
[GUIDE_BOOK]:                   https://leanpub.com/result/
[GUIDE_HOME]:                   https://result.leakyabstractions.com/
[GUIDE_START]:                  https://result.leakyabstractions.com/docs/start
[GUILLERMO]:                    https://guillermo.dev/
[GUILLERMO_IMAGE]:              https://guillermo.dev/assets/images/thumb.png
[JAVADOC]:                      https://javadoc.io/doc/com.leakyabstractions/result/
[LOGO_DARK]:                    docs/result-logo.dark.svg#gh-dark-mode-only
[LOGO_LIGHT]:                   docs/result-logo.svg#gh-light-mode-only
[PRAGVER]:                      https://pragver.github.io/
[QUALITY_GATE]:                 https://sonarcloud.io/dashboard?id=LeakyAbstractions_result
[SUPPORT]:                      https://github.com/LeakyAbstractions/.github/blob/main/SUPPORT.md
[USING_EXCEPTIONS]:             docs/using-exceptions.png
[USING_RESULTS]:                docs/using-results.png
