genetic-programming
===================

Symbolic regression solver, based on genetic programming methodology.
More info in this [article](http://habrahabr.ru/post/163195/)

This project depends on [Generic Genetic Algorithm project](https://github.com/lagodiuk/genetic-algorithm) (has a maven dependency)

## Quick start ##

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

/**
 * f(x) - ? <br/>
 * 
 * f(0) = 0 <br/>
 * f(1) = 11 <br/>
 * f(2) = 24 <br/>
 * f(3) = 39 <br/>
 * f(4) = 56 <br/>
 * f(5) = 75 <br/>
 * f(6) = 96 <br/>
 * 
 * (target function is f(x) = x^2 + 10*x)
 */
public class HelloSymbolicRegression {

	public static void main(String[] args) {

		// define training set
		TabulatedFunctionFitness fitness =
				new TabulatedFunctionFitness(
						new Target().when("x", 0).targetIs(0),
						new Target().when("x", 1).targetIs(11),
						new Target().when("x", 2).targetIs(24),
						new Target().when("x", 3).targetIs(39),
						new Target().when("x", 4).targetIs(56),
						new Target().when("x", 5).targetIs(75),
						new Target().when("x", 6).targetIs(96));

		SymbolicRegressionEngine engine =
				new SymbolicRegressionEngine(
						fitness,
						// define variables
						list("x"),
						// define base functions
						list(Functions.ADD, Functions.SUB, Functions.MUL, Functions.VARIABLE, Functions.CONSTANT));

		addListener(engine);

		// 200 iterations
		engine.evolve(200);
	}

	/**
	 * Track each iteration
	 */
	private static void addListener(SymbolicRegressionEngine engine) {
		engine.addIterationListener(new SymbolicRegressionIterationListener() {
			@Override
			public void update(SymbolicRegressionEngine engine) {

				Expression bestSyntaxTree = engine.getBestSyntaxTree();

				double currFitValue = engine.fitness(bestSyntaxTree);

				// log to console
				System.out.println(
						String.format("iter = %s \t fit = %s \t func = %s",
								engine.getIteration(), currFitValue, bestSyntaxTree.print()));

				// halt condition
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