package com.lagodiuk.gp.math;

import com.lagodiuk.ga.Fitness;

public class GpFitness implements Fitness<GpGene, Double> {

	@Override
	public Double calculate(GpGene gene) {
		double delt = 0;
		for (int i = -5; i < 20; i++) {

			gene.getContext().setVariable("x", i);

			// double target = (i * i * i * 5) + i + 10;
			double target = ((((3 * i * i * i) - (i * i * 7)) + (i * 10)) - 35) * i;
			// double target = i * i;
			// double target = ( i + 100 ) * ( i - 100 );
			// double target = i + 6;

			double x = target - gene.getSyntaxTree().eval(gene.getContext());

			delt += x * x;
		}
		return delt;
	}

}