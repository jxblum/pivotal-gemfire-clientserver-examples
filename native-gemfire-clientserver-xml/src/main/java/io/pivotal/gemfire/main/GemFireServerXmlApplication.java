package io.pivotal.gemfire.main;

import java.util.Properties;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.CacheLoader;
import com.gemstone.gemfire.cache.CacheLoaderException;
import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.LoaderHelper;

/**
 * The GemFireServerXmlApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class GemFireServerXmlApplication {

	public static void main(String[] args) throws Exception {
		gemfireCache(gemfireProperties());
	}

	static Properties gemfireProperties() {
		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("name", GemFireServerXmlApplication.class.getSimpleName());
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
		return new CacheFactory(gemfireProperties).set("cache-xml-file", "server-cache.xml").create();
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
