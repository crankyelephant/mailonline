package vedran.mailonline.searchengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DocumentRank {

	private Map<String, String> domainAbbreviations = new HashMap<String, String>();
	private File[] directories;
	private int N; // number of documents
	
	private Map<Integer, ArrayList<String>> tf = new HashMap<Integer, ArrayList<String>>();
//	private List<Integer> tf = new ArrayList<Integer>();
	private int idf = 0;

	public DocumentRank(Map<String, String> domainAbbreviations, File[] files) {
		this.domainAbbreviations = domainAbbreviations;
		this.directories = files;
		
		for (File directory : directories) {
			N += directory.listFiles().length;
		}
		
	}

	// response as a list of doc names
	private List<String> response = new ArrayList<String>();
	
	

	
	public List<String> getRankedDocs(String input) throws FileNotFoundException {
		input = input.toLowerCase();
		for (File directory : directories) {
			for (File file : directory.listFiles()) {
								
				
				
				Scanner scanner = new Scanner(file);
				scanner.useDelimiter("\\Z");
				String text = scanner.next().toLowerCase();
				
				int n = 0; // frequency of the term in the doc
				StringTokenizer tokens = new StringTokenizer(text);
				while(tokens.hasMoreTokens()) {
					if (input.equals(tokens.nextToken())) {
						n += 1;
					}
				}
				
				if (!tf.containsKey(n)) {
					tf.put(n, new ArrayList<String>());
				}
				
				tf.get(n).add(file.getName());
				
				if (n > 0) {
					idf += 1;
				}
				
				scanner.close();
			}
		}
		
		Integer[] tfValues = (Integer[]) (tf.keySet().toArray(new Integer[tf.keySet().size()]));
		Arrays.sort(tfValues);
		
		for (int i = tfValues.length-1; i > 0 ; i--) {
			for (String fileName : tf.get(tfValues[i])) {
				response.add(fileName);
			}
		}
		
		return response;
	}

}
