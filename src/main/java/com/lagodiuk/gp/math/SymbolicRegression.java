package com.lagodiuk.gp.math;

import java.util.Collection;
import java.util.List;

import com.lagodiuk.ga.Environment;
import com.lagodiuk.ga.Fitness;
import com.lagodiuk.ga.IterartionListener;
import com.lagodiuk.ga.Population;
import com.lagodiuk.gp.math.interpreter.Context;
import com.lagodiuk.gp.math.interpreter.Expression;
import com.lagodiuk.gp.math.interpreter.Function;
import com.lagodiuk.gp.math.interpreter.SyntaxTreeUtils;

public class SymbolicRegression {

	private static final int INITIAL_PARENT_GENES_SURVIVE_COUNT = 1;

	private static final int DEFAULT_POPULATION_SIZE = 5;

	private static final int MAX_INITIAL_TREE_DEPTH = 1;

	private Environment<GpGene, Double> environment;

	private Context context;

	public SymbolicRegression(Fitness<GpGene, Double> fitnessFunction, Collection<String> variables, List<? extends Function> baseFunctions) {
		this.context = new Context(baseFunctions, variables);
		Population<GpGene> population = this.createPopulation(this.context, fitnessFunction, DEFAULT_POPULATION_SIZE);
		this.environment = new Environment<GpGene, Double>(population, fitnessFunction);
		this.environment.setParentGenesSurviveCount(INITIAL_PARENT_GENES_SURVIVE_COUNT);
	}

	private Population<GpGene> createPopulation(Context context, Fitness<GpGene, Double> fitnessFunction, int populationSize) {
		Population<GpGene> population = new Population<GpGene>();
		for (int i = 0; i < populationSize; i++) {
			GpGene gene = new GpGene(context, fitnessFunction, SyntaxTreeUtils.createTree(MAX_INITIAL_TREE_DEPTH, context));
			population.addGene(gene);
		}
		return population;
	}

	public void addIterationListener(IterartionListener<GpGene, Double> listener) {
		this.environment.addIterationListener(listener);
	}

	public void evolve(int itrationsCount) {
		this.environment.iterate(itrationsCount);
	}

	public Context getContext() {
		return this.context;
	}

	public Expression getBestSyntaxTree() {
		return this.environment.getBest().getSyntaxTree();
	}

}
