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

import java.util.Properties;

import org.springframework.util.StringUtils;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.CacheLoaderException;
import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.LoaderHelper;

/**
 * The GemFireServerXmlApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class GemFireServerXmlApplication {

	public static final String DEFAULT_MAX_CONNECTIONS = "100";
	public static final String DEFAULT_MAX_TIME_BETWEEN_PINGS = "60000";

	static {
		System.setProperty("BIND_ADDRESS", systemProperty("gemfire.cache.server.bind-address", "localhost"));
		System.setProperty("HOSTNAME_FOR_CLIENTS", systemProperty("gemfire.cache.server.hostname-for-clients",
			"localhost"));
		System.setProperty("PORT", systemProperty("gemfire.cache.server.port", "12480"));
		System.setProperty("MAX_CONNECTIONS", DEFAULT_MAX_CONNECTIONS);
		System.setProperty("MAX_TIME_BETWEEN_PINGS", DEFAULT_MAX_TIME_BETWEEN_PINGS);
	}

	public static void main(String[] args) throws Exception {
		registerShutdownHook(gemfireCache(gemfireProperties()));
	}

	static String systemProperty(String propertyName, String defaultValue) {
		String propertyValue = System.getProperty(propertyName);
		return (StringUtils.hasText(propertyValue) ? propertyValue : defaultValue);
	}

	static Properties gemfireProperties() {
		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("name", GemFireServerXmlApplication.class.getSimpleName());
		gemfireProperties.setProperty("mcast-port", "0");
		gemfireProperties.setProperty("log-level", systemProperty("gemfire.log.level", "config"));
		gemfireProperties.setProperty("locators", systemProperty("gemfire.locator.host-port", "localhost[11235]"));
		gemfireProperties.setProperty("start-locator", systemProperty("gemfire.locator.host-port", "localhost[11235]"));
		gemfireProperties.setProperty("jmx-manager", "true");
		gemfireProperties.setProperty("jmx-manager-port", systemProperty("gemfire.manager.port", "1199"));
		gemfireProperties.setProperty("jmx-manager-start", "true");

		return gemfireProperties;
	}

	static Cache gemfireCache(Properties gemfireProperties) {
		return new CacheFactory(gemfireProperties).set("cache-xml-file", "server-cache.xml").create();
	}

	static void registerShutdownHook(Cache gemfireCache) {
		Runtime.getRuntime().addShutdownHook(new Thread(gemfireCache::close, "GemFire Server Shutdown Thread"));
	}

	public static class SquareRootsCacheLoader implements CacheLoader<Long, Long>, Declarable {

		public Long load(final LoaderHelper<Long, Long> helper) throws CacheLoaderException {
			long number = helper.getKey();
			return new Double(Math.sqrt(number)).longValue();
		}

		public void init(final Properties props) {
		}

		public void close() {
		}
	}

}
