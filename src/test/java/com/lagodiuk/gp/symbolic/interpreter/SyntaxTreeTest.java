package com.lagodiuk.gp.symbolic.interpreter;

import static com.lagodiuk.gp.symbolic.interpreter.TestUtils.addExpr;
import static com.lagodiuk.gp.symbolic.interpreter.TestUtils.constantExpr;
import static com.lagodiuk.gp.symbolic.interpreter.TestUtils.createContext;
import static com.lagodiuk.gp.symbolic.interpreter.TestUtils.list;
import static com.lagodiuk.gp.symbolic.interpreter.TestUtils.subExpr;
import static com.lagodiuk.gp.symbolic.interpreter.TestUtils.variableExpr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class SyntaxTreeTest {

	@Test
	public void testTerminal() {
		Context context = createContext(Functions.values());

		Expression const7 = constantExpr(7);
		assertEquals("7.0", const7.print());

		assertTrue(Double.compare(7, const7.eval(context)) == 0);

		Expression varX = variableExpr("x");
		assertEquals("x", varX.print());

		for (int x = -10; x < 10; x++) {
			context.setVariable("x", x);
			assertTrue(Double.compare(x, varX.eval(context)) == 0);
		}

		Expression varY = variableExpr("y");
		assertEquals("y", varY.print());

		for (int y = -10; y < 10; y++) {
			context.setVariable("y", y);
			assertTrue(Double.compare(y, varY.eval(context)) == 0);
		}
	}

	@Test
	public void testComplex() {
		Context context = createContext(Functions.values());

		Expression const7 = constantExpr(7);

		Expression varX = variableExpr("x");

		Expression add = addExpr(varX, const7);
		assertEquals("(x + 7.0)", add.print());

		for (int x = -10; x < 10; x++) {
			context.setVariable("x", x);
			assertTrue(Double.compare(x + 7, add.eval(context)) == 0);
		}

	}

	@Test
	public void testComplex2() {
		Context context = createContext(Functions.values());

		Expression varX = variableExpr("x");
		Expression varY = variableExpr("y");
		Expression varZ = variableExpr("z");

		Expression add = addExpr(varX, varY);
		Expression sub = subExpr(varZ, add);

		assertEquals("(z - (x + y))", sub.print());

		for (int x = -10; x < 10; x++) {
			for (int y = -10; y < 10; y++) {
				for (int z = -10; z < 10; z++) {
					context.setVariable("x", x);
					context.setVariable("y", y);
					context.setVariable("z", z);
					assertTrue(Double.compare((z - (x + y)), sub.eval(context)) == 0);
				}
			}
		}
	}

	@Test
	public void testClone() {
		Context context = createContext(Functions.values());

		Expression varX = new Expression(Functions.VARIABLE).setVariable("x");
		Expression const5 = new Expression(Functions.CONSTANT).setCoefficientsOfNode(list(5.0));
		Expression original = addExpr(const5, varX);
		Expression clone = original.clone();

		assertTrue(original.print().equals(clone.print()));

		for (int x = -10; x < 10; x++) {
			context.setVariable("x", x);
			assertTrue(Double.compare(original.eval(context), clone.eval(context)) == 0);
		}
	}

	@Test
	public void testCoefficientsOfTree() {
		Expression varX = variableExpr("x");
		Expression const1 = constantExpr(1);
		Expression const2 = constantExpr(2);
		Expression const3 = constantExpr(3);

		Expression complexExpr = subExpr(const3, subExpr(addExpr(varX, const1), const2));
		assertEquals("(3.0 - ((x + 1.0) - 2.0))", complexExpr.print());

		assertEquals(list(3.0, 1.0, 2.0), complexExpr.getCoefficientsOfTree());

		complexExpr.setCoefficientsOfTree(list(30.0, 10.0, 20.0));
		assertEquals("(30.0 - ((x + 10.0) - 20.0))", complexExpr.print());
	}

	@Test
	public void testCoefficientsOfTreeFunc() {
		Function linearFunc = new LinearFunc();

		Expression varX = variableExpr("x");

		Expression linear = new Expression(linearFunc).setCoefficientsOfNode(list(9.0, 7.0)).setChilds(list(varX));
		assertEquals("(9.0*x + 7.0)", linear.print());

		Expression const1 = constantExpr(1);
		Expression const2 = constantExpr(2);
		Expression const3 = constantExpr(3);

		Expression complexExpr = subExpr(const3, subExpr(addExpr(linear, const1), const2));
		assertEquals("(3.0 - (((9.0*x + 7.0) + 1.0) - 2.0))", complexExpr.print());

		assertEquals(list(3.0, 9.0, 7.0, 1.0, 2.0), complexExpr.getCoefficientsOfTree());

		complexExpr.setCoefficientsOfTree(list(30.0, 90.0, 70.0, 10.0, 20.0));
		assertEquals("(30.0 - (((90.0*x + 70.0) + 10.0) - 20.0))", complexExpr.print());
	}

	@Test
	public void testAllNodesAsList() {
		Expression varX = variableExpr("x");
		Expression const1 = constantExpr(1);
		Expression const2 = constantExpr(2);
		Expression const3 = constantExpr(3);
		Expression add = addExpr(varX, const1);
		Expression sub = subExpr(add, const2);
		Expression complexExpr = subExpr(const3, sub);

		assertEquals("(3.0 - ((x + 1.0) - 2.0))", complexExpr.print());

		assertEquals(list(complexExpr, const3, sub, add, const2, varX, const1), complexExpr.getAllNodesAsList());
	}

	/**
	 * F(x) = k*x + b <br/>
	 * Coefficients, which can be optimized, are [k, b]
	 */
	private static class LinearFunc implements Function {

		@Override
		public double eval(Expression expression, Context context) {
			Expression child = expression.getChilds().get(0);
			double k = expression.getCoefficientsOfNode().get(0);
			double b = expression.getCoefficientsOfNode().get(1);
			return (k * child.eval(context)) + b;
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
		public boolean isCommutative() {
			return true;
		}

		@Override
		public String print(Expression expression) {
			Expression child = expression.getChilds().get(0);
			double k = expression.getCoefficientsOfNode().get(0);
			double b = expression.getCoefficientsOfNode().get(1);
			return String.format("(%s*%s + %s)", k, child.print(), b);
		}

		@Override
		public List<Double> getCoefficients(Expression expression) {
			return expression.getCoefficientsOfNode().subList(0, 2);
		}

		@Override
		public void setCoefficients(Expression expression, List<Double> coefficients, int startIndex) {
			expression.removeCoefficients();
			expression.addCoefficient(coefficients.get(startIndex));
			expression.addCoefficient(coefficients.get(startIndex + 1));
		}

		@Override
		public int coefficientsCount() {
			return 2;
		}
	}
}
