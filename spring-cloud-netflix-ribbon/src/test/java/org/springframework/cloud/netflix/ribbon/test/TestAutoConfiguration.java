/*
 * Copyright 2013-2019 the original author or authors.
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

package org.springframework.cloud.netflix.ribbon.test;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.noop.NoopDiscoveryClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author Spencer Gibb
 */
@Configuration
@Import({ NoopDiscoveryClientAutoConfiguration.class })
@AutoConfigureBefore(SecurityAutoConfiguration.class)
public class TestAutoConfiguration {

	public static final String USER = "user";

	public static final String PASSWORD = "{noop}password";

	@Configuration
	@Order(Ordered.HIGHEST_PRECEDENCE)
	protected static class TestSecurityConfiguration
			extends WebSecurityConfigurerAdapter {

		TestSecurityConfiguration() {
			super(true);
		}

		@Bean
		public UserDetailsService userDetailsService() {
			InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
			manager.createUser(
					User.withUsername(USER).password(PASSWORD).roles("USER").build());
			return manager;
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// super.configure(http);
			http.antMatcher("/proxy-username").httpBasic().and().authorizeRequests()
					.antMatchers("/**").permitAll();
		}

	}

}
