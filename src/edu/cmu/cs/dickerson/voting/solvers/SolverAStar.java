package edu.cmu.cs.dickerson.voting.solvers;

import edu.cmu.cs.dickerson.voting.io.IOUtil;
import edu.cmu.cs.dickerson.voting.model.Data;
import edu.cmu.cs.dickerson.voting.solvers.exceptions.SolverException;

public class SolverAStar extends Solver {

	public SolverAStar(Data data) {
		super(data);
	}

	@Override
	public Solution solve() throws SolverException {
		
		Solution sol = new Solution();
		for(Integer aStar : data.getCandidates()) {
			IOUtil.dPrintln(getID(), "Computing Dodgson score for a*=[" + aStar + "]");
			SingleSolution ss = solve(aStar);
			sol.addSingleSolution(ss);
		}

		return sol;
	}

	@Override
	public SingleSolution solve(Integer aStar) throws SolverException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		return "A* Solver";
	}

	
}
