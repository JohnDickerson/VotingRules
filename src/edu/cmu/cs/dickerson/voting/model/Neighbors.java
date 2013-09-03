package edu.cmu.cs.dickerson.voting.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.cmu.cs.dickerson.voting.io.IOUtil;

public class Neighbors {

	private Map<Ordering, Set<Ordering>> neighborMap; 
	
	private Neighbors(Map<Ordering, Set<Ordering>> neighborMap) {
		this.neighborMap = neighborMap;
	}
	
	public static Neighbors createNeighbors(Set<Ordering> orderings) {
		
		IOUtil.dPrintln("Creating neighbors for all " + orderings.size() + " orderings.");
		
		// Number of candidates m, each ordering has m-1 neighbors
		int m = orderings.isEmpty() ? 0 : orderings.iterator().next().getOrdering().size();
		
		Map<Ordering, Set<Ordering>> neighborMap = new HashMap<Ordering, Set<Ordering>>();
		for(Ordering ordering : orderings) {
			
			List<Integer> baseOrdering = ordering.getOrdering();
			Set<Ordering> singleSwaps = new HashSet<Ordering>();
			for(int swapIdx=0; swapIdx<m-1; swapIdx++) {
				// TODO switch everything to numeric, stop using full Ordering
				throw new UnsupportedOperationException();
			}
		}
		return new Neighbors(neighborMap);
	}
}
