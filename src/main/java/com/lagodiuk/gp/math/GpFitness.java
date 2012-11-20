package com.lagodiuk.gp.math;

import com.lagodiuk.ga.Fitness;

public class GpFitness implements Fitness<GpGene, Double> {

	@Override
	public Double calculate(GpGene gene) {
		double delt = 0;
		for (int i = -20; i < 20; i++) {

			gene.getContext().setVariable("x", i);

			// double target = (i * i * i * 5) + i + 10;
			double target = ((((3 * i * i * i) - (i * i * 7)) + (i * 10)) - 35) * i;
			// double target = i * i;
			// double target = ( i + 100 ) * ( i - 100 );
			// double target = i + 6;

			// double target = (Math.cos((i * Math.PI) / 10) * 10) + i;
			// (2)^(SIN(A2*5)*3)+A2
			// double target = Math.pow(2, Math.sin(i * 5) * 3) + i;

			double x = target - gene.getSyntaxTree().eval(gene.getContext());

			delt += x * x;
		}
		return delt;
	}

}