package edu.cmu.cs.dickerson.voting.solvers;

public class SingleSolution {
	
	private final Integer candidate;
	private final int dodgsonScore;
	private long solveTime = 0;
	
	public SingleSolution(Integer candidate, int dodgsonScore) {
		this.candidate = candidate;
		this.dodgsonScore = dodgsonScore;
	}

	public Integer getCandidate() {
		return candidate;
	}

	public Integer getDodgsonScore() {
		return dodgsonScore;
	}

	public long getSolveTime() {
		return solveTime;
	}

	public void setSolveTime(long solveTime) {
		this.solveTime = solveTime;
	}
	
	@Override
	public String toString() {
		return candidate + " @ " + dodgsonScore;
	}
}
