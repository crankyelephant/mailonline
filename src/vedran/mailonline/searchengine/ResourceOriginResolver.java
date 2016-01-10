package vedran.mailonline.searchengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ResourceOriginResolver {
	public File getFile() throws IOException {
////		File file = new File(getClass().getClassLoader().getResource("path_to_files.txt").getFile());
//		ClassLoader cl = getClass().getClassLoader();
//		URL r = cl.getResource("path_to_files.txt");
//		File pathFile = new File(r.getFile());
//		
//		BufferedReader br = new BufferedReader(new FileReader(pathFile));
		
		InputStream in = getClass().getResourceAsStream("/path_to_files.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String path = br.readLine();
		
		File file = new File(path);
		
		br.close();
		return file;
	}
}
