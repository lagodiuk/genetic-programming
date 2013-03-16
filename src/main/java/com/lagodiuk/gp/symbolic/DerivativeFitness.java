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

import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;

public abstract class DerivativeFitness implements ExpressionFitness {

	private double left = -10;

	private double right = 10;

	private double dx = 1e-5;

	private double step = 1;

	public abstract double f(double x);

	@Override
	public double fitness(Expression expression, Context context) {
		double delt = 0;

		for (double x = this.left; x <= this.right; x += this.step) {
			double target = (this.f(x + this.dx) - this.f(x)) / this.dx;

			context.setVariable("x", x);
			double exprVal = expression.eval(context);

			delt += this.sqr(target - exprVal);
		}

		return delt;
	}

	private double sqr(double x) {
		return x * x;
	}

	public double getLeft() {
		return this.left;
	}

	public DerivativeFitness setLeft(double left) {
		this.left = left;
		return this;
	}

	public double getRight() {
		return this.right;
	}

	public DerivativeFitness setRight(double right) {
		this.right = right;
		return this;
	}

	public double getDx() {
		return this.dx;
	}

	public DerivativeFitness setDx(double dx) {
		this.dx = dx;
		return this;
	}

	public double getStep() {
		return this.step;
	}

	public DerivativeFitness setStep(double step) {
		this.step = step;
		return this;
	}

}
