package com.lagodiuk.gp.symbolic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.lagodiuk.ga.Chromosome;
import com.lagodiuk.ga.Environment;
import com.lagodiuk.ga.Fitness;
import com.lagodiuk.ga.Population;
import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;
import com.lagodiuk.gp.symbolic.interpreter.Function;
import com.lagodiuk.gp.symbolic.interpreter.SyntaxTreeUtils;

class GpChromosome implements Chromosome<GpChromosome> {

	private Expression syntaxTree;

	private Context context;

	private Fitness<GpChromosome, Double> fitnessFunction;

	private Random random = new Random();

	public GpChromosome(Context context, Fitness<GpChromosome, Double> fitnessFunction, Expression syntaxTree) {
		this.context = context;
		this.fitnessFunction = fitnessFunction;
		this.syntaxTree = syntaxTree;
	}

	@Override
	public List<GpChromosome> crossover(GpChromosome anotherChromosome) {
		List<GpChromosome> ret = new ArrayList<GpChromosome>(2);

		GpChromosome thisClone = new GpChromosome(this.context, this.fitnessFunction, this.syntaxTree.clone());
		GpChromosome anotherClone = new GpChromosome(this.context, this.fitnessFunction, anotherChromosome.syntaxTree.clone());

		Expression thisRandomNode = this.getRandomNode(thisClone.syntaxTree);
		Expression anotherRandomNode = this.getRandomNode(anotherClone.syntaxTree);

		Expression thisRandomSubTreeClone = thisRandomNode.clone();
		Expression anotherRandomSubTreeClone = anotherRandomNode.clone();

		this.swapNode(thisRandomNode, anotherRandomSubTreeClone);
		this.swapNode(anotherRandomNode, thisRandomSubTreeClone);

		ret.add(thisClone);
		ret.add(anotherClone);

		thisClone.optimizeTree();
		anotherClone.optimizeTree();

		return ret;
	}

	@Override
	public GpChromosome mutate() {
		GpChromosome ret = new GpChromosome(this.context, this.fitnessFunction, this.syntaxTree.clone());

		int type = this.random.nextInt(7);
		switch (type) {
			case 0:
				ret.mutateByRandomChangeOfFunction();
				break;
			case 1:
				ret.mutateByRandomChangeOfChild();
				break;
			case 2:
				ret.mutateByRandomChangeOfNodeToChild();
				break;
			case 3:
				ret.mutateByReverseOfChildsList();
				break;
			case 4:
				ret.mutateByRootGrowth();
				break;
			case 5:
				ret.syntaxTree = SyntaxTreeUtils.createTree(2, this.context);
				break;
			case 6:
				ret.mutateByReplaceEntireTreeWithAnySubTree();
				break;
		}

		ret.optimizeTree();
		return ret;
	}

	private void mutateByReplaceEntireTreeWithAnySubTree() {
		this.syntaxTree = this.getRandomNode(this.syntaxTree);
	}

	private void mutateByRootGrowth() {
		Function function = this.context.getRandomNonTerminalFunction();
		Expression newRoot = new Expression(function);
		newRoot.addChild(this.syntaxTree);
		for (int i = 1; i < function.argumentsCount(); i++) {
			newRoot.addChild(SyntaxTreeUtils.createTree(0, this.context));
		}
		for (int i = 0; i < function.argumentsCount(); i++) {
			newRoot.addCoefficient(this.context.getRandomValue());
		}
		this.syntaxTree = newRoot;
	}

	private void mutateByRandomChangeOfFunction() {
		Expression mutatingNode = this.getRandomNode(this.syntaxTree);

		Function function = null;
		if (this.random.nextDouble() > 0.5) {
			function = this.context.getRandomNonTerminalFunction();
		} else {
			function = this.context.getRandomTerminalFunction();
		}

		mutatingNode.setFunction(function);

		if (function.isVariable()) {
			mutatingNode.setVariable(this.context.getRandomVariableName());
		}

		int functionArgumentsCount = function.argumentsCount();
		int mutatingNodeChildsCount = mutatingNode.getChilds().size();

		if (functionArgumentsCount > mutatingNodeChildsCount) {
			for (int i = 0; i < ((functionArgumentsCount - mutatingNodeChildsCount) + 1); i++) {
				mutatingNode.getChilds().add(SyntaxTreeUtils.createTree(1, this.context));
			}
		} else if (functionArgumentsCount < mutatingNodeChildsCount) {
			List<Expression> subList = new ArrayList<Expression>(functionArgumentsCount);
			for (int i = 0; i < functionArgumentsCount; i++) {
				subList.add(mutatingNode.getChilds().get(i));
			}
			mutatingNode.setChilds(subList);
		}

		int functionCoefficientsCount = function.coefficientsCount();
		int mutatingNodeCoefficientsCount = mutatingNode.getCoefficientsOfNode().size();
		if (functionCoefficientsCount > mutatingNodeCoefficientsCount) {
			for (int i = 0; i < ((functionCoefficientsCount - mutatingNodeCoefficientsCount) + 1); i++) {
				mutatingNode.addCoefficient(this.context.getRandomValue());
			}
		} else if (functionCoefficientsCount < mutatingNodeCoefficientsCount) {
			List<Double> subList = new ArrayList<Double>(functionCoefficientsCount);
			for (int i = 0; i < functionCoefficientsCount; i++) {
				subList.add(mutatingNode.getCoefficientsOfNode().get(i));
			}
			mutatingNode.setCoefficientsOfNode(subList);
		}
	}

