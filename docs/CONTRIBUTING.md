---
---

# How to Contribute

Hi there! We're thrilled that you'd like to contribute to this project. Your help is essential for keeping it great.

The Result Library is [free software](https://www.gnu.org/philosophy/free-sw.en.html) that depends on volunteer effort
and built one contribution at a time by users like you. We'd love for you to get involved. Whatever your level of skill
or however much time you can give, your contribution is greatly appreciated. There are many ways to contribute, from
writing tutorials or blog posts, improving the documentation, submitting bug reports and feature requests, helping other
users by commenting on issues, or writing code which can be incorporated into the project itself.

Following these guidelines helps to communicate that you respect the time of the developers managing and developing this
project. In return, they should reciprocate that respect in addressing your issue, assessing changes, and helping you
finalize your pull requests.


## How to Report a Bug

Think you found a bug? Please check the [list of open issues](https://github.com/leakyabstractions/result/issues) to see
if your bug has already been reported. If it hasn't please
[submit a new issue](https://github.com/leakyabstractions/result/issues/new).

Here are a few tips for writing *great* bug reports:

- Describe the specific problem.
- Include the steps to reproduce the bug, what you expected to happen, and what happened instead.
- Check that you are using the latest version of the project.
- Include what version of the project you are using, as well as any relevant dependencies.
- Only include one bug per issue. If you have discovered two bugs, please file two issues.
- Even if you don't know how to fix the bug, including a failing test may help others track it down.


## How to Suggest a Feature

If you find yourself wishing for an enhancement or a feature that doesn't exist in the library, you are probably not
alone. There are bound to be others out there with similar needs. Many of the current features have been added because
other users saw the need.

Feature requests are welcome. But take a moment to find out whether your idea fits with the scope and goals of the
project. It's up to you to make a strong case to convince the project's developers of the merits of this feature. Please
provide as much detail and context as possible, including describing the problem you're trying to solve.

[Open an issue](https://github.com/leakyabstractions/result/issues/new) which describes the feature you would like to
see, why you want it, how it should work, etc.


## Your First Contribution

We'd love for you to contribute to this project. Unsure where to begin contributing to the library? You can start by
looking through [currently open issues](https://github.com/leakyabstractions/result/issues?q=is%3Aissue+is%3Aopen).

Feel free to ask for help; everyone is a beginner at first :smiley_cat:


## How to Propose Changes

Here's a few general guidelines for proposing changes:

- Please be sure to update the documentation.
- Each pull request should implement **one** feature or bug fix. If you want to add or fix more than one thing, submit
  more than one pull request.
- Don't commit changes to files that are irrelevant to your feature or bug fix.
- Don't bump the version number in your pull request (it will be bumped prior to release).
- Write [a good commit message](http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html)

At a high level, the [process for proposing changes](https://guides.github.com/introduction/flow/) is:

1. [Fork](https://github.com/leakyabstractions/result/fork) and clone the project.
2. Make sure the tests pass on your machine: `./gradlew test`.
3. Create a new branch: `git checkout -b my-branch-name`.
4. Make your changes, add tests, and make sure the tests still pass.
5. Make sure the changes comply with the formatting rules `./gradlew spotlessCheck`.
6. Check that the code is properly covered by the tests `./gradlew jacocoTestCoverageVerification`.
7. Push to your fork and [submit a pull request](https://github.com/leakyabstractions/result/compare).
8. Pat your self on the back and wait for your pull request to be reviewed and merged.

**Interesting in submitting your first Pull Request?** It's easy! You can learn how from this *free* series
[How to Contribute to an Open Source Project on GitHub](https://egghead.io/series/how-to-contribute-to-an-open-source-project-on-github)


## Formatting the Source Code

```shell
$ ./gradlew spotlessApply
```


## Running the Tests

```shell
$ ./gradlew test
```


## Checking Code Coverage

```shell
$ ./gradlew jacocoTestReport jacocoTestCoverageVerification
```


## Code of Conduct

This project is governed by the [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are
expected to uphold this code.
