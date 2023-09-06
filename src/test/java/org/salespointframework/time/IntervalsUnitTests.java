/*
 * Copyright 2017-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.time;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Intervals}.
 *
 * @author Oliver Gierke
 */
class IntervalsUnitTests {

	@Test
	void setsUpIntervalsCorrectly() {

		Interval interval = Interval.from(LocalDateTime.now()).withLength(Duration.ofDays(10));
		Intervals intervals = Intervals.divide(interval, Duration.ofDays(2));

		assertThat(intervals, is(iterableWithSize(5)));
	}

	@Test
	void setsUpExceedingIntervalsCorrectly() {

		Interval interval = Interval.from(LocalDateTime.now()).withLength(Duration.ofDays(10));
		Intervals intervals = Intervals.divide(interval, Duration.ofDays(3));

		assertThat(intervals, is(iterableWithSize(4)));
	}
}
