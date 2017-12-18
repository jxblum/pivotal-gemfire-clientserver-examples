/*
 * Copyright 2014-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.gemfire.cache.client;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.client.Pool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;
import org.springframework.data.gemfire.config.xml.GemfireConstants;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The SpringGemFireClientCacheTest class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringGemFireClientCacheTest.SpringGemFireClientConfiguration.class)
@SuppressWarnings("unused")
public class SpringGemFireClientCacheTest {

	@Resource(name = "Factorials")
	private Region<Long, Long> factorials;

	@Before
	public void setup() {

		assertThat(factorials, is(notNullValue()));
		assertThat(factorials.getName(), is(equalTo("Factorials")));
		assertThat(factorials.getFullPath(), is(equalTo(String.format("%1$sFactorials", Region.SEPARATOR))));
		assertThat(factorials.getAttributes(), is(notNullValue()));
		assertThat(factorials.getAttributes().getDataPolicy(), is(equalTo(DataPolicy.EMPTY)));
		assertThat(factorials.getAttributes().getPoolName(), is(equalTo(GemfireConstants.DEFAULT_GEMFIRE_POOL_NAME)));
	}

	@Test
	public void computeFactorials() {

		assertThat(factorials.get(0L), is(equalTo(1L)));
		assertThat(factorials.get(1L), is(equalTo(1L)));
		assertThat(factorials.get(2L), is(equalTo(2L)));
		assertThat(factorials.get(3L), is(equalTo(6L)));
		assertThat(factorials.get(4L), is(equalTo(24L)));
		assertThat(factorials.get(5L), is(equalTo(120L)));
		assertThat(factorials.get(6L), is(equalTo(720L)));
		assertThat(factorials.get(7L), is(equalTo(5040L)));
		assertThat(factorials.get(8L), is(equalTo(40320L)));
		assertThat(factorials.get(9L), is(equalTo(362880L)));
		assertThat(factorials.get(10L), is(equalTo(3628800L)));
	}

	@Configuration
	public static class SpringGemFireClientConfiguration {

		static int intValue(Number value) {
			return value.intValue();
		}

		@Bean
		static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
			return new PropertySourcesPlaceholderConfigurer();
		}

		@Bean
		Properties gemfireProperties(@Value("${gemfire.log.level:config}") String logLevel) {
			Properties gemfireProperties = new Properties();

			gemfireProperties.setProperty("log-level", logLevel);

			return gemfireProperties;
		}

		@Bean
		ClientCacheFactoryBean gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties) {

			ClientCacheFactoryBean gemfireCache = new ClientCacheFactoryBean();

			gemfireCache.setClose(true);
			gemfireCache.setProperties(gemfireProperties);

			return gemfireCache;
		}

		@Bean(name = GemfireConstants.DEFAULT_GEMFIRE_POOL_NAME)
		PoolFactoryBean gemfirePool(
			  @Value("${gemfire.cache.server.host:localhost}") String host,
			  @Value("${gemfire.cache.server.port:40404}") int port) {

			PoolFactoryBean gemfirePool = new PoolFactoryBean();

			gemfirePool.setName(GemfireConstants.DEFAULT_GEMFIRE_POOL_NAME);
			gemfirePool.setFreeConnectionTimeout(intValue(TimeUnit.SECONDS.toMillis(5)));
			gemfirePool.setKeepAlive(false);
			gemfirePool.setPingInterval(TimeUnit.SECONDS.toMillis(5));
			gemfirePool.setReadTimeout(intValue(TimeUnit.SECONDS.toMillis(5)));
			gemfirePool.setRetryAttempts(1);
			gemfirePool.setSubscriptionEnabled(true);
			gemfirePool.setThreadLocalConnections(false);

			gemfirePool.setServers(Collections.singletonList(new ConnectionEndpoint(host, port)));

			return gemfirePool;
		}

		@Bean(name = "Factorials")
		ClientRegionFactoryBean<Long, Long> factorialsRegion(ClientCache gemfireCache, Pool gemfirePool) {

			ClientRegionFactoryBean<Long, Long> factorialsRegion = new ClientRegionFactoryBean<>();

			factorialsRegion.setCache(gemfireCache);
			factorialsRegion.setPool(gemfirePool);
			factorialsRegion.setShortcut(ClientRegionShortcut.PROXY);

			return factorialsRegion;
		}
	}
}
