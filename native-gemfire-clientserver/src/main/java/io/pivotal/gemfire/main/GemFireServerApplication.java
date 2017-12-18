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

package io.pivotal.gemfire.main;

import java.io.IOException;
import java.util.Properties;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.CacheLoaderException;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.LoaderHelper;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionFactory;
import org.apache.geode.cache.server.CacheServer;
import org.springframework.util.StringUtils;

/**
 * The GemFireServerApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class GemFireServerApplication {

	public static final int DEFAULT_MAX_CONNECTIONS = 100;
	public static final int DEFAULT_MAX_TIME_BETWEEN_PINGS = 60000;

	public static void main(String[] args) throws Exception {

		Cache gemfireCache = gemfireCache(gemfireProperties());

		cubesRegion(gemfireCache);
		gemfireCacheServer(gemfireCache);
		registerShutdownHook(gemfireCache);
	}

	static String systemProperty(String propertyName, String defaultValue) {

		String propertyValue = System.getProperty(propertyName);

		return (StringUtils.hasText(propertyValue) ? propertyValue : defaultValue);
	}

	static Properties gemfireProperties() {

		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("name", GemFireServerApplication.class.getSimpleName());
		gemfireProperties.setProperty("log-level", systemProperty("gemfire.log.level", "config"));
		gemfireProperties.setProperty("locators", systemProperty("gemfire.locator.host-port", "localhost[11235]"));
		gemfireProperties.setProperty("jmx-manager", "true");
		gemfireProperties.setProperty("jmx-manager-port", systemProperty("gemfire.manager.port", "1199"));
		gemfireProperties.setProperty("jmx-manager-start", "true");
		gemfireProperties.setProperty("start-locator", systemProperty("gemfire.locator.host-port", "localhost[11235]"));

		return gemfireProperties;
	}

	static Cache gemfireCache(Properties gemfireProperties) {
		return new CacheFactory(gemfireProperties).create();
	}

	static CacheServer gemfireCacheServer(Cache gemfireCache) throws IOException {

		CacheServer gemfireCacheServer = gemfireCache.addCacheServer();

		gemfireCacheServer.setBindAddress(systemProperty("gemfire.cache.server.bind-address", "localhost"));
		gemfireCacheServer.setHostnameForClients(systemProperty("gemfire.cache.server.hostname-for-clients", "localhost"));
		gemfireCacheServer.setPort(Integer.getInteger("gemfire.cache.server.port", 12480));
		gemfireCacheServer.setMaxConnections(DEFAULT_MAX_CONNECTIONS);
		gemfireCacheServer.setMaximumTimeBetweenPings(DEFAULT_MAX_TIME_BETWEEN_PINGS);
		gemfireCacheServer.start();

		return gemfireCacheServer;
	}

	static Region<Long, Long> cubesRegion(Cache gemfireCache) {

		RegionFactory<Long, Long> cubesRegionFactory = gemfireCache.createRegionFactory();

		cubesRegionFactory.setCacheLoader(cubesCacheLoader());
		cubesRegionFactory.setDataPolicy(DataPolicy.PARTITION);
		cubesRegionFactory.setKeyConstraint(Long.class);
		cubesRegionFactory.setValueConstraint(Long.class);

		return cubesRegionFactory.create("Cubes");
	}

	static CacheLoader<Long, Long> cubesCacheLoader() {

		return new CacheLoader<Long, Long>() {
			public Long load(final LoaderHelper<Long, Long> helper) throws CacheLoaderException {
				long number = helper.getKey();
				return new Double(Math.pow(number, 3)).longValue();
			}

			public void close() {
			}
		};
	}

	static void registerShutdownHook(Cache gemfireCache) {
		Runtime.getRuntime().addShutdownHook(new Thread(gemfireCache::close, "GemFire Server Shutdown Thread"));
	}
}
