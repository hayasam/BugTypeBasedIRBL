/*
 * Copyright 2013 the original author or authors.
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
package org.springframework.data.hadoop.test.context;

import org.springframework.test.context.SmartContextLoader;
import org.springframework.test.context.support.AbstractDelegatingSmartContextLoader;

/**
 * {@code HadoopDelegatingSmartContextLoader} is a concrete implementation of
 * {@link AbstractDelegatingSmartContextLoader} that delegates to a
 * {@link HadoopClusterInjectingXmlContextLoader} and an
 * {@link HadoopClusterInjectingAnnotationConfigContextLoader}.
 *
 * @author Janne Valkealahti
 * @see SmartContextLoader
 * @see AbstractDelegatingSmartContextLoader
 * @see HadoopClusterInjectingXmlContextLoader
 * @see HadoopClusterInjectingAnnotationConfigContextLoader
 */
public class HadoopDelegatingSmartContextLoader extends AbstractDelegatingSmartContextLoader {

	private final SmartContextLoader xmlLoader = new HadoopClusterInjectingXmlContextLoader();
	private final SmartContextLoader annotationConfigLoader = new HadoopClusterInjectingAnnotationConfigContextLoader();

	@Override
	protected SmartContextLoader getXmlLoader() {
		return this.xmlLoader;
	}

	@Override
	protected SmartContextLoader getAnnotationConfigLoader() {
		return this.annotationConfigLoader;
	}

}
