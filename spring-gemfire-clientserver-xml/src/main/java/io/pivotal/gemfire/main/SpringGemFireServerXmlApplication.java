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

import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.CacheLoaderException;
import org.apache.geode.cache.LoaderHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

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
