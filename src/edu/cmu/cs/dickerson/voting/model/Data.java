package edu.cmu.cs.dickerson.voting.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Data {

	private final Map<Integer, String> candMap;
	private Map<Ordering, Integer> ballots;
	private int numBallots;
	
	public Data(Map<Integer, String> candMap) {
		this.candMap = candMap;
		this.ballots = new HashMap<Ordering, Integer>();
		this.numBallots = 0;
	}

	public String getCandidateName(Integer candID) {
		return candMap.get(candID);
	}

	public Set<Integer> getCandidates() {
		return candMap.keySet();
	}

	public void addBallot(final Ordering ballot) {
		addBallot(ballot, 1);
	}

	public void addBallot(final Ordering ballot, Integer count) {
		if(!ballots.containsKey(ballot)) { 
			ballots.put(ballot, count);
		} else { 
			ballots.put(ballot, ballots.get(ballot)+count);
		}
		numBallots += count;
	}
	
	public int getNumBallots() {
		return numBallots;
	}
	
	/**
	 * Calculates the deficit of a* relative to alternative a, the number of 
	 * agents whose ranks would need to change such that a* would pairwise beat a.
	 * Given N ballots and alternative a, the deficit of a* to a is:
 	 * def(a) = max(0, 1 + floor(N/2) - #PairwiseBeating(a*, a)
	 * @param aStar
	 * @param a
	 * @return def(a)
	 */
	public int calcDeficit(Integer aStar, Integer a) {

		int deficit = 1                               // a winner must be >50% winner
				+ (getNumBallots()/2)                 // int/int = floor for nonnegatives
				- calcPairwiseWinningCount(aStar, a); // #agents already ranking a* > a

		// If the deficit is nonpositive at this point, the candidate a is dead
		return deficit > 0 ? deficit : 0; 
	}
	
	/**
	 * Calculates how many voters currently rank a above b.  Formally,
	 * P(a,b) := {i \in N: a R^i b} for the binary preference relation R^i
	 * @param a
	 * @param b
	 * @return P(a,b)
	 */
	public int calcPairwiseWinningCount(Integer a, Integer b) {
		
		int numVoters = 0;
		for(Ordering ordering : ballots.keySet()) {
			
			// If the ordering ranks a above b, add the number of ballots with
			// that ordering to the count
			if(ordering.relativePosition(a, b) > 0) {
				numVoters += ballots.get(ordering);
			}
		}
		
		return numVoters;
	}
	
	public Map<Ordering, Integer> getBallots() {
		return ballots;
	}

}
