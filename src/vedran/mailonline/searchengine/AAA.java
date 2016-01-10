package vedran.mailonline.searchengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class AAA {
	public File getFile() throws IOException {
//		File file = new File(getClass().getClassLoader().getResource("path_to_files.txt").getFile());
		ClassLoader cl = getClass().getClassLoader();
		URL r = cl.getResource("path_to_files.txt");
		File pathFile = new File(r.getFile());
		
		BufferedReader br = new BufferedReader(new FileReader(pathFile));
		String path = br.readLine();
		
		File file = new File(path);
		
		br.close();
		return file;
	}
}
