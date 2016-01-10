package vedran.mailonline.searchengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File[] files = file.listFiles();

		// only outside IDE!!!
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

			List<String> docs = new ArrayList<String>();

			// If the search term does not contain logical expressions, rank the
			// docs according to tf-idf metric.
			if (!input.contains(" or ") && !input.contains(" and ")) {
				DocumentRank dr = new DocumentRank(domainAbbreviations, files);
				docs = dr.getRankedDocs(input);
				System.out.println("Ranked documents: ");
				for (String i : docs) {
					System.out.print(i + ", ");
					
				}
				System.out.println();
				// Otherwise, find the docs corresponding to the logical
				// expression.
			} else {
				ResponseProducer rp = new ResponseProducer(domainAbbreviations, files);
				docs = rp.getDocs(input);
				System.out.println("Documents mathing the search expression: ");
				for (String i : docs) {
					System.out.print(i + ", ");
					
				}
				System.out.println();
			}
		}

	}

}
