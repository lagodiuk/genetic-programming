package com.lagodiuk.gp.math.interpreter;

import java.util.LinkedList;
import java.util.List;

public enum Functions implements Function {

	CONSTANT {
		private int coefficientsCount = 1;

		@Override
		public int argumentsCount() {
			return 0;
		}

		@Override
		public int coefficientsCount() {
			return this.coefficientsCount;
		}

		@Override
		public List<Double> getCoefficients(Expression expression) {
			return expression.getCoefficientsOfNode().subList(0, this.coefficientsCount);
		}

		@Override
		public void setCoefficients(Expression expression, List<Double> coefficients, int startIndex) {
			expression.removeCoefficients();
			for (int i = 0; i < this.coefficientsCount; i++) {
				expression.addCoefficient(coefficients.get(startIndex + i));
			}
		}

		@Override
		public boolean isVariable() {
			return false;
		}

		@Override
		public double eval(Expression expression, Context context) {
			return expression.getCoefficientsOfNode().get(0);
		}

		@Override
		public String print(Expression expression, Context context) {
			double retVal = expression.getCoefficientsOfNode().get(0);
			String retStr = null;
			if (retVal < 0) {
				retStr = "(" + retVal + ")";
			} else {
				retStr = "" + retVal;
			}
			return retStr;
		}

		@Override
		public boolean isNumber() {
			return true;
		}
	},
	VARIABLE {
		private int coefficientsCount = 0;

		@Override
		public int coefficientsCount() {
			return this.coefficientsCount;
		}

		@Override
		public List<Double> getCoefficients(Expression expression) {
			return new LinkedList<Double>();
		}

		@Override
		public void setCoefficients(Expression expression, List<Double> coefficients, int startIndex) {
			expression.removeCoefficients();
		}

		@Override
		public int argumentsCount() {
			return 0;
		}

		@Override
		public boolean isVariable() {
			return true;
		}

		@Override
		public boolean isNumber() {
			return false;
		}

		@Override
		public double eval(Expression expression, Context context) {
			return context.lookupVariable(expression.getVariable());
		}

		@Override
		public String print(Expression expression, Context context) {
			return expression.getVariable();
		}
	},
	ADD {
		private int coefficientsCount = 0;

		@Override
		public int coefficientsCount() {
			return this.coefficientsCount;
		}

		@Override
		public List<Double> getCoefficients(Expression expression) {
			return new LinkedList<Double>();
		}

		@Override
		public void setCoefficients(Expression expression, List<Double> coefficients, int startIndex) {
			expression.removeCoefficients();
		}

		@Override
		public int argumentsCount() {
			return 2;
		}

		@Override
		public boolean isVariable() {
			return false;
		}

		@Override
		public boolean isNumber() {
			return false;
		}

		@Override
		public double eval(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();
			double left = childs.get(0).eval(context);
			double right = childs.get(1).eval(context);
			return (left + right);
		}

		@Override
		public String print(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();
			String left = childs.get(0).print(context);
			String right = childs.get(1).print(context);
			return "( " + left + " + " + right + " )";
		}
	},
	SUB {
		private int coefficientsCount = 0;

		@Override
		public int coefficientsCount() {
			return this.coefficientsCount;
		}

		@Override
		public List<Double> getCoefficients(Expression expression) {
			return new LinkedList<Double>();
		}

		@Override
		public void setCoefficients(Expression expression, List<Double> coefficients, int startIndex) {
			expression.removeCoefficients();
		}

		@Override
		public int argumentsCount() {
			return 2;
		}

		@Override
		public boolean isVariable() {
			return false;
		}

		@Override
		public boolean isNumber() {
			return false;
		}

		@Override
		public double eval(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();
			double left = childs.get(0).eval(context);
			double right = childs.get(1).eval(context);
			return (left - right);
		}

		@Override
		public String print(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();
			String left = childs.get(0).print(context);
			String right = childs.get(1).print(context);

			return "( " + left + " - " + right + " )";
		}
	},
	MUL {
		private int coefficientsCount = 0;

		@Override
		public int coefficientsCount() {
			return this.coefficientsCount;
		}

		@Override
		public List<Double> getCoefficients(Expression expression) {
			return new LinkedList<Double>();
		}

		@Override
		public void setCoefficients(Expression expression, List<Double> coefficients, int startIndex) {
			expression.removeCoefficients();
		}

		@Override
		public int argumentsCount() {
			return 2;
		}

		@Override
		public boolean isVariable() {
			return false;
		}

		@Override
		public boolean isNumber() {
			return false;
		}

		@Override
		public double eval(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();
			double left = childs.get(0).eval(context);
			double right = childs.get(1).eval(context);
			return (left * right);
		}

		@Override
		public String print(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();
			String left = childs.get(0).print(context);
			String right = childs.get(1).print(context);

			return "( " + left + " * " + right + " )";
		}
	},
	DIV {
		private int coefficientsCount = 0;

		@Override
		public int coefficientsCount() {
			return this.coefficientsCount;
		}

		@Override
		public List<Double> getCoefficients(Expression expression) {
			return new LinkedList<Double>();
		}

		@Override
		public void setCoefficients(Expression expression, List<Double> coefficients, int startIndex) {
			expression.removeCoefficients();
		}

		@Override
		public int argumentsCount() {
			return 2;
		}

		@Override
		public boolean isVariable() {
			return false;
		}

		@Override
		public boolean isNumber() {
			return false;
		}

		@Override
		public double eval(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();
			double left = childs.get(0).eval(context);
			double right = childs.get(1).eval(context);
			return (left / right);
		}

		@Override
		public String print(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();
			String left = childs.get(0).print(context);
			String right = childs.get(1).print(context);

			return "( " + left + " / " + right + " )";
		}
	},
	SQRT {
		private int coefficientsCount = 0;

		@Override
		public int coefficientsCount() {
			return this.coefficientsCount;
		}

		@Override
		public List<Double> getCoefficients(Expression expression) {
			return new LinkedList<Double>();
		}

		@Override
		public void setCoefficients(Expression expression, List<Double> coefficients, int startIndex) {
			expression.removeCoefficients();
		}

		@Override
		public int argumentsCount() {
			return 1;
		}

		@Override
		public boolean isVariable() {
			return false;
		}

		@Override
		public boolean isNumber() {
			return false;
		}

		@Override
		public double eval(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();
			double arg = childs.get(0).eval(context);
			return Math.sqrt(Math.abs(arg));
		}

		@Override
		public String print(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();

			String arg = childs.get(0).print(context);

			return "sqrt( abs( " + arg + " ) )";
		}
	},
	POW {
		private int coefficientsCount = 0;

		@Override
		public int coefficientsCount() {
			return this.coefficientsCount;
		}

		@Override
		public List<Double> getCoefficients(Expression expression) {
			return new LinkedList<Double>();
		}

		@Override
		public void setCoefficients(Expression expression, List<Double> coefficients, int startIndex) {
			expression.removeCoefficients();
		}

		@Override
		public int argumentsCount() {
			return 2;
		}

		@Override
		public boolean isVariable() {
			return false;
		}

		@Override
		public boolean isNumber() {
			return false;
		}

		@Override
		public double eval(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();
			double arg1 = childs.get(0).eval(context);
			double arg2 = childs.get(1).eval(context);
			return Math.pow(arg1, arg2);
		}

		@Override
		public String print(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();

			String arg1 = childs.get(0).print(context);
			String arg2 = childs.get(1).print(context);

			return "( " + arg1 + " ) ^ ( " + arg2 + " )";
		}
	},
	SIN {
		private int coefficientsCount = 0;

		@Override
		public int coefficientsCount() {
			return this.coefficientsCount;
		}

		@Override
		public List<Double> getCoefficients(Expression expression) {
			return new LinkedList<Double>();
		}

		@Override
		public void setCoefficients(Expression expression, List<Double> coefficients, int startIndex) {
			expression.removeCoefficients();
		}

		@Override
		public int argumentsCount() {
			return 1;
		}

		@Override
		public boolean isVariable() {
			return false;
		}

		@Override
		public boolean isNumber() {
			return false;
		}

		@Override
		public double eval(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();
			double arg = childs.get(0).eval(context);
			return Math.sin(arg);
		}

		@Override
		public String print(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();

			String arg = childs.get(0).print(context);

			return "sin( " + arg + " )";
		}
	},
	COS {
		private int coefficientsCount = 0;

		@Override
		public int coefficientsCount() {
			return this.coefficientsCount;
		}

		@Override
		public List<Double> getCoefficients(Expression expression) {
			return new LinkedList<Double>();
		}

		@Override
		public void setCoefficients(Expression expression, List<Double> coefficients, int startIndex) {
			expression.removeCoefficients();
		}

		@Override
		public int argumentsCount() {
			return 1;
		}

		@Override
		public boolean isVariable() {
			return false;
		}

		@Override
		public boolean isNumber() {
			return false;
		}

		@Override
		public double eval(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();
			double arg = childs.get(0).eval(context);
			return Math.cos(arg);
		}

		@Override
		public String print(Expression expression, Context context) {
			List<Expression> childs = expression.getChilds();

			String arg = childs.get(0).print(context);

			return "cos( " + arg + " )";
		}
	},

}