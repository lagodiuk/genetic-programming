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
