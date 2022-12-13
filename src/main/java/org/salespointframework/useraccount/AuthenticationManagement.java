/*
 * Copyright 2017-2022 the original author or authors.
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
package org.salespointframework.useraccount;

import java.util.Optional;

import org.salespointframework.useraccount.Password.EncryptedPassword;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.springframework.data.util.Streamable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * Application component for authentication related use cases.
 *
 * @author Oliver Drotbohm
 */
@Service
public interface AuthenticationManagement {

	/**
	 * Returns the {@link UserAccount} of the currently logged in user or {@link Optional#empty()} if no-one is currently
	 * logged in.
	 *
	 * @return will never be {@literal null}.
	 */
	Optional<UserAccount> getCurrentUser();

	/**
	 * Returns whether the given candidate {@link Password} matches the given existing one.
	 *
	 * @param candidate can be {@literal null}.
	 * @param existing must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	boolean matches(@Nullable UnencryptedPassword candidate, EncryptedPassword existing);

	/**
	 * Updates the current authentication to the given {@link UserAccount}.
	 *
	 * @param account must not be {@literal null}.
	 * @since 7.5
	 */
	void updateAuthentication(UserAccount account);

	/**
	 * Returns all {@link UserAccount}s for the users currently logged into the system.
	 *
	 * @return will never be {@literal null}.
	 * @since 8.1
	 */
	Streamable<UserAccount> getCurrentlyLoggedInUserAccounts();
}
