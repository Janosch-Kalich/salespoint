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
package org.salespointframework.useraccount.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.salespointframework.useraccount.AuthenticationManagement;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * {@link HandlerMethodArgumentResolver} to inject the {@link UserAccount} of the currently logged in user into Spring
 * MVC controller method parameters annotated with {@link LoggedIn}. The parameter can also use {@link Optional} as
 * wrapper for {@link UserAccount} to indicate that an anonymous invocation is possible.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Component
@RequiredArgsConstructor
class LoggedInUserAccountArgumentResolver implements HandlerMethodArgumentResolver, WebMvcConfigurer {

	private static final String USER_ACCOUNT_EXPECTED = "Expected to find a current user but none available! If the user does not necessarily have to be logged in, use Optional<UserAccount> instead!";
	private static final ResolvableType USER_ACCOUNT = ResolvableType.forClass(UserAccount.class);
	private static final ResolvableType OPTIONAL_OF_USER_ACCOUNT = ResolvableType.forClassWithGenerics(Optional.class,
			UserAccount.class);

	private final @NonNull AuthenticationManagement authenticationManager;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		var user = authenticationManager.getCurrentUser();
		var parameterType = ResolvableType.forMethodParameter(parameter);

		if (OPTIONAL_OF_USER_ACCOUNT.isAssignableFrom(parameterType)) {
			return user;
		}

		return hasAuthorizationAnnotation(parameter)
				? user.orElse(null)
				: user.orElseThrow(() -> new ServletRequestBindingException(USER_ACCOUNT_EXPECTED));
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		if (!parameter.hasParameterAnnotation(LoggedIn.class)) {
			return false;
		}

		var type = ResolvableType.forMethodParameter(parameter);

		return USER_ACCOUNT.isAssignableFrom(type) || OPTIONAL_OF_USER_ACCOUNT.isAssignableFrom(type);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer#addArgumentResolvers(java.util.List)
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(this);
	}

	private static boolean hasAuthorizationAnnotation(MethodParameter parameter) {
		return parameter.hasMethodAnnotation(PreAuthorize.class);
	}
}
