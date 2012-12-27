genetic-programming
===================

Symbolic regression solver, based on genetic programming methodology.
More info in this [article](http://habrahabr.ru/post/163195/)

This project depends on [Generic Genetic Algorithm project](https://github.com/lagodiuk/genetic-algorithm) (has a maven dependency)

## Usage ##

### just download jar ###
The most simple way is download <i>symbolic_regression_X.X.jar</i> from http://github.com/lagodiuk/genetic-programming/tree/master/bin
and add it to your classpath 

### try it with maven ###
<ol>
<li> git clone https://github.com/lagodiuk/genetic-algorithm.git </li>
<li> git clone https://github.com/lagodiuk/genetic-programming.git </li>
<li> mvn -f genetic-algorithm/pom.xml install </li>
<li> mvn -f genetic-programming/pom.xml install </li>
</ol>

Now you can add following maven dependencies to your project:
```xml
<dependency>
	<groupId>com.lagodiuk</groupId>
	<artifactId>ga</artifactId>
	<version>1.0.1</version>
</dependency>

<dependency>
	<groupId>com.lagodiuk</groupId>
	<artifactId>gp</artifactId>
	<version>1.0</version>
</dependency>
```

### hello world ###
```java
import java.util.LinkedList;
import java.util.List;

import com.lagodiuk.gp.symbolic.SymbolicRegressionEngine;
import com.lagodiuk.gp.symbolic.SymbolicRegressionIterationListener;
import com.lagodiuk.gp.symbolic.TabulatedFunctionFitness;
import com.lagodiuk.gp.symbolic.Target;
import com.lagodiuk.gp.symbolic.interpreter.Expression;
import com.lagodiuk.gp.symbolic.interpreter.Functions;

public class HelloSymbolicRegression {

	public static void main(String[] args) {
	
		TabulatedFunctionFitness fitnessFunction =
				new TabulatedFunctionFitness(
						new Target().when("x", 26).when("y", 35).when("z", 1).targetIs(830),
						new Target().when("x", 8).when("y", 24).when("z", -11).targetIs(130),
						new Target().when("x", 20).when("y", 1).when("z", 10).targetIs(477),
						new Target().when("x", 33).when("y", 11).when("z", 2).targetIs(1217),
						new Target().when("x", 37).when("y", 16).when("z", 7).targetIs(1524));
						
		SymbolicRegressionEngine engine =
				new SymbolicRegressionEngine(
						fitnessFunction,
						list("x", "y", "z"),
						list(Functions.ADD, Functions.SUB, Functions.MUL, Functions.VARIABLE, Functions.CONSTANT));

		addListener(engine);

		engine.evolve(200);
		
		System.out.println(engine.getBestSyntaxTree().print());
	}

	private static void addListener(SymbolicRegressionEngine engine) {
		engine.addIterationListener(new SymbolicRegressionIterationListener() {
			@Override
			public void update(SymbolicRegressionEngine engine) {

				Expression bestSyntaxTree = engine.getBestSyntaxTree();

				double currFitValue = engine.fitness(bestSyntaxTree);

				System.out.println(
						String.format("iter = %s \t fit = %s \t func = %s",
								engine.getIteration(), currFitValue, bestSyntaxTree.print()));

				if (currFitValue < 5) {
					engine.terminate();
				}
			}
		});
	}

	private static <T> List<T> list(T... items) {
		List<T> list = new LinkedList<T>();
		for (T item : items) {
			list.add(item);
		}
		return list;
	}
}
```