package com.lagodiuk.gp.math.interpreter;

import java.util.LinkedList;
import java.util.List;

public class Test {

	public static void main(String[] args) {

		List<Function> functions = new LinkedList<Function>();
		for (Function f : Functions.values()) {
			functions.add(f);
		}

		Context context = new Context(functions);

		context.setVariable("x", 10);

		Expression expr = SyntaxTreeUtils.createTree(5, context);

		System.out.println(expr.print(context));
		System.out.println(expr.eval(context));
		System.out.println(expr.getCoefficientsOfTree());

		System.out.println("=====");

		List<Double> coefficients = expr.getCoefficientsOfTree();
		System.out.println(coefficients);
		if (coefficients.size() > 0) {
			coefficients.set(0, 0.0);
		}
		System.out.println(coefficients);
		expr.setCoefficientsOfTree(coefficients);
		System.out.println(expr.print(context));
		System.out.println(expr.eval(context));
		System.out.println(expr.getCoefficientsOfTree());

	}

}