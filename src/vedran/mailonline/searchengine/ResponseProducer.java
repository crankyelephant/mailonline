package vedran.mailonline.searchengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ResponseProducer {

	private Map<String, String> domainAbbreviations = new HashMap<String, String>();
	private File[] directories;

	// Prioritized logical operators:
	// for each OR clause a list of AND clauses.
	private Map<String, ArrayList<String>> ors = new HashMap<String, ArrayList<String>>();

	// response as a list of doc names
	private List<String> response = new ArrayList<String>();

	public ResponseProducer(Map<String, String> domainAbbreviations, File[] files) {
		this.domainAbbreviations = domainAbbreviations;
		this.directories = files;
	}

	List<String> getDocs(String input) {
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

	private void parseOrs(String input) {
		if (!input.contains(" or ")) {
			ors.put(input, new ArrayList<String>());
			return;
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

	private void parseAnds(String ands) {
		if (!ands.contains(" and ")) {
			ors.get(this.currentOr).add(ands);
		} else {
			ors.get(this.currentOr).add(ands.substring(0, ands.indexOf(" and ")));
			parseAnds(ands.substring(ands.indexOf(" and ") + 5, ands.length()));
		}
	}

	private String currentDoc;

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
				String text = scanner.next();
				findAndsInDoc(text, ands);
				scanner.close();
			}
		}
	}

	private void findAndsInDoc(String text, ArrayList<String> ands) {
		if (text == null)
			return;
		// This is the case where all the terms have been found in the doc.
		// Add the doc to the response and return.
		if (ands == null) {
			response.add(currentDoc);
			return;
		} else {
			// Tokenize the string, in order to avoid "finding" "cat" in
			// "caterpillar".
			StringTokenizer tokens = new StringTokenizer(text);
			String and = ands.get(0);
			while (tokens.hasMoreTokens()) {
				// If the word is found, remove it from the list of AND clauses
				// and call the function recursively
				// for other words in the rest of the text.
				if (and.equals(tokens.nextToken())) {
					ands.remove(0);
					findAndsInDoc(text.substring(text.indexOf(and) + and.length(), text.length()), ands);
				}
			}
			return;
		}
	}

}
