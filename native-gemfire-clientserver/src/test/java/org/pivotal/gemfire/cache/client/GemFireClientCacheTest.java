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

import com.gemstone.gemfire.cache.DataPolicy;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientRegionFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;

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

		ClientRegionFactory<Long, Long> cubesRegionFactory = clientCache.createClientRegionFactory(
			ClientRegionShortcut.PROXY);

		cubesRegionFactory.setKeyConstraint(Long.class);
		cubesRegionFactory.setValueConstraint(Long.class);

		cubes = cubesRegionFactory.create("Cubes");
	}

	static Properties gemfireProperties() {
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
		assertThat(cubes.get(0l), is(equalTo(0l)));
		assertThat(cubes.get(1l), is(equalTo(1l)));
		assertThat(cubes.get(2l), is(equalTo(8l)));
		assertThat(cubes.get(3l), is(equalTo(27l)));
		assertThat(cubes.get(4l), is(equalTo(64l)));
		assertThat(cubes.get(5l), is(equalTo(125l)));
		assertThat(cubes.get(6l), is(equalTo(216l)));
		assertThat(cubes.get(7l), is(equalTo(343l)));
		assertThat(cubes.get(8l), is(equalTo(512l)));
		assertThat(cubes.get(9l), is(equalTo(729l)));
		assertThat(cubes.get(10l), is(equalTo(1000l)));
	}

}
