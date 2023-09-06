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
package org.salespointframework.core;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Streamable;

/**
 * Base interface for repositories. Declared explicitly to keep a single point of dependency to Spring Data as well as
 * override {@link #findAll()} to return a {@link Streamable} out of the box.
 *
 * @author Oliver Drotbohm
 * @since 7.3
 */
@NoRepositoryBean
public interface SalespointRepository<T, ID extends Identifier>
		extends CrudRepository<T, ID>, PagingAndSortingRepository<T, ID> {

	/**
	 * Re-declaration of {@link CrudRepository#findAll()} to return {@link Streamable} instead of {@link Iterable} for
	 * easier composition.
	 *
	 * @return all aggregates managed by the repository.
	 */
	Streamable<T> findAll();

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Sort)
	 */
	@Override
	Streamable<T> findAll(Sort sort);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAllById(java.lang.Iterable)
	 */
	@Override
	Streamable<T> findAllById(Iterable<ID> ids);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#saveAll(java.lang.Iterable)
	 */
	@Override
	<S extends T> Streamable<S> saveAll(Iterable<S> entities);
}
