package com.lagodiuk.gp.example;

import com.lagodiuk.gp.symbolic.ExpressionFitness;
import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;

public class Antiderivative implements ExpressionFitness {

	private static double dx = 1e-2;

	@Override
	public double fitness(Expression expression, Context context) {
		double delt = 0;

		// To guarantee monotonic of evolved antiderivative
		// the best approach is to trace each point through intervals of dx
		// for (double x = -10; x < 10; x += dx) {
		// but for small dx it works extremly slow
		for (double x = -10; x < 10; x += 1) {

			double target = this.targetDerivative(x);

			double exprDerivative = this.expressionDerivative(expression, context, x);

			delt += this.sqr(target - exprDerivative);
		}

		return delt;
	}

	private double expressionDerivative(Expression expression, Context context, double x) {
		context.setVariable("x", x);
		double exprX = expression.eval(context);

		context.setVariable("x", x + dx);
		double exprXPlusdX = expression.eval(context);

		return (exprXPlusdX - exprX) / dx;
	}

	private double targetDerivative(double x) {
		return x * Math.sin(x);
	}

	private double sqr(double x) {
		return x * x;
	}

}
