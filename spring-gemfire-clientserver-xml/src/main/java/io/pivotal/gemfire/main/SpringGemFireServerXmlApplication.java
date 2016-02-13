package io.pivotal.gemfire.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import com.gemstone.gemfire.cache.CacheLoader;
import com.gemstone.gemfire.cache.CacheLoaderException;
import com.gemstone.gemfire.cache.LoaderHelper;

/**
 * The SpringGemFireServerXmlApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SpringBootApplication
@ImportResource("io/pivotal/gemfire/main/spring-gemfire-cache-server-context.xml")
@SuppressWarnings("unused")
public class SpringGemFireServerXmlApplication {

	public static final int DEFAULT_MAX_CONNECTIONS = 100;

	public static void main(String[] args) {
		SpringApplication.run(SpringGemFireServerXmlApplication.class, args);
	}

	public static class SquaresCacheLoader implements CacheLoader<Long, Long> {

		@Override
		public Long load(LoaderHelper<Long, Long> loaderHelper) throws CacheLoaderException {
			long number = loaderHelper.getKey();
			return (number * number);
		}

		@Override
		public void close() {
		}
	}

}
