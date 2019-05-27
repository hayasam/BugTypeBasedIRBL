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
package org.springframework.yarn.am.cluster;

import java.util.Map;

import org.springframework.yarn.am.grid.GridProjection;
import org.springframework.yarn.support.statemachine.StateMachine;
import org.springframework.yarn.support.statemachine.state.State;

public class DefaultContainerCluster implements ContainerCluster {

	private final String id;

	private final GridProjection projection;

	private StateMachine<State<ClusterState, ClusterEvent>, ClusterEvent> stateMachine;

	private Map<String, Object> extraProperties;

	public DefaultContainerCluster(String id, GridProjection projection, StateMachine<State<ClusterState, ClusterEvent>, ClusterEvent> stateMachine) {
		this(id, projection, stateMachine, null);
	}

	public DefaultContainerCluster(String id, GridProjection projection, StateMachine<State<ClusterState, ClusterEvent>, ClusterEvent> stateMachine, Map<String, Object> extraProperties) {
		this.id = id;
		this.projection = projection;
		this.stateMachine = stateMachine;
		this.extraProperties = extraProperties;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public GridProjection getGridProjection() {
		return projection;
	}

	@Override
	public StateMachine<State<ClusterState, ClusterEvent>, ClusterEvent> getStateMachine() {
		return stateMachine;
	}

	@Override
	public Map<String, Object> getExtraProperties() {
		return extraProperties;
	}

}
