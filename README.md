
[![Build Status](https://github.com/leakyabstractions/result/workflows/Build/badge.svg)](https://github.com/LeakyAbstractions/result/actions?query=workflow%3ABuild)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=LeakyAbstractions_result&metric=alert_status)](https://sonarcloud.io/dashboard?id=LeakyAbstractions_result)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=LeakyAbstractions_result&metric=coverage)](https://sonarcloud.io/dashboard?id=LeakyAbstractions_result)
[![Latest release](https://img.shields.io/github/release/leakyabstractions/result.svg)](https://github.com/leakyabstractions/result/releases/latest)
[![Download](https://api.bintray.com/packages/leakyabstractions/maven/result/images/download.svg)](https://bintray.com/leakyabstractions/maven/result/)

![Result](docs/result-banner-centered.png)

# Result Library for Java

The purpose of this library is to provide a type-safe encapsulation of operation results that may have succeeded or
failed, instead of throwing exceptions.

If you like `Optional` but feel that it sometimes falls too short, you'll love `Result`.


## Adding Result Library to Your Build

The library has no external dependencies and it is very lightweight. Adding it to your build should be very easy.

### Maven

To add a dependency on Result using [Maven](https://maven.apache.org/), use the following:

```xml
<dependency>
    <groupId>com.leakyabstractions</groupId>
    <artifactId>result</artifactId>
    <version>0.1.2</version>
    <type>pom</type>
</dependency>
```

### Gradle

To add a dependency using [Gradle](https://gradle.org/), if you are building an application that will use `Result`
internally:

```gradle
dependencies {
    implementation 'com.leakyabstractions:result:0.1.2'
}
```

If you are building a library that will use `Result` type in its public API, you should use:

```gradle
dependencies {
    api 'com.leakyabstractions:result:0.1.2'
}
```

For more information on when to use `api` and `implementation`, read the [Gradle documentation on API and implementation
separation](https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_separation).
