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
		
		AAA a = new AAA();
//		File file;
		try {
			file = a.getFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File[] files = file.listFiles();
		System.out.println(files[1]);
		
		// only outside IDE!!!
//		System.out.print("Enter something: ");
//		String input = System.console().readLine();
//		System.out.println(input);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter search item: ");
		String input = br.readLine();
		System.out.println(input);
		
		ResponseProducer rp = new ResponseProducer(domainAbbreviations, files);
		List<String> docs = new ArrayList<String>();
		
		docs = rp.getDocs(input);
		
	}
	
	
	
}
