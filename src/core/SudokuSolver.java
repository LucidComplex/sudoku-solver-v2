package core;

import java.io.*;

/**
 * Created by tan on 12/1/15.
 */

public class SudokuSolver {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.print("Missing filename.\nUsage: java -jar SudokuSolver.jar <filename>\n");
            System.exit(1);
        }

        long start = System.currentTimeMillis();
        GA solver = new GA(args[0]);

        solver.solve();
        long end = System.currentTimeMillis();
        solver.saveSolution(end - start, args[0]);
    }

}


