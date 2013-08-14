package edu.cmu.cs.dickerson.voting.io;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import edu.cmu.cs.dickerson.voting.io.exceptions.LoaderDataException;
import edu.cmu.cs.dickerson.voting.io.exceptions.LoaderException;
import edu.cmu.cs.dickerson.voting.io.exceptions.LoaderFormatException;
import edu.cmu.cs.dickerson.voting.model.Data;
import edu.cmu.cs.dickerson.voting.model.Ordering;

public class Loader {

	private static final char CSV_DELIM = ',';
	private static final String D_OUT = "Loader";

	private Loader() {
	}

	/**
	 * Loads a strict-order complete set of preferences
	 * 
	 * @url http://www.preflib.org/format.php#soc
	 * @param path
	 */
	public static Data loadSOC(String path) throws IOException, LoaderException {

		IOUtil.dPrintln(D_OUT, "Preparing to load data from file: " + path);

		Data data = null;
		CSVReader reader = null;
		try {

			reader = new CSVReader(new FileReader(path), CSV_DELIM);
			String[] line;
			int lineNum = 0;

			// How many alternatives/candidates are available?
			// <Number-of-candidates>:int
			++lineNum;
			if ((line = reader.readNext()) == null || line.length != 1) {
				throw new LoaderFormatException(lineNum);
			}
			int numCands = Integer.valueOf(line[0]);
			if (numCands <= 0) {
				throw new LoaderDataException(lineNum);
			}

			// Load details about each alternative/candidate (ID, name)
			Map<Integer, String> candMap = new HashMap<Integer, String>();
			for (int candIdx = 0; candIdx < numCands; ++candIdx) {

				// <Cand_i-ID>:int <Cand_i-Name>:str
				++lineNum;
				if ((line = reader.readNext()) == null || line.length != 2) {
					throw new LoaderFormatException(lineNum);
				}
				Integer candID = Integer.valueOf(line[0]);
				String candName = line[1].trim();

				if (candMap.containsKey(candID)) {
					throw new LoaderDataException(lineNum,
							"Already loaded data for candidate: " + candID);
				}
				candMap.put(candID, candName);
			}
			data = new Data(candMap);

			// How many ballots were submitted, how many unique orderings?
			// <Number-of-voters>:int <Sum-of-vote-count>:int
			// <Number-of-unqiue-orderings>:int
			++lineNum;
			if ((line = reader.readNext()) == null || line.length != 3) {
				throw new LoaderFormatException(lineNum);
			}
			int numVoters = Integer.valueOf(line[0]);
			int sumVoters = Integer.valueOf(line[1]);
			int numUniqueOrderings = Integer.valueOf(line[2]);
			if (numUniqueOrderings <= 0 || numVoters <= 0) {
				throw new LoaderDataException(lineNum,
						"Too few voters or unique orderings.");
			}
			if (numVoters != sumVoters) {
				throw new LoaderDataException(lineNum,
						"We do not handle weighted relations yet.");
			}

			// For each unique preference ordering, load the support of that
			// ordering
			for (int orderingIdx = 0; orderingIdx < numUniqueOrderings; ++orderingIdx) {

				// <Number-of-ballots>:int <rank1-cand-ID>:int
				// <rank2-cand-ID>:int ... <rankM-cand-ID>:int
				++lineNum;
				if ((line = reader.readNext()) == null
						|| line.length != (1 + numCands)) {
					throw new LoaderFormatException(lineNum);
				}
				Integer support = Integer.valueOf(line[0]);

				// Form this unique preference ordering
				List<Integer> rawOrdering = new ArrayList<Integer>();
				for (int rankIdx = 0; rankIdx < numCands; ++rankIdx) {
					Integer rankICandID = Integer.valueOf(line[1 + rankIdx]);
					if (!candMap.containsKey(rankICandID)) {
						throw new LoaderDataException(lineNum,
								"Ranking for a candidate we've never seen before: "
										+ rankICandID);
					}
					rawOrdering.add(rankICandID);
				}

				Ordering ordering = new Ordering(rawOrdering);
				// Record the support for this preference ordering (immutable key for hashmap)
				data.addBallot(ordering, support);
			}

			IOUtil.dPrintln(D_OUT, "Successfully read " + lineNum + " lines ("
					+ data.getCandidates().size() + " candidates, "
					+ data.getNumBallots() + " ballots)");

		} finally {
			reader.close();
		}

		return data;
	}

}
