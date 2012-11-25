package com.lagodiuk.gp.symbolic.interpreter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;
import com.lagodiuk.gp.symbolic.interpreter.Function;
import com.lagodiuk.gp.symbolic.interpreter.Functions;

public class TestUtils {

	public static Context createContext(Function... functions) {
		return new Context(listFromArray(functions));
	}

	public static Expression variableExpr(String x) {
		return new Expression(Functions.VARIABLE).setVariable(x);
	}

	public static Expression constantExpr(double value) {
		return new Expression(Functions.CONSTANT).setCoefficientsOfNode(listFromArray(value));
	}

	public static Expression subExpr(Expression left, Expression right) {
		return new Expression(Functions.SUB).setChilds(listFromArray(left, right));
	}

	public static Expression addExpr(Expression left, Expression right) {
		return new Expression(Functions.ADD).setChilds(listFromArray(left, right));
	}

	public static <T> List<T> listFromArray(T... elements) {
		List<T> list = new LinkedList<T>();
		for (T elem : elements) {
			list.add(elem);
		}
		return list;
	}

	public static <T> Set<T> setFromArray(T... elements) {
		Set<T> list = new HashSet<T>();
		for (T elem : elements) {
			list.add(elem);
		}
		return list;
	}

}
