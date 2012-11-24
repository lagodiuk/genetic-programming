package com.lagodiuk.gp.math;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.lagodiuk.ga.Environment;
import com.lagodiuk.ga.IterartionListener;
import com.lagodiuk.gp.math.interpreter.Functions;

public class Launcher {

	public static void main(String[] args) {
		SymbolicRegression sr = new SymbolicRegression(new GpFitness2(), list("x"), list(Functions.values()));
		sr.addIterationListener(new IterartionListener<GpGene, Double>() {

			private Locale locale = new Locale("ru");

			private double prevFitValue = -1;

			@Override
			public void update(Environment<GpGene, Double> environment) {
				GpGene bestGene = environment.getBest();

				double currFitValue = environment.fitness(bestGene);

				if (Double.compare(currFitValue, this.prevFitValue) != 0) {
					System.out.println("Func = " + bestGene.getSyntaxTree().print());
				}

				System.out.println(String.format(this.locale, "%s \t %s", environment.getIteration(), currFitValue));

				this.prevFitValue = currFitValue;

				if (currFitValue < 10) {
					environment.terminate();
				}
			}
		});
		sr.evolve(200);
		System.out.println(sr.getBestSyntaxTree().print());
	}

	private static <T> List<T> list(T... items) {
		List<T> list = new LinkedList<T>();
		for (T item : items) {
			list.add(item);
		}
		return list;
	}
}
