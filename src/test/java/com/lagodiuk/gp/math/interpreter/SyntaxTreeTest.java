package com.lagodiuk.gp.math.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class SyntaxTreeTest {

	@Test
	public void testTerminal() {
		Context context = this.createContext(Functions.values());

		Expression const7 = this.constantExpr(7);
		assertEquals("7.0", const7.print(context));

		assertTrue(Double.compare(7, const7.eval(context)) == 0);

		Expression varX = this.variableExpr("x");
		assertEquals("x", varX.print(context));

		for (int x = -10; x < 10; x++) {
			context.setVariable("x", x);
			assertTrue(Double.compare(x, varX.eval(context)) == 0);
		}

		Expression varY = this.variableExpr("y");
		assertEquals("y", varY.print(context));

		for (int y = -10; y < 10; y++) {
			context.setVariable("y", y);
			assertTrue(Double.compare(y, varY.eval(context)) == 0);
		}
	}

	@Test
	public void testComplex() {
		Context context = this.createContext(Functions.values());

		Expression const7 = this.constantExpr(7);

		Expression varX = this.variableExpr("x");

		Expression add = this.addExpr(varX, const7);
		assertEquals("( x + 7.0 )", add.print(context));

		for (int x = -10; x < 10; x++) {
			context.setVariable("x", x);
			assertTrue(Double.compare(x + 7, add.eval(context)) == 0);
		}

	}

	@Test
	public void testComplex2() {
		Context context = this.createContext(Functions.values());

		Expression varX = this.variableExpr("x");
		Expression varY = this.variableExpr("y");
		Expression varZ = this.variableExpr("z");

		Expression add = this.addExpr(varX, varY);
		Expression sub = this.subExpr(varZ, add);

		assertEquals("( z - ( x + y ) )", sub.print(context));

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
		Context context = this.createContext(Functions.values());

		Expression varX = new Expression(Functions.VARIABLE).setVariable("x");
		Expression const5 = new Expression(Functions.CONSTANT).setCoefficientsOfNode(this.listFromArray(5.0));
		Expression original = this.addExpr(const5, varX);
		Expression clone = original.clone();

		assertTrue(original.print(context).equals(clone.print(context)));

		for (int x = -10; x < 10; x++) {
			context.setVariable("x", x);
			assertTrue(Double.compare(original.eval(context), clone.eval(context)) == 0);
		}
	}

	@Test
	public void testCoefficientsOfTree() {
		Context context = this.createContext(Functions.values());

		Expression varX = this.variableExpr("x");
		Expression const1 = this.constantExpr(1);
		Expression const2 = this.constantExpr(2);
		Expression const3 = this.constantExpr(3);

		Expression complexExpr = this.subExpr(const3, this.subExpr(this.addExpr(varX, const1), const2));
		assertEquals("( 3.0 - ( ( x + 1.0 ) - 2.0 ) )", complexExpr.print(context));

		assertEquals(this.listFromArray(3.0, 1.0, 2.0), complexExpr.getCoefficientsOfTree());

		complexExpr.setCoefficientsOfTree(this.listFromArray(30.0, 10.0, 20.0));
		assertEquals("( 30.0 - ( ( x + 10.0 ) - 20.0 ) )", complexExpr.print(context));
	}

	@Test
	public void testAllNodesAsList() {
		Context context = this.createContext(Functions.values());

		Expression varX = this.variableExpr("x");
		Expression const1 = this.constantExpr(1);
		Expression const2 = this.constantExpr(2);
		Expression const3 = this.constantExpr(3);
		Expression add = this.addExpr(varX, const1);
		Expression sub = this.subExpr(add, const2);
		Expression complexExpr = this.subExpr(const3, sub);

		assertEquals("( 3.0 - ( ( x + 1.0 ) - 2.0 ) )", complexExpr.print(context));

		assertEquals(
				this.listFromArray(complexExpr, const3, sub, add, const2, varX, const1), 
				complexExpr.getAllNodesAsList());
	}

	private Context createContext(Function... functions) {
		return new Context(this.listFromArray(functions));
	}

	private Expression variableExpr(String x) {
		return new Expression(Functions.VARIABLE).setVariable(x);
	}

	private Expression constantExpr(double value) {
		return new Expression(Functions.CONSTANT).setCoefficientsOfNode(this.listFromArray(value));
	}

	private Expression subExpr(Expression left, Expression right) {
		return new Expression(Functions.SUB).setChilds(this.listFromArray(left, right));
	}

	private Expression addExpr(Expression left, Expression right) {
		return new Expression(Functions.ADD).setChilds(this.listFromArray(left, right));
	}

	private <T> List<T> listFromArray(T... elements) {
		List<T> list = new LinkedList<T>();
		for (T elem : elements) {
			list.add(elem);
		}
		return list;
	}
}
