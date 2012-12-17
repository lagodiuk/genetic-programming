package com.lagodiuk.gp.symbolic.example;

import java.util.LinkedList;
import java.util.List;

import com.lagodiuk.gp.symbolic.DerivativeFitness;
import com.lagodiuk.gp.symbolic.SymbolicRegressionEngine;
import com.lagodiuk.gp.symbolic.SymbolicRegressionIterationListener;
import com.lagodiuk.gp.symbolic.interpreter.Expression;
import com.lagodiuk.gp.symbolic.interpreter.Functions;

public class LauncherDerivative {

	public static void main(String[] args) {
		DerivativeFitness fitnessFunc = new DerivativeFitness() {
			@Override
			public double f(double x) {
				return (x * x * x) + (10 * x) + Math.pow(3, x);
			}
		};
		fitnessFunc.setLeft(-10).setRight(10).setStep(0.5).setDx(0.01);
		SymbolicRegressionEngine engine = new SymbolicRegressionEngine(fitnessFunc, list("x"), list(Functions.values()));
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
