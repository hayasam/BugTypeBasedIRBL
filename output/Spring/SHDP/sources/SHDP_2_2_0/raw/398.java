/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.hadoop.store.output;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.springframework.data.hadoop.store.AbstractStoreTests;
import org.springframework.data.hadoop.store.StoreException;
import org.springframework.data.hadoop.store.TestUtils;
import org.springframework.data.hadoop.store.codec.CodecInfo;
import org.springframework.data.hadoop.store.strategy.naming.RollingFileNamingStrategy;
import org.springframework.data.hadoop.test.context.HadoopDelegatingSmartContextLoader;
import org.springframework.data.hadoop.test.context.MiniHadoopCluster;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(loader=HadoopDelegatingSmartContextLoader.class)
@MiniHadoopCluster
public class FileWriteOpenTests extends AbstractStoreTests {

	private static final Log log = LogFactory.getLog(FileWriteOpenTests.class);

	@org.springframework.context.annotation.Configuration
	static class Config {
		// just empty to survive without xml configs
	}

	@Test
	public void testDoubleOpenWriteFailure() throws Exception {
		log.info("testDoubleOpenWriteFailure1");
		// what we do here is to make sure that if we're
		// unable to roll files, by forcing path in TestTextFileWriter,
		// we wont get error from hadoop which would look like
		// java.io.FileNotFoundException: ID mismatch. Request id and saved id: 16399 , 16400
		// which happens if two writers use same file at a same time
		// and file is opened in overwrite mode.
		String[] dataArray = new String[] { DATA10 };

		TestTextFileWriter writer1 = new TestTextFileWriter(getConfiguration(), testDefaultPath, null);
		writer1.setMaxOpenAttempts(1);
		writer1.setFileNamingStrategy(new RollingFileNamingStrategy());
		TestTextFileWriter writer2 = new TestTextFileWriter(getConfiguration(), testDefaultPath, null);
		writer2.setFileNamingStrategy(new RollingFileNamingStrategy());
		writer2.setMaxOpenAttempts(1);

		Exception catched = null;
		try {
			TestUtils.writeData(writer1, dataArray, false);
			TestUtils.writeData(writer2, dataArray, false);
		} catch (Exception e) {
			catched = e;
		}

		TestUtils.close(writer1);
		TestUtils.close(writer2);

		assertThat(catched, instanceOf(StoreException.class));
		assertThat(catched.getMessage(), containsString("We've reached"));
	}

	@Test
	public void testDoubleOpenWriteFailureShouldNotHappen() throws Exception {
		// mostly similar that testDoubleOpenWriteFailure() but
		// we don't force the path so rollover should work
		String[] dataArray = new String[] { DATA10 };

		TextFileWriter writer1 = new TextFileWriter(getConfiguration(), testDefaultPath, null);
		writer1.setFileNamingStrategy(new RollingFileNamingStrategy());
		TextFileWriter writer2 = new TextFileWriter(getConfiguration(), testDefaultPath, null);
		writer2.setFileNamingStrategy(new RollingFileNamingStrategy());

		TestUtils.writeData(writer1, dataArray, false);
		TestUtils.writeData(writer2, dataArray, false);
		TestUtils.close(writer1);
		TestUtils.close(writer2);
		// we're ok if we don't get exceptions
	}

	private static class TestTextFileWriter extends TextFileWriter {

		private Path path;

		public TestTextFileWriter(Configuration configuration, Path basePath, CodecInfo codec) {
			super(configuration, basePath, codec);
			this.path = basePath;
		}

		@Override
		protected Path getResolvedPath() {
			return path;
		}

	}

}
