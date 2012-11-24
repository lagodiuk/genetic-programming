package com.lagodiuk.gp.math;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.lagodiuk.ga.Environment;
import com.lagodiuk.ga.Fitness;
import com.lagodiuk.ga.IterartionListener;
import com.lagodiuk.ga.Population;
import com.lagodiuk.gp.math.interpreter.Context;
import com.lagodiuk.gp.math.interpreter.Function;
import com.lagodiuk.gp.math.interpreter.Functions;
import com.lagodiuk.gp.math.interpreter.SyntaxTreeUtils;

public class Launcher {

	public static void main(String[] args) {

		List<Function> functions = new LinkedList<Function>();
		for (Function f : Functions.values()) {
			functions.add(f);
		}
		final Context context = new Context(functions);
		context.setVariable("x", 10);
		// context.setVariable( "y", 10 );

		Fitness<GpGene, Double> fit = new GpFitness();

		Population<GpGene> population = new Population<GpGene>();
		for (int i = 0; i < 5; i++) {
			GpGene gene = new GpGene(context, fit, SyntaxTreeUtils.createTree(1, context));
			population.addGene(gene);
		}

		Environment<GpGene, Double> env = new Environment<GpGene, Double>(population, fit);
		env.setParentGenesSurviveCount(1);

		env.addIterationListener(new IterartionListener<GpGene, Double>() {

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

		env.iterate(200);

		System.out.println("Func = " + env.getBest().getSyntaxTree().print());
	}

}