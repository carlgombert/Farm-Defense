package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class TxtFileUtil {

	public static URL readFile(String file) {
		URL url = TxtFileUtil.class.getClassLoader().getResource(file);
		return url;
	}
	
	public static BufferedReader readURL(URL url) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error reading txt files");
		}
		return br;
	}
}
