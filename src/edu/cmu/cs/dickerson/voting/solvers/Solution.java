package edu.cmu.cs.dickerson.voting.solvers;

import java.util.HashMap;
import java.util.Map;

public class Solution {

	private Map<Integer, SingleSolution> solutions;
	private long totalSolveTime = 0;
	
	private SingleSolution dodgsonWinner = null;
	
	public Solution() {
		solutions = new HashMap<Integer, SingleSolution>();
	}
	
	public void addSingleSolution(SingleSolution s) {
		
		solutions.put(s.getCandidate(), s);
		totalSolveTime += s.getSolveTime();
		
		if(dodgsonWinner == null || s.getDodgsonScore() < dodgsonWinner.getDodgsonScore()) {
			dodgsonWinner = s;
		}
	}
	
	public Map<Integer, SingleSolution> getSolutions() {
		return solutions;
	}

	public long getTotalSolveTime() {
		return totalSolveTime;
	}

	public SingleSolution getDodgsonWinner() {
		return dodgsonWinner;
	}

	public Integer getDogsonScore() {
		if(dodgsonWinner != null) {
			return getDodgsonScore(dodgsonWinner.getCandidate());
		} else {
			return null;
		}
	}
	
	public Integer getDodgsonScore(Integer candidate) {
		return solutions.get(candidate).getDodgsonScore();
	}
	
	@Override
	public String toString() {
		
		if(dodgsonWinner == null) {
			return "[Empty Solution]";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Winner: " + dodgsonWinner.toString() + "   [");
		for(Integer aStar : solutions.keySet()) {
			sb.append(" (" + solutions.get(aStar) + ")");
		}
		sb.append(" ]");
		
		return sb.toString();
	}

}

