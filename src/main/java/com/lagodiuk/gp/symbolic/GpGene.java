package com.lagodiuk.gp.symbolic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.lagodiuk.ga.Environment;
import com.lagodiuk.ga.Fitness;
import com.lagodiuk.ga.Gene;
import com.lagodiuk.ga.Population;
import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;
import com.lagodiuk.gp.symbolic.interpreter.Function;
import com.lagodiuk.gp.symbolic.interpreter.SyntaxTreeUtils;

class GpGene implements Gene<GpGene> {

	private Expression syntaxTree;

	private Context context;

	private Fitness<GpGene, Double> fitnessFunction;

	private Random random = new Random();

	public GpGene(Context context, Fitness<GpGene, Double> fitnessFunction, Expression syntaxTree) {
		this.context = context;
		this.fitnessFunction = fitnessFunction;
		this.syntaxTree = syntaxTree;
	}

	@Override
	public List<GpGene> crossover(GpGene anotherGene) {
		List<GpGene> ret = new ArrayList<GpGene>(2);

		GpGene thisClone = new GpGene(this.context, this.fitnessFunction, this.syntaxTree.clone());
		GpGene anotherClone = new GpGene(this.context, this.fitnessFunction, anotherGene.syntaxTree.clone());

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
	public GpGene mutate() {
		GpGene ret = new GpGene(this.context, this.fitnessFunction, this.syntaxTree.clone());

		int type = this.random.nextInt(4);
		switch (type) {
			case 0:
				ret.mutateByRandomChangeOfFunction();
				break;
			case 1:
				ret.mutateByRandomChangeOfChild();
				break;
			case 2:
				ret.mutateByReverseOfChildsList();
				break;
			case 3:
				ret.mutateByRandomChangeOfNodeToChild();
				break;
		}

		ret.optimizeTree();
		return ret;
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
			CoefficientsGene initialGene = new CoefficientsGene(coefficientsOfTree, 0.6, 0.8);
			Population<CoefficientsGene> population = new Population<CoefficientsGene>();
			for (int i = 0; i < 5; i++) {
				population.addGene(initialGene.mutate());
			}
			population.addGene(initialGene);

			Fitness<CoefficientsGene, Double> fit = new CoefficientsFitness();

			Environment<CoefficientsGene, Double> env = new Environment<GpGene.CoefficientsGene, Double>(population, fit);

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

	private class CoefficientsGene implements Gene<CoefficientsGene>, Cloneable {

		private double pMutation;

		private double pCrossover;

		private List<Double> coefficients;

		public CoefficientsGene(List<Double> coefficients, double pMutation, double pCrossover) {
			this.coefficients = coefficients;
			this.pMutation = pMutation;
			this.pCrossover = pCrossover;
		}

		@Override
		public List<CoefficientsGene> crossover(CoefficientsGene anotherGene) {
			List<CoefficientsGene> ret = new ArrayList<GpGene.CoefficientsGene>(2);

			CoefficientsGene thisClone = this.clone();
			CoefficientsGene anotherClone = anotherGene.clone();

			for (int i = 0; i < thisClone.coefficients.size(); i++) {
				if (GpGene.this.random.nextDouble() > this.pCrossover) {
					thisClone.coefficients.set(i, anotherGene.coefficients.get(i));
					anotherClone.coefficients.set(i, this.coefficients.get(i));
				}
			}
			ret.add(thisClone);
			ret.add(anotherClone);

			return ret;
		}

		@Override
		public CoefficientsGene mutate() {
			CoefficientsGene ret = this.clone();
			for (int i = 0; i < ret.coefficients.size(); i++) {
				if (GpGene.this.random.nextDouble() > this.pMutation) {
					double coeff = ret.coefficients.get(i);
					coeff += GpGene.this.context.getRandomMutationValue();
					ret.coefficients.set(i, coeff);
				}
			}
			return ret;
		}

		@Override
		protected CoefficientsGene clone() {
			List<Double> ret = new ArrayList<Double>(this.coefficients.size());
			for (double d : this.coefficients) {
				ret.add(d);
			}
			return new CoefficientsGene(ret, this.pMutation, this.pCrossover);
		}

		public List<Double> getCoefficients() {
			return this.coefficients;
		}

	}

	private class CoefficientsFitness implements Fitness<CoefficientsGene, Double> {

		@Override
		public Double calculate(CoefficientsGene gene) {
			GpGene.this.syntaxTree.setCoefficientsOfTree(gene.getCoefficients());
			return GpGene.this.fitnessFunction.calculate(GpGene.this);
		}

	}

}