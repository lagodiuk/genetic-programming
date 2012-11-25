package com.lagodiuk.gp.symbolic;

import com.lagodiuk.ga.Fitness;
import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;

class SymbolicRegressionFitness implements Fitness<GpGene, Double> {

	private ExpressionFitness expressionFitness;

	public SymbolicRegressionFitness(ExpressionFitness expressionFitness) {
		this.expressionFitness = expressionFitness;
	}

	@Override
	public Double calculate(GpGene gene) {
		Expression expression = gene.getSyntaxTree();
		Context context = gene.getContext();
		return this.expressionFitness.fitness(expression, context);
	}

}
