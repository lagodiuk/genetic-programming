package com.lagodiuk.gp.example;

import java.util.LinkedList;
import java.util.List;

import com.lagodiuk.gp.symbolic.SymbolicRegressionEngine;
import com.lagodiuk.gp.symbolic.SymbolicRegressionIterationListener;
import com.lagodiuk.gp.symbolic.TabulatedFunctionFitness;
import com.lagodiuk.gp.symbolic.Target;
import com.lagodiuk.gp.symbolic.interpreter.Expression;
import com.lagodiuk.gp.symbolic.interpreter.Functions;

public class LauncherXYZ {

	/**
	 * Modified example from book "Programming collective intelligence"
	 */
	public static void main(String[] args) {
		TabulatedFunctionFitness fitnessFunction =
				new TabulatedFunctionFitness(
						new Target().when("x", 26).when("y", 35).when("z", 1).targetIs(830),
						new Target().when("x", 8).when("y", 24).when("z", -11).targetIs(130),
						new Target().when("x", 20).when("y", 1).when("z", 10).targetIs(477),
						new Target().when("x", 33).when("y", 11).when("z", 2).targetIs(1217),
						new Target().when("x", 37).when("y", 16).when("z", 7).targetIs(1524));
		SymbolicRegressionEngine engine =
				new SymbolicRegressionEngine(
						fitnessFunction,
						list("x", "y", "z"),
						list(Functions.ADD, Functions.SUB, Functions.MUL, Functions.VARIABLE, Functions.CONSTANT));

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
				if (currFitValue < 5) {
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
