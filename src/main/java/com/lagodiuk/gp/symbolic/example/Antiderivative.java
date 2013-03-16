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
package com.lagodiuk.gp.symbolic.example;

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
