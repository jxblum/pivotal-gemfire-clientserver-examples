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

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.Region;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The SpringGemFireClientCacheXmlTest class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SuppressWarnings("unused")
public class SpringGemFireClientCacheXmlTest {

	@Resource(name = "Squares")
	private Region<Long, Long> squares;

	@Before
	public void setup() {

		assertThat(squares, is(notNullValue()));
		assertThat(squares.getName(), is(equalTo("Squares")));
		assertThat(squares.getFullPath(), is(equalTo(String.format("%1$sSquares", Region.SEPARATOR))));
		assertThat(squares.getAttributes(), is(notNullValue()));
		assertThat(squares.getAttributes().getDataPolicy(), is(equalTo(DataPolicy.EMPTY)));
		assertThat(squares.getAttributes().getPoolName(), is(equalTo("serverPool")));
	}

	@Test
	public void computeSquares() {

		assertThat(squares.get(0L), is(equalTo(0L)));
		assertThat(squares.get(1L), is(equalTo(1L)));
		assertThat(squares.get(2L), is(equalTo(4L)));
		assertThat(squares.get(3L), is(equalTo(9L)));
		assertThat(squares.get(4L), is(equalTo(16L)));
		assertThat(squares.get(5L), is(equalTo(25L)));
		assertThat(squares.get(6L), is(equalTo(36L)));
		assertThat(squares.get(7L), is(equalTo(49L)));
		assertThat(squares.get(8L), is(equalTo(64L)));
		assertThat(squares.get(9L), is(equalTo(81L)));
		assertThat(squares.get(10L), is(equalTo(100L)));
	}
}
