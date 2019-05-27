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
package org.springframework.yarn.boot;

import org.springframework.boot.actuate.autoconfigure.EndpointWebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.yarn.am.YarnAppmaster;
import org.springframework.yarn.am.cluster.ContainerClusterStateMachineConfiguration;
import org.springframework.yarn.boot.condition.ConditionalOnYarnAppmaster;
import org.springframework.yarn.config.annotation.EnableYarn;

@Configuration
@ConditionalOnYarnAppmaster
@ConditionalOnClass(EnableYarn.class)
@ConditionalOnMissingBean(YarnAppmaster.class)
@ConditionalOnExpression("${spring.yarn.appmaster.containercluster.enabled:false}")
@AutoConfigureBefore({EndpointWebMvcAutoConfiguration.class, YarnAppmasterAutoConfiguration.class})
@Import(ContainerClusterStateMachineConfiguration.class)
public class ContainerClusterAppmasterAutoConfiguration {
}
