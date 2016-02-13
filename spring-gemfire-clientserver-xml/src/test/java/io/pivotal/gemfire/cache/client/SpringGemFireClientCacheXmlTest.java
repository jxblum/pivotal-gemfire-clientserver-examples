package io.pivotal.gemfire.cache.client;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gemstone.gemfire.cache.DataPolicy;
import com.gemstone.gemfire.cache.Region;

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
		assertThat(squares.get(0l), is(equalTo(0l)));
		assertThat(squares.get(1l), is(equalTo(1l)));
		assertThat(squares.get(2l), is(equalTo(4l)));
		assertThat(squares.get(3l), is(equalTo(9l)));
		assertThat(squares.get(4l), is(equalTo(16l)));
		assertThat(squares.get(5l), is(equalTo(25l)));
		assertThat(squares.get(6l), is(equalTo(36l)));
		assertThat(squares.get(7l), is(equalTo(49l)));
		assertThat(squares.get(8l), is(equalTo(64l)));
		assertThat(squares.get(9l), is(equalTo(81l)));
		assertThat(squares.get(10l), is(equalTo(100l)));
	}

}
