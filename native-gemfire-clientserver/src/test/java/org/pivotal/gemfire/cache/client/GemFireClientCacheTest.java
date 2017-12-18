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

package org.pivotal.gemfire.cache.client;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

import io.pivotal.gemfire.main.GemFireServerApplication;

/**
 * The GemFireClientCacheTest class...
 *
 * @author John Blum
 * @since 1.0.0
 */
public class GemFireClientCacheTest {

	private static ClientCache clientCache;

	private static Region<Long, Long> cubes;

	@BeforeClass
	public static void setupGemFire() {

		clientCache = new ClientCacheFactory(gemfireProperties())
			.setPoolFreeConnectionTimeout(5000) // 5 seconds
			.setPoolIdleTimeout(TimeUnit.SECONDS.toMillis(10))
			.setPoolMaxConnections(GemFireServerApplication.DEFAULT_MAX_CONNECTIONS)
			.setPoolMinConnections(1)
			.setPoolPingInterval(TimeUnit.SECONDS.toMillis(5))
			.setPoolReadTimeout(2000) // 2 seconds
			.setPoolRetryAttempts(1)
			.setPoolSubscriptionEnabled(true)
			.setPoolThreadLocalConnections(false)
			.addPoolServer(System.getProperty("gemfire.cache.server.host", "localhost"),
				Integer.getInteger("gemfire.cache.server.port", 12480))
			.create();

		ClientRegionFactory<Long, Long> cubesRegionFactory =
			clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY);

		cubesRegionFactory.setKeyConstraint(Long.class);
		cubesRegionFactory.setValueConstraint(Long.class);

		cubes = cubesRegionFactory.create("Cubes");
	}

	private static Properties gemfireProperties() {

		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("log-level", System.getProperty("gemfire.log.level", "config"));

		return gemfireProperties;
	}

	@AfterClass
	public static void tearDownGemFire() {
		if (clientCache != null) {
			clientCache.close(false);
		}
	}

	@Before
	public void setup() {

		assertThat(cubes, is(notNullValue()));
		assertThat(cubes.getName(), is(equalTo("Cubes")));
		assertThat(cubes.getFullPath(), is(equalTo(String.format("%1$sCubes", Region.SEPARATOR))));
		assertThat(cubes.getAttributes(), is(notNullValue()));
		assertThat(cubes.getAttributes().getDataPolicy(), is(equalTo(DataPolicy.EMPTY)));
		assertThat(cubes.getAttributes().getPoolName(), is(equalTo("DEFAULT")));
	}

	@Test
	public void computeCubes() {

		assertThat(cubes.get(0L), is(equalTo(0L)));
		assertThat(cubes.get(1L), is(equalTo(1L)));
		assertThat(cubes.get(2L), is(equalTo(8L)));
		assertThat(cubes.get(3L), is(equalTo(27L)));
		assertThat(cubes.get(4L), is(equalTo(64L)));
		assertThat(cubes.get(5L), is(equalTo(125L)));
		assertThat(cubes.get(6L), is(equalTo(216L)));
		assertThat(cubes.get(7L), is(equalTo(343L)));
		assertThat(cubes.get(8L), is(equalTo(512L)));
		assertThat(cubes.get(9L), is(equalTo(729L)));
		assertThat(cubes.get(10L), is(equalTo(1000L)));
	}

}
