genetic-programming
===================

Symbolic regression solver, based on genetic programming methodology.
More info in this [article](http://habrahabr.ru/post/163195/)

This project depends on [Generic Algorithm project](https://github.com/lagodiuk/genetic-algorithm) (has a maven dependency)

### usage ###
<ol>
<li> git clone https://github.com/lagodiuk/genetic-algorithm.git </li>
<li> git clone https://github.com/lagodiuk/genetic-programming.git </li>
<li> mvn -f genetic-algorithm/pom.xml install </li>
<li> mvn -f genetic-programming/pom.xml install </li>
<li>
add maven dependencies to your project:
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
</li>
</ol>