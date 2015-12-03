# Running
---------

Compile all files with:

```
javac -g src/core/*.java src/contrib/*.java
```

Then, package into a jar with:

```
cd src
jar SudokuSolver.jar core.SudokuSolver core/*.class contrib/*.class
```

Run the jar file with:

```
java -jar SudokuSolver.jar <filename>
```

# Modifying Parameters
----------------------

To modify parameters, modify the corresponding lines (lines 30 ~ 50) in
GA.java and compile again.


# Input files
-------------

Input files are configured with the sudoku size on the first
line. Followed by the puzzle with 0's for blanks.

ex. 

```
4
1023
0210
4001
0000
```

# Output files
--------------

An output file is generated with the puzzle solution. This file
also includes running time, generations generated, and the algorithm's
configuration.
