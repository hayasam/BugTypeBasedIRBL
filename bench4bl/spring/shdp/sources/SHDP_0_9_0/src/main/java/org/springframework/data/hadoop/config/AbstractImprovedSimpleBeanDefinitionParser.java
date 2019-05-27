/*
 * Copyright 2011 the original author or authors.
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
package org.springframework.data.hadoop.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 * Simple extension to {@link AbstractSimpleBeanDefinitionParser} that handles properties references (-ref).
 * 
 * @author Costin Leau
 */
abstract class AbstractImprovedSimpleBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		NamedNodeMap attributes = element.getAttributes();
		for (int x = 0; x < attributes.getLength(); x++) {
			Attr attribute = (Attr) attributes.item(x);
			if (isEligibleAttribute(attribute, parserContext)) {
				String attributeName = attribute.getLocalName();
				boolean isReference = NamespaceUtils.isReference(attributeName);
				String propertyName = extractPropertyName((isReference ? attributeName.substring(0,
						attributeName.length() - 4) : attributeName));
				Assert.state(StringUtils.hasText(propertyName),
						"Illegal property name returned from 'extractPropertyName(String)': cannot be null or empty.");

				if (isReference) {
					builder.addPropertyReference(propertyName, attribute.getValue());
				}
				else {
					builder.addPropertyValue(propertyName, attribute.getValue());
				}
			}
		}
		postProcess(builder, element);
	}

	protected String defaultId(ParserContext context, Element element) {
		context.getReaderContext().error(
				"Id is required for element '" + context.getDelegate().getLocalName(element)
						+ "' when used as a top-level tag", element);

		return null;
	}

	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
			throws BeanDefinitionStoreException {
		String name = super.resolveId(element, definition, parserContext);
		if (!StringUtils.hasText(name)) {
			name = defaultId(parserContext, element);
		}
		return name;
	}
}