	private void mutateByReverseOfChildsList() {
		Expression mutatingNode = this.getRandomNode(this.syntaxTree);
		Function mutatingNodeFunction = mutatingNode.getFunction();

		if ((mutatingNode.getChilds().size() > 1)
				&& (!mutatingNodeFunction.isCommutative())) {

			Collections.reverse(mutatingNode.getChilds());

		} else {
			this.mutateByRandomChangeOfFunction();
		}
	}

	private void mutateByRandomChangeOfChild() {
		Expression mutatingNode = this.getRandomNode(this.syntaxTree);

		if (!mutatingNode.getChilds().isEmpty()) {

			int indx = this.random.nextInt(mutatingNode.getChilds().size());

			mutatingNode.getChilds().set(indx, SyntaxTreeUtils.createTree(1, this.context));

		} else {
			this.mutateByRandomChangeOfFunction();
		}
	}

	private void mutateByRandomChangeOfNodeToChild() {
		Expression mutatingNode = this.getRandomNode(this.syntaxTree);

		if (!mutatingNode.getChilds().isEmpty()) {

			int indx = this.random.nextInt(mutatingNode.getChilds().size());

			Expression child = mutatingNode.getChilds().get(indx);

			this.swapNode(mutatingNode, child.clone());

		} else {
			this.mutateByRandomChangeOfFunction();
		}
	}

	private Expression getRandomNode(Expression tree) {
		List<Expression> allNodesOfTree = tree.getAllNodesAsList();
		int allNodesOfTreeCount = allNodesOfTree.size();
		int indx = this.random.nextInt(allNodesOfTreeCount);
		return allNodesOfTree.get(indx);
	}

	private void swapNode(Expression oldNode, Expression newNode) {
		oldNode.setChilds(newNode.getChilds());
		oldNode.setFunction(newNode.getFunction());
		oldNode.setCoefficientsOfNode(newNode.getCoefficientsOfNode());
		oldNode.setVariable(newNode.getVariable());
	}

	public void optimizeTree() {
		this.optimizeTree(70);
	}

	public void optimizeTree(int iterations) {

		SyntaxTreeUtils.cutTree(this.syntaxTree, this.context, 6);
		SyntaxTreeUtils.simplifyTree(this.syntaxTree, this.context);

		List<Double> coefficientsOfTree = this.syntaxTree.getCoefficientsOfTree();

		if (coefficientsOfTree.size() > 0) {
			CoefficientsChromosome initialChromosome = new CoefficientsChromosome(coefficientsOfTree, 0.6, 0.8);
			Population<CoefficientsChromosome> population = new Population<CoefficientsChromosome>();
			for (int i = 0; i < 5; i++) {
				population.addChromosome(initialChromosome.mutate());
			}
			population.addChromosome(initialChromosome);

			Fitness<CoefficientsChromosome, Double> fit = new CoefficientsFitness();

			Environment<CoefficientsChromosome, Double> env = new Environment<GpChromosome.CoefficientsChromosome, Double>(population, fit);

			env.iterate(iterations);

			List<Double> optimizedCoefficients = env.getBest().getCoefficients();

			this.syntaxTree.setCoefficientsOfTree(optimizedCoefficients);
		}
	}

	public Context getContext() {
		return this.context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Expression getSyntaxTree() {
		return this.syntaxTree;
	}

	private class CoefficientsChromosome implements Chromosome<CoefficientsChromosome>, Cloneable {

		private double pMutation;

		private double pCrossover;

		private List<Double> coefficients;

		public CoefficientsChromosome(List<Double> coefficients, double pMutation, double pCrossover) {
			this.coefficients = coefficients;
			this.pMutation = pMutation;
			this.pCrossover = pCrossover;
		}

		@Override
		public List<CoefficientsChromosome> crossover(CoefficientsChromosome anotherChromosome) {
			List<CoefficientsChromosome> ret = new ArrayList<GpChromosome.CoefficientsChromosome>(2);

			CoefficientsChromosome thisClone = this.clone();
			CoefficientsChromosome anotherClone = anotherChromosome.clone();

			for (int i = 0; i < thisClone.coefficients.size(); i++) {
				if (GpChromosome.this.random.nextDouble() > this.pCrossover) {
					thisClone.coefficients.set(i, anotherChromosome.coefficients.get(i));
					anotherClone.coefficients.set(i, this.coefficients.get(i));
				}
			}
			ret.add(thisClone);
			ret.add(anotherClone);

			return ret;
		}

		@Override
		public CoefficientsChromosome mutate() {
			CoefficientsChromosome ret = this.clone();
			for (int i = 0; i < ret.coefficients.size(); i++) {
				if (GpChromosome.this.random.nextDouble() > this.pMutation) {
					double coeff = ret.coefficients.get(i);
					coeff += GpChromosome.this.context.getRandomMutationValue();
					ret.coefficients.set(i, coeff);
				}
			}
			return ret;
		}

		@Override
		protected CoefficientsChromosome clone() {
			List<Double> ret = new ArrayList<Double>(this.coefficients.size());
			for (double d : this.coefficients) {
				ret.add(d);
			}
			return new CoefficientsChromosome(ret, this.pMutation, this.pCrossover);
		}

		public List<Double> getCoefficients() {
			return this.coefficients;
		}

	}

	private class CoefficientsFitness implements Fitness<CoefficientsChromosome, Double> {

		@Override
		public Double calculate(CoefficientsChromosome chromosome) {
			GpChromosome.this.syntaxTree.setCoefficientsOfTree(chromosome.getCoefficients());
			return GpChromosome.this.fitnessFunction.calculate(GpChromosome.this);
		}

	}

}