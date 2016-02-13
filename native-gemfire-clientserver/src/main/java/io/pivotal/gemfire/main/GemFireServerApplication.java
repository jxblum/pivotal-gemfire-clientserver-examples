package io.pivotal.gemfire.main;

import java.io.IOException;
import java.util.Properties;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.CacheLoader;
import com.gemstone.gemfire.cache.CacheLoaderException;
import com.gemstone.gemfire.cache.DataPolicy;
import com.gemstone.gemfire.cache.LoaderHelper;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionFactory;
import com.gemstone.gemfire.cache.server.CacheServer;

/**
 * The GemFireServerApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class GemFireServerApplication {

	public static final int DEFAULT_MAX_CONNECTIONS = 100;

	public static void main(String[] args) throws Exception {
		Cache gemfireCache = gemfireCache(gemfireProperties());
		cubesRegion(gemfireCache);
		gemfireCacheServer(gemfireCache);
	}

	static Properties gemfireProperties() {
		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("name", GemFireServerApplication.class.getSimpleName());
		gemfireProperties.setProperty("mcast-port", "0");
		gemfireProperties.setProperty("log-level", System.getProperty("gemfire.log-level", "config"));
		gemfireProperties.setProperty("locators", System.getProperty("gemfire.locators", "localhost[11235]"));
		gemfireProperties.setProperty("start-locator", System.getProperty("gemfire.locators", "localhost[11235]"));
		gemfireProperties.setProperty("jmx-manager", "true");
		gemfireProperties.setProperty("jmx-manager-port", System.getProperty("gemfire.jmx-manager-port", "1199"));
		gemfireProperties.setProperty("jmx-manager-start", "true");

		return gemfireProperties;
	}

	static Cache gemfireCache(Properties gemfireProperties) {
		return new CacheFactory(gemfireProperties).create();
	}

	static CacheServer gemfireCacheServer(Cache gemfireCache) throws IOException {
		CacheServer gemfireCacheServer = gemfireCache.addCacheServer();

		gemfireCacheServer.setBindAddress(System.getProperty("gemfire.cache.server.bind-address", "localhost"));
		gemfireCacheServer.setHostnameForClients(System.getProperty("gemfire.cache.server.hostname-for-clients", "localhost"));
		gemfireCacheServer.setPort(Integer.getInteger("gemfire.cache.server.port", 12480));
		gemfireCacheServer.setMaxConnections(DEFAULT_MAX_CONNECTIONS);
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

}
