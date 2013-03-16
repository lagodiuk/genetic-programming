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
import java.util.LinkedList;
import java.util.List;

import com.lagodiuk.gp.symbolic.SymbolicRegressionEngine;
import com.lagodiuk.gp.symbolic.SymbolicRegressionIterationListener;
import com.lagodiuk.gp.symbolic.TabulatedFunctionFitness;
import com.lagodiuk.gp.symbolic.Target;
import com.lagodiuk.gp.symbolic.interpreter.Expression;
import com.lagodiuk.gp.symbolic.interpreter.Functions;

/**
 * f(x) - ? <br/>
 * 
 * f(0) = 0 <br/>
 * f(1) = 11 <br/>
 * f(2) = 24 <br/>
 * f(3) = 39 <br/>
 * f(4) = 56 <br/>
 * f(5) = 75 <br/>
 * f(6) = 96 <br/>
 * 
 * (target function is f(x) = x^2 + 10*x)
 */
public class HelloSymbolicRegression {

	public static void main(String[] args) {

		// define training set
		TabulatedFunctionFitness fitness =
				new TabulatedFunctionFitness(
						new Target().when("x", 0).targetIs(0),
						new Target().when("x", 1).targetIs(11),
						new Target().when("x", 2).targetIs(24),
						new Target().when("x", 3).targetIs(39),
						new Target().when("x", 4).targetIs(56),
						new Target().when("x", 5).targetIs(75),
						new Target().when("x", 6).targetIs(96));

		SymbolicRegressionEngine engine =
				new SymbolicRegressionEngine(
						fitness,
						// define variables
						list("x"),
						// define base functions
						list(Functions.ADD, Functions.SUB, Functions.MUL, Functions.VARIABLE, Functions.CONSTANT));

		addListener(engine);

		// 200 iterations
		engine.evolve(200);
	}

	/**
	 * Track each iteration
	 */
	private static void addListener(SymbolicRegressionEngine engine) {
		engine.addIterationListener(new SymbolicRegressionIterationListener() {
			@Override
			public void update(SymbolicRegressionEngine engine) {

				Expression bestSyntaxTree = engine.getBestSyntaxTree();

				double currFitValue = engine.fitness(bestSyntaxTree);

				// log to console
				System.out.println(
						String.format("iter = %s \t fit = %s \t func = %s",
								engine.getIteration(), currFitValue, bestSyntaxTree.print()));

				// halt condition
				if (currFitValue < 5) {
					engine.terminate();
				}
			}
		});
	}

	private static <T> List<T> list(T... items) {
		List<T> list = new LinkedList<T>();
		for (T item : items) {
			list.add(item);
		}
		return list;
	}
}
