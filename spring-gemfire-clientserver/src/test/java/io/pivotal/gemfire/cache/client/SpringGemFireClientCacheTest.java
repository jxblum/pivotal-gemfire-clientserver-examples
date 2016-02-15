/*
 *  Copyright 2014-present the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.gemfire.cache.client;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;
import org.springframework.data.gemfire.config.GemfireConstants;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gemstone.gemfire.cache.DataPolicy;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.Pool;

import io.pivotal.gemfire.main.SpringGemFireServerApplication;

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
		assertThat(factorials.get(0l), is(equalTo(1l)));
		assertThat(factorials.get(1l), is(equalTo(1l)));
		assertThat(factorials.get(2l), is(equalTo(2l)));
		assertThat(factorials.get(3l), is(equalTo(6l)));
		assertThat(factorials.get(4l), is(equalTo(24l)));
		assertThat(factorials.get(5l), is(equalTo(120l)));
		assertThat(factorials.get(6l), is(equalTo(720l)));
		assertThat(factorials.get(7l), is(equalTo(5040l)));
		assertThat(factorials.get(8l), is(equalTo(40320l)));
		assertThat(factorials.get(9l), is(equalTo(362880l)));
		assertThat(factorials.get(10l), is(equalTo(3628800l)));
	}

	@Configuration
	public static class SpringGemFireClientConfiguration {

		@Bean
		PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
			return new PropertySourcesPlaceholderConfigurer();
		}

		@Bean
		Properties gemfireProperties(@Value("${gemfire.log.level:config}") String logLevel) {
			Properties gemfireProperties = new Properties();

			gemfireProperties.setProperty("log-level", logLevel);

			return gemfireProperties;
		}

		@Bean(name = GemfireConstants.DEFAULT_GEMFIRE_POOL_NAME)
		PoolFactoryBean gemfirePool(@Value("${gemfire.cache.server.host:localhost}") String host,
			@Value("${gemfire.cache.server.port:12480}") int port)
		{
			PoolFactoryBean gemfirePool = new PoolFactoryBean();

			gemfirePool.setName(GemfireConstants.DEFAULT_GEMFIRE_POOL_NAME);
			gemfirePool.setFreeConnectionTimeout(5000); // 5 seconds
			gemfirePool.setKeepAlive(false);
			gemfirePool.setMaxConnections(SpringGemFireServerApplication.DEFAULT_MAX_CONNECTIONS);
			gemfirePool.setMinConnections(1);
			gemfirePool.setPingInterval(TimeUnit.SECONDS.toMillis(5));
			gemfirePool.setReadTimeout(2000); // 2 seconds
			gemfirePool.setRetryAttempts(1);
			gemfirePool.setSubscriptionEnabled(true);
			gemfirePool.setThreadLocalConnections(false);

			gemfirePool.setServers(Collections.singletonList(new InetSocketAddress(host, port)));

			return gemfirePool;
		}

		@Bean
		ClientCacheFactoryBean gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties,
			Pool gemfirePool)
		{
			ClientCacheFactoryBean gemfireCache = new ClientCacheFactoryBean();

			gemfireCache.setClose(true);
			gemfireCache.setLazyInitialize(false);
			gemfireCache.setProperties(gemfireProperties);
			gemfireCache.setPool(gemfirePool);
			gemfireCache.setUseBeanFactoryLocator(false);

			return gemfireCache;
		}

		@Bean(name = "Factorials")
		ClientRegionFactoryBean<Long, Long> factorialsRegion(ClientCache gemfireCache, Pool gemfirePool) {
			ClientRegionFactoryBean<Long, Long> factorialsRegion = new ClientRegionFactoryBean<>();

			factorialsRegion.setCache(gemfireCache);
			factorialsRegion.setName("Factorials");
			factorialsRegion.setPool(gemfirePool);
			factorialsRegion.setShortcut(ClientRegionShortcut.PROXY);

			return factorialsRegion;
		}
	}

}
