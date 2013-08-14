package edu.cmu.cs.dickerson.voting.solvers;

import edu.cmu.cs.dickerson.voting.model.Data;
import edu.cmu.cs.dickerson.voting.solvers.exceptions.SolverException;

public abstract class Solver {

	protected final Data data;
	public Solver(Data data) {
		this.data = data;
	}
	
	public abstract Solution solve() throws SolverException;
	public abstract SingleSolution solve(Integer aStar) throws SolverException;

	public abstract String getID();
}
