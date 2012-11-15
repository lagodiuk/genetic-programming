package com.lagodiuk.gp.math.interpreter;

import java.util.List;

public interface Function {

	double eval(Expression expression, Context context);

	int argumentsCount();

	boolean isVariable();

	boolean isNumber();

	String print(Expression expression, Context context);

	List<Double> getCoefficients(Expression expression);

	void setCoefficients(Expression expression, List<Double> coefficients, int startIndex);

	int coefficientsCount();

}
