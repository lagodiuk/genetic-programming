package com.lagodiuk.gp.symbolic.interpreter;

import static com.lagodiuk.gp.symbolic.interpreter.TestUtils.addExpr;
import static com.lagodiuk.gp.symbolic.interpreter.TestUtils.constantExpr;
import static com.lagodiuk.gp.symbolic.interpreter.TestUtils.createContext;
import static com.lagodiuk.gp.symbolic.interpreter.TestUtils.powExpr;
import static com.lagodiuk.gp.symbolic.interpreter.TestUtils.subExpr;
import static com.lagodiuk.gp.symbolic.interpreter.TestUtils.variableExpr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class SyntaxTreeUtilsTest {

	@Test
	public void simplifyTreeTest1() {
		Context context = createContext(Functions.values());

		Expression varX = variableExpr("x");
		Expression const1 = constantExpr(1);
		Expression const2 = constantExpr(2);
		Expression const3 = constantExpr(3);

		Expression complexExpr = addExpr(varX, subExpr(const3, addExpr(const1, const2)));
		assertEquals("(x + (3.0 - (1.0 + 2.0)))", complexExpr.print());
		assertTrue(calculateDepth(complexExpr) == 3);

		Expression simplifiedExpr = complexExpr.clone();
		SyntaxTreeUtils.simplifyTree(simplifiedExpr, context);
		assertEquals("(x + 0.0)", simplifiedExpr.print());
		assertTrue(calculateDepth(simplifiedExpr) == 1);
	}

	@Test
	public void simplifyTreeTest2() {
		Context context = createContext(Functions.values());

		Expression varX = variableExpr("x");
		Expression const1 = constantExpr(1);
		Expression const2 = constantExpr(2);
		Expression const3 = constantExpr(3);
		Expression const4 = constantExpr(4);
		Expression const5 = constantExpr(5);
		Expression const6 = constantExpr(6);

		Expression complexExpr = addExpr(addExpr(addExpr(const4, const6), addExpr(const5, varX)), subExpr(const3, addExpr(const1, const2)));
		assertEquals("(((4.0 + 6.0) + (5.0 + x)) + (3.0 - (1.0 + 2.0)))", complexExpr.print());

		Expression simplifiedExpr = complexExpr.clone();
		SyntaxTreeUtils.simplifyTree(simplifiedExpr, context);
		assertEquals("((10.0 + (5.0 + x)) + 0.0)", simplifiedExpr.print());
	}

	@Test
	public void simplifyTreeTest3() {
		Context context = createContext(Functions.values());
		Expression powExpr = powExpr(constantExpr(2), constantExpr(3));
		assertEquals("(2.0 ^ 3.0)", powExpr.print());
		SyntaxTreeUtils.simplifyTree(powExpr, context);
		assertEquals("8.0", powExpr.print());
	}

	@Test
	public void cutTreeTest() {
		Context context = createContext(Functions.values());

		Expression varX = variableExpr("x");
		Expression const1 = constantExpr(1);
		Expression const2 = constantExpr(2);
		Expression const3 = constantExpr(3);
		Expression const4 = constantExpr(4);

		Expression complexExpr = addExpr(varX, subExpr(const3, addExpr(const1, subExpr(const4, const2))));
		assertEquals("(x + (3.0 - (1.0 + (4.0 - 2.0))))", complexExpr.print());
		assertTrue(calculateDepth(complexExpr) == 4);

		Expression cutExpr = complexExpr.clone();
		SyntaxTreeUtils.cutTree(cutExpr, context, 1);
		assertTrue(calculateDepth(cutExpr) == 1);
	}

	@Test
	public void generateRandomTreeTest() {
		Context context = createContext(Functions.values());
		context.setVariable("x", 0);
		context.setVariable("y", 0);

		for (int maxDepth = 0; maxDepth < 7; maxDepth++) {
			Set<Integer> depths = new HashSet<Integer>();
			for (int i = 0; i < 100; i++) {
				Expression tree = SyntaxTreeUtils.createTree(maxDepth, context);
				int currDepth = calculateDepth(tree);
				depths.add(currDepth);
			}

			Set<Integer> target = new HashSet<Integer>();
			for (int i = 0; i < (maxDepth + 1); i++) {
				target.add(i);
			}

			for (Integer depth : depths) {
				assertTrue(target.contains(depth));
			}
		}
	}

	private static int calculateDepth(Expression root) {
		return calculateNodeLevels(root) - 1;
	}

	private static int calculateNodeLevels(Expression root) {
		int depth = 0;
		if (!root.getChilds().isEmpty()) {
			for (Expression child : root.getChilds()) {
				depth = Math.max(depth, calculateNodeLevels(child));
			}
		}
		return depth + 1;
	}

}
