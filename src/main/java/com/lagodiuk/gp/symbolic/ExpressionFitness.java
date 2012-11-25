package com.lagodiuk.gp.symbolic;

import com.lagodiuk.gp.symbolic.interpreter.Context;
import com.lagodiuk.gp.symbolic.interpreter.Expression;

public interface ExpressionFitness {

	double fitness(Expression expression, Context context);

}
