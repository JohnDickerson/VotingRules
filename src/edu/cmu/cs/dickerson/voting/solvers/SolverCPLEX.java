package edu.cmu.cs.dickerson.voting.solvers;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.util.HashMap;
import java.util.Map;

import edu.cmu.cs.dickerson.voting.io.IOUtil;
import edu.cmu.cs.dickerson.voting.model.Data;
import edu.cmu.cs.dickerson.voting.model.Ordering;
import edu.cmu.cs.dickerson.voting.solvers.exceptions.SolverException;

public class SolverCPLEX extends Solver {

	private Map<Ordering, Integer> orderingToStartIdx;

	public SolverCPLEX(Data data) {
		super(data);
	}

	@Override
	public String getID() {
		return "CPLEX";
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

		SingleSolution ss = null;
		try {
			IloCplex cplex = new IloCplex();
			//cplex.setParam(IloCplex.IntParam.Threads, 4);
			//cplex.setParam(IloCplex.DoubleParam.TiLim, 3600.00);


			// Our only decision variables are the binary x^i_k:
			// for each agent i, do we swap a* up exactly k steps
			int numDecVars = 0;
			orderingToStartIdx = new HashMap<Ordering, Integer>();
			for(Ordering o : data.getBallots().keySet()) {

				int orderingCount = data.getBallots().get(o); 
				int aStarRank = o.getPosition(aStar);

				orderingToStartIdx.put(o, numDecVars);
				numDecVars += orderingCount * (aStarRank - 1);

			}
			IloNumVar[] x = cplex.boolVarArray(numDecVars);

			// These are the k-values in the objective, which are the cost to
			// swap a^* up by k slots (i.e., cost is exactly k)
			int[] costs = new int[x.length];
			for(Ordering o : data.getBallots().keySet()) {

				int orderingCount = data.getBallots().get(o); 
				int orderingStartIdx = orderingToStartIdx.get(o);
				int aStarRank = o.getPosition(aStar);

				for(int oIdx=0; oIdx<orderingCount; ++oIdx) {
					for(int k=1; k<=aStarRank-1; ++k) {
						costs[orderingStartIdx + (aStarRank-1)*oIdx + (k-1)] = k;
					}
				}
			}

			// Objective:
			// Minimize \sum_{i \in Agents} \sum_{k=1}^{r^i - 1} k * x^i_k
			cplex.addMinimize(cplex.scalProd(costs, x));


			// Subject to:
			// \sum_{i \in N} \sum_{a's rank}^{r^i - 1} x^i_k >= def(a)   \forall a \neq a*
			for(Integer candidate : data.getCandidates()) {

				// Don't need to enforce winning against itself
				if(candidate.equals(aStar)) { continue; }

				// If a* is pairwise winning against this candidate already, then
				// a* will still be pairwise winning regardless of what we do; skip.
				int deficit = data.calcDeficit(aStar, candidate);
				
				if(deficit <= 0) { continue; }

				IloLinearNumExpr sum = cplex.linearNumExpr(); 
				for(Ordering o : data.getBallots().keySet()) {

					int candidateRank = o.getPosition(candidate);
					int aStarRank = o.getPosition(aStar);
					int orderingStartIdx = orderingToStartIdx.get(o);

					// If a* is already winning against a in this ordering, skip (since
					// this wasn't contributing to the deficit def(a) anyway)
					if(aStarRank < candidateRank) { continue; }

					int orderingCount = data.getBallots().get(o); 
					for(int oIdx=0; oIdx < orderingCount; ++oIdx) {
						for(int k=(aStarRank-candidateRank); k<=aStarRank-1; ++k) {
							int xIdx = orderingStartIdx + (aStarRank-1)*oIdx + (k-1);
							sum.addTerm(1.0, x[xIdx]);
						}

					}
				}
				cplex.addGe(sum, deficit);
			}


			// \sum_{k=1}^{r^i - 1} x^i_k <= 1    \forall i \in N
			// We bump up a* EXACTLY a certain amount (or not at all)
			for(Ordering o : data.getBallots().keySet()) {

				int orderingCount = data.getBallots().get(o); 
				int orderingStartIdx = orderingToStartIdx.get(o);
				int aStarRank = o.getPosition(aStar);

				for(int oIdx=0; oIdx<orderingCount; ++oIdx) {
					
					IloLinearNumExpr sum = cplex.linearNumExpr(); 
					for(int k=1; k<=aStarRank-1; ++k) {
						int xIdx = orderingStartIdx + (aStarRank-1)*oIdx + (k-1);
						sum.addTerm(1.0, x[xIdx]);
					}
					cplex.addLe(sum, 1.0);
				}
			}
			
			

			//
			// Solve the model
			long solveStartTime = System.nanoTime();
			boolean solvedOK = cplex.solve();
			long solveEndTime = System.nanoTime();
			long solveTime = solveEndTime - solveStartTime;

			if(solvedOK) {

				IOUtil.dPrintln(getID(), "Found an answer! CPLEX status: " + cplex.getStatus() + ", Time: " + ((double) solveTime / 1000000000.0));

				// The objective value is the Dodgson score for a*
				double objectiveValue = cplex.getObjValue();
				int dodgsonScore = (int) Math.round(objectiveValue);
				IOUtil.dPrintln(getID(), "Computed Dodgson score of " + dodgsonScore + " for a*=[" + aStar + "]");

				ss = new SingleSolution(aStar, dodgsonScore);

				// Elapsed solve time (just solve time, not IP write time)
				ss.setSolveTime(solveTime);

			} else {
				throw new SolverException("cplex.solve() returned false.");
			}

			cplex.end();		

		} catch(IloException e) {
			System.err.println("Exception thrown during CPLEX solve: " + e);
			throw new SolverException(e.toString());
		}

		// Try to get Java to free some memory
		orderingToStartIdx = null;

		return ss;
	}

}
