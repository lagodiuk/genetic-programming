package com.lagodiuk.gp.symbolic;

import java.util.Collections;
import java.util.LinkedList;
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
		List<GpGene> ret = new LinkedList<GpGene>();

		GpGene thisClone = new GpGene(this.context, this.fitnessFunction, this.syntaxTree.clone());
		GpGene anotherClone = new GpGene(this.context, this.fitnessFunction, anotherGene.syntaxTree.clone());

		Expression thisRandomSubTree = this.getRandomSubTree(thisClone.syntaxTree);
		Expression anotherRandomSubTree = this.getRandomSubTree(anotherClone.syntaxTree);

		Expression thisRandomSubTreeClone = thisRandomSubTree.clone();
		Expression anotherRandomSubTreeClone = anotherRandomSubTree.clone();

		this.swapNode(thisRandomSubTree, anotherRandomSubTreeClone);
		this.swapNode(anotherRandomSubTree, thisRandomSubTreeClone);

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
			case 1:
				ret.mutateByRandomChangeOfChild();
			case 2:
				ret.mutateByShuffleOfChildsList();
			case 3:
				ret.mutateByRandomChangeOfNodeToChild();
		}

		ret.optimizeTree();
		return ret;
	}

	private void mutateByRandomChangeOfFunction() {
		Expression mutatingNode = this.getRandomSubTree(this.syntaxTree);

		Function function = null;
		if (Math.random() > 0.5) {
			function = this.context.getRandomFunction();
		} else {
			function = this.context.getRandomTerminalFunction();
		}

		mutatingNode.setFunction(function);

		if (function.isVariable()) {
			mutatingNode.setVariable(this.context.getRandomVariableName());
		}

		if (function.argumentsCount() > mutatingNode.getChilds().size()) {
			for (int i = 0; i < ((function.argumentsCount() - mutatingNode.getChilds().size()) + 1); i++) {
				mutatingNode.getChilds().add(SyntaxTreeUtils.createTree(0, this.context));
			}
		} else if (function.argumentsCount() < mutatingNode.getChilds().size()) {
			List<Expression> subList = new LinkedList<Expression>();
			for (int i = 0; i < function.argumentsCount(); i++) {
				subList.add(mutatingNode.getChilds().get(i));
			}
			mutatingNode.setChilds(subList);
		}

		if (function.coefficientsCount() > mutatingNode.getCoefficientsOfNode().size()) {
			for (int i = 0; i < ((function.coefficientsCount() - mutatingNode.getCoefficientsOfNode().size()) + 1); i++) {
				mutatingNode.addCoefficient(this.context.getRandomValue());
			}
		} else if (function.coefficientsCount() < mutatingNode.getCoefficientsOfNode().size()) {
			List<Double> subList = new LinkedList<Double>();
			for (int i = 0; i < function.coefficientsCount(); i++) {
				subList.add(mutatingNode.getCoefficientsOfNode().get(i));
			}
			mutatingNode.setCoefficientsOfNode(subList);
		}
	}

	private void mutateByShuffleOfChildsList() {
		Expression mutatingNode = this.getRandomSubTree(this.syntaxTree);

		if (mutatingNode.getChilds().size() > 1) {

			Collections.shuffle(mutatingNode.getChilds());

		} else {
			this.mutateByRandomChangeOfFunction();
		}
	}

	private void mutateByRandomChangeOfChild() {
		Expression mutatingNode = this.getRandomSubTree(this.syntaxTree);

		if (!mutatingNode.getChilds().isEmpty()) {

			int indx = this.random.nextInt(mutatingNode.getChilds().size());

			mutatingNode.getChilds().set(indx, SyntaxTreeUtils.createTree(1, this.context));

		} else {
			this.mutateByRandomChangeOfFunction();
		}
	}

	private void mutateByRandomChangeOfNodeToChild() {
		Expression mutatingNode = this.getRandomSubTree(this.syntaxTree);

		if (!mutatingNode.getChilds().isEmpty()) {

			int indx = this.random.nextInt(mutatingNode.getChilds().size());

			Expression child = mutatingNode.getChilds().get(indx);

			this.swapNode(mutatingNode, child);

		} else {
			this.mutateByRandomChangeOfFunction();
		}
	}

	private Expression getRandomSubTree(Expression tree) {
		List<Expression> allNodesOfTree = tree.getAllNodesAsList();
		int allNodesOfTreeCount = allNodesOfTree.size();

		if ((allNodesOfTreeCount >= 7) && (Math.random() > 0.7)) {
			allNodesOfTreeCount = allNodesOfTreeCount / 2;
		}

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
		this.optimizeTree(50);
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
			List<CoefficientsGene> ret = new LinkedList<GpGene.CoefficientsGene>();

			CoefficientsGene crossovered1 = this.clone();
			CoefficientsGene crossovered2 = anotherGene.clone();

			for (int i = 0; i < crossovered1.coefficients.size(); i++) {
				if (Math.random() > crossovered1.pCrossover) {
					crossovered1.coefficients.set(i, anotherGene.coefficients.get(i));
					crossovered2.coefficients.set(i, this.coefficients.get(i));
				}
			}
			ret.add(crossovered1);
			ret.add(crossovered2);

			return ret;
		}

		@Override
		public CoefficientsGene mutate() {
			CoefficientsGene ret = this.clone();
			for (int i = 0; i < ret.coefficients.size(); i++) {
				if (Math.random() > ret.pMutation) {
					double coeff = ret.coefficients.get(i);
					coeff += GpGene.this.context.getRandomMutationValue();
					ret.coefficients.set(i, coeff);
				}
			}
			return ret;
		}

		@Override
		protected CoefficientsGene clone() {
			List<Double> ret = new LinkedList<Double>();
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