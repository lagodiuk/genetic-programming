package com.lagodiuk.gp.math;

import com.lagodiuk.ga.Fitness;

public class GpFitness2 implements Fitness<GpGene, Double> {

	@Override
	public Double calculate(GpGene gene) {
		double delt = 0;
		for (int x = -5; x < 5; x++) {
			gene.getContext().setVariable("x", x);
			for (int y = -5; y < 5; y++) {
				gene.getContext().setVariable("y", y);

				// double target = x * 5 + y * ( y - 4 );
				// double target = x + y;
				// double target = x * 5 + y * ( y - 4 ) + x * y;
				double target = x * x;
				// double target = x * x + y * y;

				double val = target - gene.getSyntaxTree().eval(gene.getContext());

				delt += val * val;
			}
		}
		return delt;
	}

}