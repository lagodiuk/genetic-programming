package com.lagodiuk.gp.math;

import java.util.LinkedList;
import java.util.List;

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
		Context context = new Context(functions);
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
			@Override
			public void update(Environment<GpGene, Double> environment) {
				GpGene bestGene = environment.getBest();

				double fitValue = environment.fitness(bestGene);

				System.out.println(environment.getIteration() + "\t" + fitValue);

				if (fitValue < 10) {
					environment.terminate();
				}
			}
		});

		env.iterate(200);

		System.out.println(env.getBest().getSyntaxTree().print(context));
		System.out.println(fit.calculate(env.getBest()));
	}

}