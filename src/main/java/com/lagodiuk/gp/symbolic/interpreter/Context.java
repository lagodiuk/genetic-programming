package com.lagodiuk.gp.symbolic.interpreter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Context {

	private Random random = new Random();

	private double minValue = -50;

	private double maxValue = 50;

	private double minMutationValue = -3;

	private double maxMutationValue = 3;

	private Map<String, Double> variables = new HashMap<String, Double>();

	private List<Function> allFunctions = new ArrayList<Function>();

	private List<Function> terminalFunctions = new ArrayList<Function>();

	private int nextRndFunctionIndx = 0;

	public Context(List<Function> functions) {
		this(functions, Collections.<String> emptyList());
	}

	public Context(List<? extends Function> functions, Collection<String> variables) {
		this.allFunctions.addAll(functions);
		for (Function f : functions) {
			if (f.argumentsCount() == 0) {
				this.terminalFunctions.add(f);
			}
		}
		if (this.terminalFunctions.isEmpty()) {
			throw new IllegalArgumentException("At least one terminal function must be defined");
		}

		Collections.shuffle(this.allFunctions);

		for (String variable : variables) {
			this.setVariable(variable, 0);
		}
	}

	public double lookupVariable(String variable) {
		return this.variables.get(variable);
	}

	public void setVariable(String variable, double value) {
		this.variables.put(variable, value);
	}

	public void removeVariable(String variable) {
		this.variables.remove(variable);
	}

	public void removeAllVariables() {
		this.variables.clear();
	}

	public Function getRandomFunction() {
		if (this.nextRndFunctionIndx >= this.allFunctions.size()) {
			this.nextRndFunctionIndx = 0;
			Collections.shuffle(this.allFunctions);
		}
		// round-robin like selection
		return this.allFunctions.get(this.nextRndFunctionIndx++);
	}

	public Function getRandomTerminalFunction() {
		while (true) {
			int indx = this.random.nextInt(this.terminalFunctions.size());
			Function f = this.terminalFunctions.get(indx);

			if ((!this.hasVariables()) && (f.isVariable())) {
				// if context doesn't contain variables
				continue;
			}

			return f;
		}
	}

	public List<Function> getTerminalFunctions() {
		return this.terminalFunctions;
	}

	public String getRandomVariableName() {
		int indx = this.random.nextInt(this.variables.keySet().size());
		int i = 0;
		for (String varName : this.variables.keySet()) {
			if (i == indx) {
				return varName;
			}
			++i;
		}
		// Unreachable code
		return this.variables.keySet().iterator().next();
	}

	public double getRandomValue() {
		return (this.random.nextGaussian() * (this.maxValue - this.minValue)) + this.minValue;
	}

	public double getRandomMutationValue() {
		return (this.random.nextGaussian() * (this.maxMutationValue - this.minMutationValue)) + this.minMutationValue;
	}

	public boolean hasVariables() {
		return !this.variables.isEmpty();
	}

}