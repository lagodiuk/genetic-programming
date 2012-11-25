package com.lagodiuk.gp.example;

import com.lagodiuk.gp.symbolic.ExpressionFitness;
import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;

public class Derivative implements ExpressionFitness {

	private static double dx = 1e-5;

	@Override
	public double fitness(Expression expression, Context context) {
		double delt = 0;

		for (int x = -10; x < 11; x++) {
			double target = (this.f(x + dx) - this.f(x)) / dx;

			context.setVariable("x", x);
			double exprVal = expression.eval(context);

			delt += this.sqr(target - exprVal);
		}

		return delt;
	}

	private double f(double x) {
		return x * x * x;
	}

	private double sqr(double x) {
		return x * x;
	}
}
