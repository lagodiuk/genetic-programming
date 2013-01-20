package com.lagodiuk.gp.symbolic;

import java.util.Collection;
import java.util.List;

import com.lagodiuk.ga.GeneticAlgorithm;
import com.lagodiuk.ga.Fitness;
import com.lagodiuk.ga.IterartionListener;
import com.lagodiuk.ga.Population;
import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;
import com.lagodiuk.gp.symbolic.interpreter.Function;
import com.lagodiuk.gp.symbolic.interpreter.SyntaxTreeUtils;

public class SymbolicRegressionEngine {

	private static final int INITIAL_PARENT_CHROMOSOMES_SURVIVE_COUNT = 1;

	private static final int DEFAULT_POPULATION_SIZE = 5;

	private static final int MAX_INITIAL_TREE_DEPTH = 1;

	private GeneticAlgorithm<GpChromosome, Double> environment;

	private Context context;

	private ExpressionFitness expressionFitness;

	public SymbolicRegressionEngine(ExpressionFitness expressionFitness, Collection<String> variables, List<? extends Function> baseFunctions) {
		this.context = new Context(baseFunctions, variables);
		this.expressionFitness = expressionFitness;
		SymbolicRegressionFitness fitnessFunction = new SymbolicRegressionFitness(this.expressionFitness);
		Population<GpChromosome> population = this.createPopulation(this.context, fitnessFunction, DEFAULT_POPULATION_SIZE);
		this.environment = new GeneticAlgorithm<GpChromosome, Double>(population, fitnessFunction);
		this.environment.setParentChromosomesSurviveCount(INITIAL_PARENT_CHROMOSOMES_SURVIVE_COUNT);
	}

	private Population<GpChromosome> createPopulation(Context context, Fitness<GpChromosome, Double> fitnessFunction, int populationSize) {
		Population<GpChromosome> population = new Population<GpChromosome>();
		for (int i = 0; i < populationSize; i++) {
			GpChromosome chromosome =
					new GpChromosome(context, fitnessFunction, SyntaxTreeUtils.createTree(MAX_INITIAL_TREE_DEPTH, context));
			population.addChromosome(chromosome);
		}
		return population;
	}

	public void addIterationListener(final SymbolicRegressionIterationListener listener) {
		this.environment.addIterationListener(new IterartionListener<GpChromosome, Double>() {
			@Override
			public void update(GeneticAlgorithm<GpChromosome, Double> environment) {
				listener.update(SymbolicRegressionEngine.this);
			}
		});
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

	public double fitness(Expression expression) {
		return this.expressionFitness.fitness(expression, this.context);
	}

	public void terminate() {
		this.environment.terminate();
	}

	public int getIteration() {
		return this.environment.getIteration();
	}

	public void setParentsSurviveCount(int n) {
		this.environment.setParentChromosomesSurviveCount(n);
	}

}
