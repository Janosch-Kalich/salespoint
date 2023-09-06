/*
 * Copyright 2019-2023 the original author or authors.
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
package org.salespointframework.order;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.lang.Nullable;

/**
 * Custom extension of {@link Order}.
 *
 * @author Oliver Drotbohm
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
class CustomOrder extends Order {
	@Nullable private String customProperty;

	@SuppressWarnings("unused")
	private CustomOrder() {
		super();
	}

	/**
	 * Creates a new {@link CustomOrder} for the given {@link UserAccountIdentifier} and custom property.
	 *
	 * @param userAccountIdentifier must not be {@literal null}.
	 * @param customProperty can be {@literal null}.
	 */
	public CustomOrder(UserAccountIdentifier userAccountIdentifier, @Nullable String customProperty) {

		super(userAccountIdentifier);

		this.customProperty = customProperty;
	}
}
