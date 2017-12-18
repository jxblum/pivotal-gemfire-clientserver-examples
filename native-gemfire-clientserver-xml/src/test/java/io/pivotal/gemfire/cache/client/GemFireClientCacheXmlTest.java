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

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;

import io.pivotal.gemfire.main.GemFireServerXmlApplication;

/**
 * The GemFireClientCacheXmlTest class...
 *
 * @author John Blum
 * @since 1.0.0
 */
public class GemFireClientCacheXmlTest {

	private static ClientCache clientCache;

	private static Region<Long, Long> squareRoots;

	static {
		System.setProperty("HOST", System.getProperty("gemfire.cache.server.host", "localhost"));
		System.setProperty("PORT", System.getProperty("gemfire.cache.server.port", "12480"));
		System.setProperty("MAX_CONNECTIONS", GemFireServerXmlApplication.DEFAULT_MAX_CONNECTIONS);
	}

	@BeforeClass
	public static void setupGemFire() {
		clientCache = new ClientCacheFactory(gemfireProperties()).set("cache-xml-file", "client-cache.xml").create();
		squareRoots = clientCache.getRegion("SquareRoots");
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

		assertThat(squareRoots, is(notNullValue()));
		assertThat(squareRoots.getName(), is(equalTo("SquareRoots")));
		assertThat(squareRoots.getFullPath(), is(equalTo(String.format("%1$sSquareRoots", Region.SEPARATOR))));
		assertThat(squareRoots.getAttributes(), is(notNullValue()));
		assertThat(squareRoots.getAttributes().getDataPolicy(), is(equalTo(DataPolicy.EMPTY)));
		assertThat(squareRoots.getAttributes().getPoolName(), is(equalTo("serverPool")));
	}

	@Test
	public void computeSquareRoots() {

		assertThat(squareRoots.get(100L), is(equalTo(10L)));
		assertThat(squareRoots.get(81L), is(equalTo(9L)));
		assertThat(squareRoots.get(64L), is(equalTo(8L)));
		assertThat(squareRoots.get(49L), is(equalTo(7L)));
		assertThat(squareRoots.get(36L), is(equalTo(6L)));
		assertThat(squareRoots.get(25L), is(equalTo(5L)));
		assertThat(squareRoots.get(16L), is(equalTo(4L)));
		assertThat(squareRoots.get(9L), is(equalTo(3L)));
		assertThat(squareRoots.get(4L), is(equalTo(2L)));
		assertThat(squareRoots.get(1L), is(equalTo(1L)));
		assertThat(squareRoots.get(0L), is(equalTo(0L)));
	}
}
