
package com.leakyabstractions.result;

import static com.leakyabstractions.result.Results.*;
import static com.leakyabstractions.result.Results.failure;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Results#combine(Result...)}.
 * 
 * @author Guillermo Calvo
 */
@DisplayName("Results combineVarArgs")
class Results_combineVarArgs_Test {

    @Test
    void should_return_success_when_no_failures() {
        // Given
        final Result<String, Exception> result1 = success("success1");
        final Result<String, IOException> result2 = success("success2");
        final Result<String, Exception> result3 = success("success3");
        final Result<String, IOException> result4 = success("success4");
        // When
        final Result<List<String>, Stream<Exception>> result = combine(result1, result2, result3, result4)
                .mapSuccess(this::toList);
        // Then
        assertThat(result).isEqualTo(success(list("success1", "success2", "success3", "success4")));
    }

    @Test
    void should_return_failure_when_any_failures() {
        // Given
        final Result<Integer, String> result1 = success(123);
        final Result<Integer, String> result2 = failure("failure1");
        final Result<Integer, String> result3 = failure("failure2");
        final Result<Integer, String> result4 = success(321);
        // When
        final Result<Stream<Integer>, List<String>> result = combine(result1, result2, result3, result4)
                .mapFailure(this::toList);
        // Then
        assertThat(result).isEqualTo(failure(list("failure1", "failure2")));
    }

    <T> List<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toList());
    }
}
