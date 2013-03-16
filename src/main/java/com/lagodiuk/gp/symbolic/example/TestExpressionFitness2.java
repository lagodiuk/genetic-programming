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
