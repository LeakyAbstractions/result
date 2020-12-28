
# Result Library for Java

The purpose of this library is to provide a type-safe encapsulation of operation results that may have succeeded or
failed, instead of throwing exceptions.

If you like `Optional` but feel that it sometimes falls too short, you'll love `Result`.


## Adding Result to Your Build

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


## Asserting Result objects

Additionaly you can use fluent assertions (based on [AssertJ](https://assertj.github.io/)) for Result objects in your
unit tests.

To add a dependency on Result using **Maven**, use the following:

```xml
<dependency>
    <groupId>com.leakyabstractions</groupId>
    <artifactId>result-assertj</artifactId>
    <version>0.1.2</version>
    <scope>test</scope>
</dependency>
```

To add a dependency using **Gradle**:

```gradle
dependencies {
    testImplementation 'com.leakyabstractions:result-assertj:0.1.2'
}
```
