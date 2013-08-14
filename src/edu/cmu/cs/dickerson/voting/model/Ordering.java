package edu.cmu.cs.dickerson.voting.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ordering {

	// Raw preference ordering, e.g., [First place candidate, second place, ..., last place]
	private final List<Integer> ordering;
	
	// Mapping of candidate identifiers -> position in the raw preference ordering
	private final Map<Integer, Integer> candPositionMap;
	
	public Ordering(List<Integer> ordering) {
		this.ordering = Collections.unmodifiableList(ordering);
		
		int position = 0;
		candPositionMap = new HashMap<Integer, Integer>();
		for(Integer cand : this.ordering) {
			candPositionMap.put(cand, ++position);
		}
	}

	public List<Integer> getOrdering() {
		return ordering;
	}

	public Integer getPosition(Integer candidate) {
		return candPositionMap.get(candidate);
	}
	
	/**
	 * Returns the relative difference in rankings between two alternatives.
	 * E.g., if this ordering has a at position 5 and b at position 2, then the
	 * relative difference is (2 - 5) = -3; alternative a is -3 above b
	 * @param a
	 * @param b
	 * @return relative difference in position between a and b
	 */
	public Integer relativePosition(Integer a, Integer b) {
		return candPositionMap.get(b) - candPositionMap.get(a);
	}
	
	@Override
	public boolean equals(Object o) {
		return ordering.equals(o);
	}
	
	@Override
	public int hashCode() {
		return ordering.hashCode();
	}
}
