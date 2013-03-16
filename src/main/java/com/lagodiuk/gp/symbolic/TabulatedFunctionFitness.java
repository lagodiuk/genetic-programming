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

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;

public class TabulatedFunctionFitness implements ExpressionFitness {

	private List<Target> targets = new LinkedList<Target>();

	public TabulatedFunctionFitness(Target... targets) {
		for (Target target : targets) {
			this.targets.add(target);
		}
	}

	public TabulatedFunctionFitness(List<Target> targets) {
		this.targets.addAll(targets);
	}

	@Override
	public double fitness(Expression expression, Context context) {
		double diff = 0;

		for (Target target : this.targets) {
			for (Entry<String, Double> e : target.getContextState().entrySet()) {
				String variableName = e.getKey();
				Double variableValue = e.getValue();
				context.setVariable(variableName, variableValue);
			}
			double targetValue = target.getTargetValue();
			double calculatedValue = expression.eval(context);
			diff += this.sqr(targetValue - calculatedValue);
		}

		return diff;
	}

	private double sqr(double x) {
		return x * x;
	}

}
