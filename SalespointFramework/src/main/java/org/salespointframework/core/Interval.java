/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.core;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Oliver Gierke
 */
public final class Interval {

	private final LocalDateTime start;
	private final LocalDateTime end;

	public Interval(LocalDateTime start, LocalDateTime end) {

		Objects.requireNonNull(start, "Start must not be null!");
		Objects.requireNonNull(end, "End must not be null!");

		this.start = start;
		this.end = end;
	}

	/**
	 * @return the start
	 */
	public LocalDateTime getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public LocalDateTime getEnd() {
		return end;
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Interval)) {
			return false;
		}

		Interval that = (Interval) obj;

		return this.start.equals(that.start) && this.end.equals(that.end);
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = 31;

		result += 17 * start.hashCode();
		result += 17 * end.hashCode();

		return result;
	}
}
