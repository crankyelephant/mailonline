package vedran.mailonline.searchengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Produces response for queries represented as logical expressions (consisting
 * any combination of "and" and "or" clauses").
 * 
 * @author vedran
 *
 */
public class ResponseProducer {

	private Map<String, String> domainAbbreviations = new HashMap<String, String>();
	private File[] directories;

	// Prioritized logical operators:
	// for each OR clause a list of AND clauses.
	private Map<String, ArrayList<String>> ors = new HashMap<String, ArrayList<String>>();

	// response as a list of doc names
	private Set<String> response = new HashSet<String>();

	public ResponseProducer(Map<String, String> domainAbbreviations, File[] files) {
		this.domainAbbreviations = domainAbbreviations;
		this.directories = files;
	}

	/**
	 * Get docs corresponding to the query represented as a logical expression.
	 * 
	 * @param input
	 * @return docs as set (no repetition)
	 */
	Set<String> getDocs(String input) {
		input = input.toLowerCase();

		parseOrs(input);
		searchThroughOrs();

		for (String or : ors.keySet()) {
			try {
				findDocsForAnds(ors.get(or));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		return response;

	}

	/**
	 * OR clauses are tops of the tree, so first find them.
	 * 
	 * @param input
	 */
	private void parseOrs(String input) {
		if (!input.contains(" or ")) {
			ors.put(input, new ArrayList<String>());
			// return;
		} else {
			ors.put(input.substring(0, input.indexOf(" or ")), new ArrayList<String>());
			parseOrs(input.substring(input.indexOf(" or ") + 4, input.length()));
		}
	}

	private String currentOr;

	private void searchThroughOrs() {
		for (String or : ors.keySet()) {
			currentOr = or;
			parseAnds(or);
		}
	}

	/**
	 * For every OR clause, find AND clauses.
	 * 
	 * @param ands
	 */
	private void parseAnds(String ands) {
		if (!ands.contains(" and ")) {
			ors.get(this.currentOr).add(ands);
		} else {
			ors.get(this.currentOr).add(ands.substring(0, ands.indexOf(" and ")));
			parseAnds(ands.substring(ands.indexOf(" and ") + 5, ands.length()));
		}
	}

	private String currentDoc;

	/**
	 * For each AND clause, find the corresponding documents.
	 * 
	 * @param ands
	 * @throws FileNotFoundException
	 */
	private void findDocsForAnds(ArrayList<String> ands) throws FileNotFoundException {
		for (File directory : directories) {
			StringTokenizer directoryNameComponents = new StringTokenizer(directory.getName());
			while (directoryNameComponents.hasMoreTokens()) {
				// If any search item of the AND expression corresponds to a
				// name component (separated by ".") of the current directory,
				// then remove this item from the list, assuming that every
				// document in that directory is relevant for that item.
				String directoryNameComponent = directoryNameComponents.nextToken(".");
				if (ands.contains(directoryNameComponent)) {
					ands.remove(directoryNameComponent);
				}
				// Also check for abbreviations.
				if (domainAbbreviations.containsKey(directoryNameComponent)) {
					if (ands.contains(domainAbbreviations.get(directoryNameComponent))) {
						ands.remove(directoryNameComponent);
					}
				}
			}
			for (File file : directory.listFiles()) {
				this.currentDoc = file.getName();
				Scanner scanner = new Scanner(file);
				scanner.useDelimiter("\\Z");
				String text = scanner.next().toLowerCase();
				// Create deep copy of the list of AND clauses
				ArrayList<String> andsCopy = new ArrayList<String>();
				for (String and : ands) {
					andsCopy.add(and);
				}
				findAndsInDoc(text, andsCopy);
				scanner.close();
			}
		}
	}

	/**
	 * Search for a term (AND operand) in document.
	 * 
	 * @param text
	 * @param ands
	 */
	private void findAndsInDoc(String text, ArrayList<String> ands) {
		if (text == null)
			return;
		// This is the case where all the terms have been found in the doc (and
		// therefore had been removed from the list below).
		// Add the doc to the response and return.
		if (ands.size() == 0) {
			response.add(currentDoc);
			return;
		} else {
			// Tokenize the string, in order to avoid making "cat" in
			// "caterpillar" a hit.
			StringTokenizer tokens = new StringTokenizer(text);
			String and = ands.get(0);
			while (tokens.hasMoreTokens() && ands.size() != 0) {
				// If the word is found, remove it from the list of AND clauses
				// and call the function recursively
				// for other words in the rest of the text.
				String token = tokens.nextToken();
				if (ands.get(0).equals(token)) {
					ands.remove(0);
					findAndsInDoc(text.substring(text.indexOf(and) + and.length(), text.length()), ands);
				}
			}

		}
	}

}
