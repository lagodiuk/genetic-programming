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