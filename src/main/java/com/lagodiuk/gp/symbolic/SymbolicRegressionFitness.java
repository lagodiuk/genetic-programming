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
