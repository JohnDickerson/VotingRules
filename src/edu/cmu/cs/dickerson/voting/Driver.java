package edu.cmu.cs.dickerson.voting;

import java.io.IOException;

import edu.cmu.cs.dickerson.voting.io.IOUtil;
import edu.cmu.cs.dickerson.voting.io.Loader;
import edu.cmu.cs.dickerson.voting.io.exceptions.LoaderException;
import edu.cmu.cs.dickerson.voting.model.Data;
import edu.cmu.cs.dickerson.voting.solvers.Solution;
import edu.cmu.cs.dickerson.voting.solvers.Solver;
import edu.cmu.cs.dickerson.voting.solvers.SolverCPLEX;
import edu.cmu.cs.dickerson.voting.solvers.exceptions.SolverException;


//If running with CPLEX, need to add a VM argument to the CPLEX library, e.g.:
//-Xmx8g -Djava.library.path=/Users/spook/ILOG/CPLEX_Studio122/cplex/bin/x86-64_darwin9_gcc4.0
//-Xmx2g -Djava.library.path=/usr0/home/jpdicker/ILOG/CPLEX_Studio_AcademicResearch122/cplex/bin/x86-64_sles10_4.1

public class Driver {

	private static String getRootDir() {

		boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
		boolean isMacOsX = System.getProperty("os.name").toLowerCase().contains("mac os x");

		String rootDir;
		if(isWindows) {
			rootDir = "C:\\amem\\voting\\data\\";
			IOUtil.dPrintln("Detected Window as operating system");
		} else if(isMacOsX) {
			rootDir = "/Users/spook/amem/voting/data/";
			IOUtil.dPrintln("Detected Mac OS X as operating system");
		} else {
			rootDir = "/usr0/home/jpdicker/amem/voting/data/";
			IOUtil.dPrintln("Detected *nix-based operating system");
		}

		return rootDir;
	}

	public static void main(String[] args) {


		//
		// Load voting data from a file "path" into Data data
		String rootDir = getRootDir();
		String path = rootDir + "ED-00004-00000200.soc"; // Netflix competition data, induced completeness

		Data data = null;
		try {
			data = Loader.loadSOC(path);
		} catch(IOException e) {
			System.err.println("Encountered a system IO error while loading file: " + path);
			System.err.println(e);
			System.exit(-1);
		} catch(LoaderException e) {
			System.err.println("There's something wrong with the format of the data, or the data itself, in file: " + path);
			System.err.println(e);
			System.exit(-1);
		}

		//
		// Solve for the Dodgson winner given our voter data
		Solver solver = new SolverCPLEX(data);
		Solution solution = null;
		try {
			solution = solver.solve();
			System.out.println(solution);
		} catch(SolverException e) {
			System.err.println(e);
		}

	}

}
