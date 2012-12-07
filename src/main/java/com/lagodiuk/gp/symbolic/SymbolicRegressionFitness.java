package com.lagodiuk.gp.symbolic;

import com.lagodiuk.ga.Fitness;
import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;

class SymbolicRegressionFitness implements Fitness<GpChromosome, Double> {

	private ExpressionFitness expressionFitness;

	public SymbolicRegressionFitness(ExpressionFitness expressionFitness) {
		this.expressionFitness = expressionFitness;
	}

	@Override
	public Double calculate(GpChromosome chromosome) {
		Expression expression = chromosome.getSyntaxTree();
		Context context = chromosome.getContext();
		return this.expressionFitness.fitness(expression, context);
	}

}
