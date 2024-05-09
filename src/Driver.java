import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Driver {
	// Resources
	static String locationCity, searchBar;
	static int minPrice, maxPrice;
	static boolean sortByCreationTime, filterDayOld;
	
	static MyBot bot = new MyBot();
	static UrlFormat url = new UrlFormat();		//new UrlFormat("Rancho Cordova", "cars for sale", 1000, 10000, true, true);
	static String itemDataFilePath = "Data.txt", urlStr = "";
	static IDChecker idChecker = new IDChecker(itemDataFilePath, bot);
	static LinkedList<String> ids = new LinkedList<String>();
	
	// Fields
	static int cooldown = 3; // in minutes
	static int increaseADallorLoop = 10;
	static boolean urlFlag = false;
	
	public static void main(String[] args) {
		//searchOneItem("3434880430135456");
		run();
    }
	
	public static void run() {
		while(true) {
			int loop = 1;
			while(loop <= increaseADallorLoop) {
				idChecker = new IDChecker(itemDataFilePath, bot);
				new HTMLFetcher(url.getUrl(),"HTML.txt");
				saveIDsToFile();
				url.loop();
				loop++;
			}
			System.out.println("Cooldown Initiated.");
			wait(cooldown);
		}
	}
	
	public static void searchOneItem(String id) {
		idChecker = new IDChecker(itemDataFilePath, bot);
		idChecker.checkID(id, true);
	}
	public static void saveIDsToFile() {
		try {
			Scanner scan = new Scanner(new File("HTML.txt"));
			String line = "";
			String id = "";
			while(scan.hasNextLine()) {
				line = scan.nextLine();
				while(line.contains("primary_listing_photo")) {
					line = line.substring(line.indexOf("primary_listing_photo")-30); // Trim to start of listing
					line = line.substring(line.indexOf("id\":")+5);	
					id = line.substring(0,line.indexOf("\"")).trim();
					// Add more checks here as needed | Checks if id exists, if not it adds it
					idChecker.checkID(id, false);
			        				 
					line = line.substring(line.indexOf("primary_listing_photo")+30); // Trim to start of listing
					if(checkForUpdatedSearch()) { break; }
				}
				if(checkForUpdatedSearch()) { break; }
			}
			if(checkForUpdatedSearch()) { run(); }
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static boolean checkForUpdatedSearch() {
		try {
			Scanner scan = new Scanner(new File("UpdatedFlag.txt"));
			String line = "";
			while(scan.hasNextLine()) {
				line = scan.nextLine();
				if(line.equals("True")) {
					try (BufferedWriter writer = new BufferedWriter(new FileWriter("UpdatedFlag.txt"))) {
						writer.write("False");
			    	} catch (IOException e) {
			    		System.out.println("An error occurred while updating the file.");
			    	}
					url = new UrlFormat();
					return true;
				} else {
					return false;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// TOOLS
	public static void wait(int minutes) {
		url.resetLoop();
		try {
			Thread.sleep(minutes * 1000 * 60);
		} catch (InterruptedException e) {
			System.out.println("Error in wait()..");
		}
	}
}
