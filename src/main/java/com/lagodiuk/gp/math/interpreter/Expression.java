package com.lagodiuk.gp.math.interpreter;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Expression implements Cloneable {

	private List<Expression> childs = new LinkedList<Expression>();

	private List<Double> coefficients = new LinkedList<Double>();

	private String variable;

	private Function function;

	public Expression(Function function) {
		this.function = function;
	}

	public double eval(Context context) {
		return this.function.eval(this, context);
	}

	public String print(Context context) {
		return this.function.print(this, context);
	}

	public List<Expression> getChilds() {
		return this.childs;
	}

	public void setChilds(List<Expression> childs) {
		this.childs = childs;
	}

	public void addChild(Expression child) {
		this.childs.add(child);
	}

	public void removeChilds() {
		this.childs.clear();
	}

	public List<Double> getCoefficients() {
		return this.coefficients;
	}

	public void setCoefficients(List<Double> coefficients) {
		this.coefficients = coefficients;
	}

	public void addCoefficient(double coefficient) {
		this.coefficients.add(coefficient);
	}

	public void removeCoefficients() {
		if (this.coefficients.size() > 0) {
			this.coefficients.clear();
		}
	}

	public String getVariable() {
		return this.variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public Function getFunction() {
		return this.function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	@Override
	public Expression clone() {
		Expression cloned = new Expression(this.function);
		if (this.variable != null) {
			cloned.variable = new String(this.variable);
		}
		for (Expression c : this.childs) {
			cloned.childs.add(c.clone());
		}
		for (Double d : this.coefficients) {
			cloned.coefficients.add(d);
		}
		return cloned;
	}

	public List<Double> getCoefficientsOfTree() {
		LinkedList<Double> coefficients = new LinkedList<Double>();
		this.getCoefficientsOfTree(coefficients);
		Collections.reverse(coefficients);
		return coefficients;
	}

	private void getCoefficientsOfTree(Deque<Double> coefficients) {
		List<Double> coeffs = this.function.getCoefficients(this);
		for (Double d : coeffs) {
			coefficients.push(d);
		}
		for (int i = 0; i < this.childs.size(); i++) {
			this.childs.get(i).getCoefficientsOfTree(coefficients);
		}
	}

	public void setCoefficientsOfTree(List<Double> coefficients) {
		this.setCoefficientsOfTree(coefficients, 0);
	}

	private int setCoefficientsOfTree(List<Double> coefficients, int index) {
		this.function.setCoefficients(this, coefficients, index);
		index += this.function.coefficientsCount();
		if (this.childs.size() > 0) {
			for (int i = 0; i < this.childs.size(); i++) {
				index = this.childs.get(i).setCoefficientsOfTree(coefficients, index);
			}
		}
		return index;
	}

	public List<Expression> getAllNodesAsList() {
		List<Expression> nodes = new LinkedList<Expression>();
		// this.getAllNodesTopologicallySorted( nodes );
		this.getAllNodesWidthSearch(nodes);
		return nodes;
	}

	// private void getAllNodesTopologicallySorted( List<Expression> nodesList )
	// {
	// nodesList.add( this );
	// for ( Expression child : this.childs ) {
	// child.getAllNodesTopologicallySorted( nodesList );
	// }
	// }

	private void getAllNodesWidthSearch(List<Expression> nodesList) {
		int indx = 0;
		nodesList.add(this);
		while (true) {
			if (indx < nodesList.size()) {
				Expression node = nodesList.get(indx++);
				for (Expression child : node.childs) {
					nodesList.add(child);
				}
			} else {
				break;
			}
		}
	}

}