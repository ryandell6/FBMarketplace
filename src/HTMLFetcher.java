import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

public class HTMLFetcher {	
	public String url = "", filePath = "";
	
	public HTMLFetcher(String url, String filePath) {
		this.url = url;
		this.filePath = filePath;
		fetchAndSaveHTML();
	}
	
	// HTML
	public void fetchAndSaveHTML() {
		// DELETE PREVIOUS HTML FILE
		File fileName = new File(filePath);
		FileWriter fooWriter;
		try {
			fooWriter = new FileWriter(fileName, false);
			fooWriter.write(" ");
			fooWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} // true to append

		getHTML(url);
	}
	
	public void getHTML(String link) {
		// GET HTML
		File fileName = new File(filePath);
		BufferedReader br = null;
		try {
			URL url = new URL(link);
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
			}
			PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(fileName, false)));
			output.println(sb);
			output.close();
			br.close();
			//System.out.println("saved: "+link);
		} catch (Exception e) {
			System.out.println("\t\t >> Can't find item. [" + link + "]");
		}
	}
	
	public int getLineCount() {
		int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("HTML.txt"))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return count;
	}
	
	public boolean contains(String keyPhrase) {
		boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("HTML.txt"))) {
        	String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(keyPhrase)) {
                    found = true;
                }
            }
            return found;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
	}
}