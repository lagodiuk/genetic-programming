package com.lagodiuk.gp.symbolic.example;

import java.util.LinkedList;
import java.util.List;

import com.lagodiuk.gp.symbolic.SymbolicRegressionEngine;
import com.lagodiuk.gp.symbolic.SymbolicRegressionIterationListener;
import com.lagodiuk.gp.symbolic.TabulatedFunctionFitness;
import com.lagodiuk.gp.symbolic.Target;
import com.lagodiuk.gp.symbolic.interpreter.Expression;
import com.lagodiuk.gp.symbolic.interpreter.Functions;

public class Launcher2 {

	public static void main(String[] args) {
		TabulatedFunctionFitness fitnessFunction =
				new TabulatedFunctionFitness(
						new Target().when("x", 0).targetIs((0 * 0) + 0),
						new Target().when("x", 1).targetIs((1 * 1) + 1),
						new Target().when("x", 2).targetIs((2 * 2) + 2),
						new Target().when("x", 3).targetIs((3 * 3) + 3),
						new Target().when("x", 4).targetIs((4 * 4) + 4),
						new Target().when("x", 5).targetIs((5 * 5) + 5),
						new Target().when("x", 6).targetIs((6 * 6) + 6));
		SymbolicRegressionEngine engine = new SymbolicRegressionEngine(fitnessFunction, list("x"), list(Functions.values()));

		engine.addIterationListener(new SymbolicRegressionIterationListener() {
			private double prevFitValue = -1;

			@Override
			public void update(SymbolicRegressionEngine engine) {
				Expression bestSyntaxTree = engine.getBestSyntaxTree();
				double currFitValue = engine.fitness(bestSyntaxTree);
				if (Double.compare(currFitValue, this.prevFitValue) != 0) {
					System.out.println("Func = " + bestSyntaxTree.print());
				}
				System.out.println(String.format("%s \t %s", engine.getIteration(), currFitValue));
				this.prevFitValue = currFitValue;
				if (currFitValue < 10) {
					engine.terminate();
				}
			}
		});

		engine.evolve(200);
		System.out.println(engine.getBestSyntaxTree().print());
	}

	private static <T> List<T> list(T... items) {
		List<T> list = new LinkedList<T>();
		for (T item : items) {
			list.add(item);
		}
		return list;
	}

}
