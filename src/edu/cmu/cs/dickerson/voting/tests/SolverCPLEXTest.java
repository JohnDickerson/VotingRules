package edu.cmu.cs.dickerson.voting.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.cmu.cs.dickerson.voting.model.Data;
import edu.cmu.cs.dickerson.voting.model.Ordering;
import edu.cmu.cs.dickerson.voting.solvers.Solution;
import edu.cmu.cs.dickerson.voting.solvers.Solver;
import edu.cmu.cs.dickerson.voting.solvers.SolverCPLEX;
import edu.cmu.cs.dickerson.voting.solvers.exceptions.SolverException;

public class SolverCPLEXTest {

	// This simple test is from the Caragiannis et al. AIJ paper
	
	@Test
	public void test() {
		
		// Simple example in Section 2 (Preliminaries); three ballots and candidates
		// Scores should be: a @ 0, b @ 1, c @ 3
		Map<Integer, String> candMap = new HashMap<Integer, String>();
		candMap.put(1, "a");
		candMap.put(2, "b");
		candMap.put(3, "c");
		
		Data d1 = new Data(candMap);
		
		List<Integer> b1 = new ArrayList<Integer>();
		b1.add(1); b1.add(2); b1.add(3);
		List<Integer> b2 = new ArrayList<Integer>();
		b2.add(2); b2.add(1); b2.add(3);
		List<Integer> b3 = new ArrayList<Integer>();
		b3.add(1); b3.add(3); b3.add(2);
		
		d1.addBallot(new Ordering(b1));
		d1.addBallot(new Ordering(b2));
		d1.addBallot(new Ordering(b3));
		
		Solver solver = new SolverCPLEX(d1);
		Solution solution = null;
		try {
			solution = solver.solve();
			System.out.println(solution);
		} catch(SolverException e) {
			System.err.println(e);
		}
		
		assertEquals(solution.getDodgsonScore(1).intValue(), 0);
		assertEquals(solution.getDodgsonScore(2).intValue(), 1);
		assertEquals(solution.getDodgsonScore(3).intValue(), 3);
		
	}

}
