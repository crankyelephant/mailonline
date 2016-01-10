package vedran.mailonline.searchengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SearchEngine {

	static File file = null;
	static Map<String, String> domainAbbreviations = new HashMap<String, String>();

	public static void main(String[] args) throws IOException {

		domainAbbreviations.put("alt", "alternative");
		domainAbbreviations.put("computer", "comp");
		domainAbbreviations.put("system", "sys");
		domainAbbreviations.put("recreation", "rec");
		domainAbbreviations.put("science", "sci");
		domainAbbreviations.put("crypt", "cryptography");
		domainAbbreviations.put("medicine", "med");
		domainAbbreviations.put("sociology", "soc");

		ResourceOriginResolver a = new ResourceOriginResolver();
		// File file;
		try {
			file = a.getFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File[] files = file.listFiles();

		// only outside IDE:
		// System.out.print("Enter something: ");
		// String input = System.console().readLine();
		// System.out.println(input);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print("Enter search item: ");
			String input = br.readLine();

			if (input.equals("quit")) {
				System.out.println("Goodbye!");
				System.exit(0);
			}

			// If the search term does not contain logical expressions, rank the
			// docs according to tf-idf metric.
			if (!input.contains(" or ") && !input.contains(" and ")) {
				Map<Double, ArrayList<String>> docs = new HashMap<Double, ArrayList<String>>();

				DocumentRank dr = new DocumentRank(domainAbbreviations, files);
				docs = dr.getRankedDocs(input);
				System.out.println("Ranked documents: ");

				// HashMap<String, Double> response = new HashMap<String,
				// Double>();

				Double[] tfValues = (Double[]) (docs.keySet().toArray(new Double[docs.keySet().size()]));
				Arrays.sort(tfValues);
				for (int i = tfValues.length - 1; i > 0; i--) {
					for (String fileName : docs.get(tfValues[i])) {
						System.out.print(fileName + "(tf-idf = " + tfValues[i] + ") ");
					}
				}

				System.out.println();
				// Otherwise, find the docs corresponding to the logical
				// expression.
			} else {
				Set<String> docs = new HashSet<String>();
				ResponseProducer rp = new ResponseProducer(domainAbbreviations, files);
				docs = rp.getDocs(input);
				System.out.println("Documents mathing the search expression: ");
				if (docs.size() > 500) {
					PrintWriter out = new PrintWriter("results.txt");
					for (String i : docs) {
						out.println(i);
					}
					out.close();
					System.out.println("Too many results. Check the file \"results.txt\".");
				} else {
					for (String i : docs) {
						System.out.print(i + " ");

					}
				}
				System.out.println();
			}
		}

	}

}
