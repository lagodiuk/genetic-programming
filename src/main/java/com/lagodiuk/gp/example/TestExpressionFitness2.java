package com.lagodiuk.gp.example;

import com.lagodiuk.gp.symbolic.ExpressionFitness;
import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;

public class TestExpressionFitness2 implements ExpressionFitness {

	@Override
	public double fitness(Expression expression, Context context) {
		double delt = 0;
		for (int x = -10; x < 10; x += 2) {
			context.setVariable("x", x);
			for (int y = -10; y < 10; y += 2) {
				context.setVariable("y", y);

				// double target = (x * 5) + (y * (y - 4));
				// double target = x + y;
				// double target = (x * 5) + (y * (y - 4)) + (x * y);
				// double target = x * x;
				double target = (x * x) + (y * y);

				double val = target - expression.eval(context);

				delt += val * val;
			}
		}
		return delt;
	}

}