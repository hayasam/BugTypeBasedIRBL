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
package org.springframework.yarn.support.statemachine;

import org.springframework.messaging.Message;
import org.springframework.yarn.support.statemachine.listener.StateMachineListener;

public interface StateMachine<S, E> {

	S getState();

	S getInitialState();

	void start();

	void sendEvent(Message<E> event);

	void sendEvent(E event);

	void addStateListener(StateMachineListener<S,E> listener);

}
