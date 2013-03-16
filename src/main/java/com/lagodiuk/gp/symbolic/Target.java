/*******************************************************************************
 * Copyright 2012 Yuriy Lagodiuk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.lagodiuk.gp.symbolic;

import java.util.HashMap;
import java.util.Map;

public class Target {
	private Map<String, Double> contextState = new HashMap<String, Double>();
	private double targetValue;

	public Target() {
	}

	public Target(Map<String, Double> contextState, double targetValue) {
		this.contextState.putAll(contextState);
		this.targetValue = targetValue;
	}

	public Target when(String variableName, double variableValue) {
		this.contextState.put(variableName, variableValue);
		return this;
	}

	public Target targetIs(double targetValue) {
		this.targetValue = targetValue;
		return this;
	}

	public double getTargetValue() {
		return this.targetValue;
	}

	public Map<String, Double> getContextState() {
		return this.contextState;
	}
}
