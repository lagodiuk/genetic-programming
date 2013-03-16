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

public class TestExpressionFitness implements ExpressionFitness {

	@Override
	public double fitness(Expression expression, Context context) {
		double delt = 0;
		for (int i = -20; i < 20; i++) {

			context.setVariable("x", i);

			double target = (i * i * i * 5) + i + 10;
			// double target = ((((3 * i * i * i) - (i * i * 7)) + (i * 10)) -
			// 35) * i;
			// double target = i * i;
			// double target = ( i + 100 ) * ( i - 100 );
			// double target = i + 6;

			// double target = (Math.cos((i * Math.PI) / 10) * 10) + i;
			// (2)^(SIN(A2*5)*3)+A2
			// double target = Math.pow(2, Math.sin(i * 5) * 3) + i;

			double x = target - expression.eval(context);

			delt += x * x;
		}
		return delt;
	}

}
