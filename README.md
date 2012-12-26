genetic-programming
===================

Symbolic regression solver, based on genetic programming methodology.
More info in this [article](http://habrahabr.ru/post/163195/)

This project depends on [Generic Algorithm project](https://github.com/lagodiuk/genetic-algorithm) (as a maven dependency)

### usage ###
1) git clone https://github.com/lagodiuk/genetic-algorithm.git
2) git clone https://github.com/lagodiuk/genetic-programming.git
3) mvn -f genetic-algorithm/pom.xml install
4) mvn -f genetic-programming/pom.xml install
5) add maven dependencies to your project:
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