package com.lagodiuk.gp.symbolic.interpreter;

import java.util.List;

public class SyntaxTreeUtils {

	public static Expression createTree(int depth, Context context) {
		if (depth > 0) {

			Function f = context.getRandomFunction();
			Expression expr = new Expression(f);

			if (f.argumentsCount() > 0) {

				for (int i = 0; i < f.argumentsCount(); i++) {
					Expression child = createTree(depth - 1, context);
					expr.addChild(child);
				}

			} else {

				if (f.isVariable()) {

					String varName = context.getRandomVariableName();
					expr.setVariable(varName);

				}

			}

			for (int i = 0; i < f.coefficientsCount(); i++) {
				expr.addCoefficient(context.getRandomValue());
			}

			return expr;

		} else {

			Function f = context.getRandomTerminalFunction();
			Expression expr = new Expression(f);

			if (f.isVariable()) {

				String varName = context.getRandomVariableName();
				expr.setVariable(varName);

			}

			for (int i = 0; i < f.coefficientsCount(); i++) {
				expr.addCoefficient(context.getRandomValue());
			}

			return expr;

		}
	}

	public static void simplifyTree(Expression tree, Context context) {
		if (hasVariableNode(tree)) {
			for (Expression child : tree.getChilds()) {
				simplifyTree(child, context);
			}
		} else {
			double value = tree.eval(context);
			tree.addCoefficient(value);
			tree.removeChilds();
			List<Function> terminalFunctions = context.getTerminalFunctions();
			for (Function f : terminalFunctions) {
				if (f.isNumber()) {
					tree.setFunction(f);
					break;
				}
			}
		}
	}

	public static void cutTree(Expression tree, Context context, int depth) {
		if (depth > 0) {
			for (Expression child : tree.getChilds()) {
				cutTree(child, context, depth - 1);
			}
		} else {
			int childsCount = tree.getChilds().size();
			tree.removeChilds();
			for (int i = 0; i < childsCount; i++) {
				tree.addChild(createTree(0, context));
			}
		}
	}

	public static boolean hasVariableNode(Expression tree) {
		boolean ret = false;

		if (tree.getFunction().isVariable()) {
			ret = true;
		} else {
			for (Expression child : tree.getChilds()) {
				ret = hasVariableNode(child);
				if (ret) {
					break;
				}
			}
		}

		return ret;
	}

}
