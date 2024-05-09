import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

public class GetHTML {	
	// HTML
	public void run(String link) {
		// DELETE PREVIOUS HTML FILE
		File fileName = new File("DiscordHTML.txt");
		FileWriter fooWriter;
		try {
			fooWriter = new FileWriter(fileName, false);
			fooWriter.write(" ");
			fooWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} // true to append

		// GET HTML
		BufferedReader br = null;
		try {
			URL url = new URL(link);
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
			//if (line.contains("POST")) {
				sb.append(line);
				sb.append(System.lineSeparator());
			//}
			}
			PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
			output.println(sb);
			output.close();
			br.close();
		} catch (Exception e) {
			System.out.println("\t\t >> Can't find item. [" + link + "]");
		}
		
	}

}
