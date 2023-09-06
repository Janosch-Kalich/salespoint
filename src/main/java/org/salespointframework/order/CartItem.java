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
package org.salespointframework.order;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

import javax.money.MonetaryAmount;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * A CartItem consists of a {@link Product} and a {@link Quantity}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@ToString
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItem implements Priced {

	private final String id;
	private final MonetaryAmount price;
	private final Quantity quantity;
	private final Product product;

	/**
	 * Creates a new {@link CartItem}.
	 *
	 * @param product must not be {@literal null}.
	 * @param quantity must not be {@literal null}.
	 */
	CartItem(Product product, Quantity quantity) {
		this(UUID.randomUUID().toString(), product, quantity);
	}

	/**
	 * Creates a new {@link CartItem}.
	 *
	 * @param id must not be {@literal null}.
	 * @param product must not be {@literal null}.
	 * @param quantity must not be {@literal null}.
	 */
	private CartItem(String id, Product product, Quantity quantity) {

		Assert.notNull(id, "Identifier must not be null!");
		Assert.notNull(product, "Product must be not null!");
		Assert.notNull(quantity, "Quantity must be not null!");
		Assert.isTrue(quantity.isPositive(), "Quantity must be positive!");

		product.verify(quantity);

		this.id = id;
		this.quantity = quantity;
		this.price = product.getPrice().multiply(quantity.getAmount());
		this.product = product;
	}

	/**
	 * Returns the name of the {@link Product} associated with the {@link CartItem}.
	 *
	 * @return
	 */
	public final String getProductName() {
		return product.getName();
	}

	/**
	 * Returns a new {@link CartItem} that has the given {@link Quantity} added to the current one. Subtracting quantities
	 * to below zero will normalize the quantity to zero.
	 *
	 * @param quantity must not be {@literal null}.
	 * @return a {@link CartItem} with the given quantity added or {@literal null} in case the resulting {@link Quantity}
	 *         is zero or negative.
	 */
	@Nullable
	final CartItem add(Quantity quantity) {

		Assert.notNull(quantity, "Quantity must not be null!");

		var newQuantity = this.quantity.add(quantity);

		return newQuantity.isZeroOrNegative() ? null : new CartItem(id, product, newQuantity);
	}

	/**
	 * Creates an {@link OrderLine} from this CartItem.
	 *
	 * @return
	 */
	final OrderLine toOrderLine() {
		return new OrderLine(product, quantity);
	}
}
