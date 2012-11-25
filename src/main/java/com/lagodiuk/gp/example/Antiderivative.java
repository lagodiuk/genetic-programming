package com.lagodiuk.gp.example;

import com.lagodiuk.gp.symbolic.ExpressionFitness;
import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;

public class Antiderivative implements ExpressionFitness {

	private static double dx = 1e-5;

	@Override
	public double fitness(Expression expression, Context context) {
		double delt = 0;

		for (int x = -10; x < 11; x++) {
			double target = this.targetDerivative(x);

			double exprDerivative = expressionDerivative(expression, context, x);

			delt += this.sqr(target - exprDerivative);
		}

		return delt;
	}

	private double expressionDerivative(Expression expression, Context context, int x) {
		context.setVariable("x", x);
		double exprX = expression.eval(context);

		context.setVariable("x", x + dx);
		double exprXPlusdX = expression.eval(context);

		double exprDerivative = (exprXPlusdX - exprX) / dx;
		return exprDerivative;
	}

	private double targetDerivative(double x) {
		return x;
	}

	private double sqr(double x) {
		return x * x;
	}

}
