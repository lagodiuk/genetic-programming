/*******************************************************************************
 * Copyright 2012 Yuriy Lagodiuk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.lagodiuk.gp.symbolic.example;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.lagodiuk.gp.symbolic.SymbolicRegressionEngine;
import com.lagodiuk.gp.symbolic.SymbolicRegressionIterationListener;
import com.lagodiuk.gp.symbolic.TabulatedFunctionFitness;
import com.lagodiuk.gp.symbolic.Target;
import com.lagodiuk.gp.symbolic.interpreter.Expression;
import com.lagodiuk.gp.symbolic.interpreter.Function;
import com.lagodiuk.gp.symbolic.interpreter.Functions;

public class Main {

	private static NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);

	private static FileInputStream fileIn;

	private static PrintWriter fileOut;

	private static int iteration = 1;

	private static boolean evolved = false;

	private static double threshold = 10;

	public static void main(String[] args) throws Exception {
		System.out.println("Symbolic regression solver");

		configureInputOutput(args);
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(fileIn));

		List<Function> functions = getFunctions(inputReader);
		List<String> variables = getVariables(inputReader);
		TabulatedFunctionFitness fitness = getTrainingData(inputReader, variables);

		SymbolicRegressionEngine engine = new SymbolicRegressionEngine(fitness, variables, functions);

		final String prefix = makePrefix(variables);
		engine.addIterationListener(new SymbolicRegressionIterationListener() {
			private double prevFitValue = -1;

			@Override
			public void update(SymbolicRegressionEngine engine) {
				Expression bestSyntaxTree = engine.getBestSyntaxTree();
				double currFitValue = engine.fitness(bestSyntaxTree);
				if (Double.compare(currFitValue, this.prevFitValue) != 0) {
					outPrintln();
					outPrintln(prefix + bestSyntaxTree.print());
				}

				outPrintln(String.format("%s \t %s", iteration, currFitValue));
				++iteration;
				this.prevFitValue = currFitValue;
				if (currFitValue < threshold) {
					engine.terminate();
					evolved = true;
				}
			}
		});

		outPrintln();
		outPrintln(String.format("Start time is: %s", new Date()));

		BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			engine.evolve(50);
			boolean terminate = true;
			if (!evolved) {
				System.out.println("Continue? (50 iterations) Y/N (don't forget to press Enter)");
				String s = systemIn.readLine();
				if ("y".equalsIgnoreCase(s)) {
					terminate = false;
				}
			}
			if (terminate) {
				break;
			}
		}

		outPrintln();
		outPrintln("Best function is:");
		outPrintln(prefix + engine.getBestSyntaxTree().print());
		outPrintln();
		outPrintln(String.format("End time is: %s", new Date()));
		outPrintln();

		closeInOut();
	}

	private static String makePrefix(List<String> variables) {
		String vars = variables.toString();
		vars = vars.substring(1, vars.length() - 1);
		return String.format("f(%s) = ", vars);
	}

	private static List<String> getVariables(BufferedReader inputReader) throws Exception {
		List<String> variables = new ArrayList<String>();
		String s = inputReader.readLine();
		while ((s.startsWith("#")) || (s.trim().isEmpty())) {
			s = inputReader.readLine();
		}
		s = s.replaceAll("f\\((.*)\\).*", "$1").trim();
		for (String variableName : s.split("\\,")) {
			variables.add(variableName.trim());
		}
		return variables;
	}

	private static List<Function> getFunctions(BufferedReader inputReader) throws Exception {
		Set<Function> functions = new HashSet<Function>();
		functions.add(Functions.CONSTANT);
		functions.add(Functions.VARIABLE);
		String s = inputReader.readLine();
		while ((s.startsWith("#")) || (s.trim().isEmpty())) {
			s = inputReader.readLine();
		}
		for (String functionName : s.split("\\s+")) {
			Function f = Functions.valueOf(functionName);
			functions.add(f);
		}
		List<Function> functionsList = new ArrayList<Function>(functions);
		return functionsList;
	}

	private static TabulatedFunctionFitness getTrainingData(
			BufferedReader inputReader, List<String> variables) throws Exception {
		List<Target> targets = new LinkedList<Target>();
		String s = inputReader.readLine();
		while ((s.startsWith("#")) || (s.trim().isEmpty())) {
			s = inputReader.readLine();
		}
		int variablesCount = variables.size();
		while (s != null) {
			if ((s.startsWith("#")) || (s.trim().isEmpty())) {
				s = inputReader.readLine();
				continue;
			}

			if (s.matches("[Tt]hreshold.*")) {
				s = s.replaceAll("[Tt]hreshold\\s*=(.*)", "$1").trim();
				threshold = numberFormat.parse(s).doubleValue();
				s = inputReader.readLine();
				continue;
			}

			String[] split = s.split("=");
			String left = split[1].trim();
			String right = split[0].trim();
			right = right.replaceAll("f\\((.*)\\)", "$1");

			double targetValue = numberFormat.parse(left).doubleValue();

			String[] values = right.split("\\,");
			Target target = new Target();
			for (int i = 0; i < variablesCount; i++) {
				double value = numberFormat.parse(values[i].trim()).doubleValue();
				target.when(variables.get(i), value);
			}
			target.targetIs(targetValue);
			targets.add(target);

			s = inputReader.readLine();
		}
		return new TabulatedFunctionFitness(targets);
	}

	private static void configureInputOutput(String[] args) throws FileNotFoundException {
		switch (args.length) {
			case 1:
				fileIn = new FileInputStream(args[0]);
				break;

			case 2:
				fileIn = new FileInputStream(args[0]);
				fileOut = new PrintWriter(args[1]);
				break;
		}
	}

	private static void outPrintln() {
		System.out.println();
		if (fileOut != null) {
			fileOut.println();
		}
	}

	private static void outPrintln(String message) {
		System.out.println(message);
		if (fileOut != null) {
			fileOut.println(message);
		}
	}

	private static void closeInOut() throws Exception {
		if (fileIn != null) {
			fileIn.close();
		}
		if (fileOut != null) {
			fileOut.close();
		}
	}

}
