package com.lagodiuk.gp.math.interpreter;

import java.util.ArrayList;
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
		this.allFunctions.addAll(functions);
		for (Function f : functions) {
			if (f.argumentsCount() == 0) {
				this.terminalFunctions.add(f);
			}
		}

		Collections.shuffle(this.allFunctions);
	}

	public double lookup(String variable) {
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

	public Function getRandomFunction_old() {
		int indx = this.random.nextInt(this.allFunctions.size());
		return this.allFunctions.get(indx);
	}

	public Function getRandomFunction() {
		if (this.nextRndFunctionIndx >= this.allFunctions.size()) {
			this.nextRndFunctionIndx = 0;
			Collections.shuffle(this.allFunctions);
		}
		return this.allFunctions.get(this.nextRndFunctionIndx++);
	}

	public Function getRandomTerminalFunction() {
		while (true) {
			int indx = this.random.nextInt(this.terminalFunctions.size());
			Function f = this.terminalFunctions.get(indx);
			if ((!this.hasVariables()) && (f.isVariable())) {
				continue;
			}
			return f;
		}
	}

	public List<Function> getTerminalFunctions() {
		return this.terminalFunctions;
	}

	public String getRandomVariableName() {
		List<String> varNames = new ArrayList<String>();
		varNames.addAll(this.variables.keySet());
		int indx = this.random.nextInt(varNames.size());
		return varNames.get(indx);
	}

	public double getRandomValue() {
		// return Math.random() * ( maxValue - minValue ) + minValue;
		return (this.random.nextGaussian() * (this.maxValue - this.minValue)) + this.minValue;
	}

	public double getRandomMutationValue() {
		// return Math.random() * ( maxMutationValue - minMutationValue ) +
		// minMutationValue;
		return (this.random.nextGaussian() * (this.maxMutationValue - this.minMutationValue)) + this.minMutationValue;
	}

	public boolean hasVariables() {
		return !this.variables.isEmpty();
	}

}