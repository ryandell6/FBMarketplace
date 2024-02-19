import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

public class FBMarketplace {
	private LinkedList<String> ids = new LinkedList<String>();
	private String url = "", search = "cars for sale";
	private boolean newestFirst = true, lowestPrice = false;
	private int minPrice = 0, maxPrice = 10;
	
	public void run() {
		/*setFilters();				// Creates the url for getHTML()
		getHTML();					// Saves the data from the search to a file
		saveIDs();					// Saves all item IDs from the HTML into an array
		//printIDs();				// Print all IDs
		//parseAllData();			// Parses the data received by the hmtl
		
		if(checkForNewListing()) {	// Checks to see if there is a new listing, return true/false
			notifyForNewListing();	// Send a message with the listing information and a url
		}*/
		sendNotification();
	}

	public void setFilters() {
		String base = "https://www.facebook.com/marketplace", userID = "105598082806892";
		String searchBase = "search?", query = "query=", newestFirst = "", lowestPrice = "", price = "", endBase = "";
		
		// Filter Detection
		if(minPrice != -1 || maxPrice != -1) {
			price = "minPrice="+minPrice+"&maxPrice="+maxPrice+"&";
			endBase = "&exact=false";	
		}
		if(this.newestFirst) {
			newestFirst = "sortBy=creation_time_descend&query=";
			endBase = "&exact=false";
			query = "";
		} else if(this.lowestPrice) {
			newestFirst = "sortBy=price_ascend&query=";
			endBase = "&exact=false";
			query = "";
		}
		
		// Format Search Field
		String tempStr = "";
		for(int x = 0; x < search.length(); x++) {
			if(search.charAt(x)==' ') {
				tempStr+="%20";
			} else {
				tempStr+=search.charAt(x);
			}
		}
		search = tempStr;
		
		// Set URL / Print URL
		url = base+"/"+userID+"/"+searchBase+price+query+newestFirst+lowestPrice+search+endBase;
		System.out.println(url);
	}

	public void getHTML() {
		GetHTML html = new GetHTML();
		html.run(url);
	}

	public void saveIDs() {
		try {
			// Save IDs to linked list
			Scanner scan = new Scanner(new File("HTML.txt"));
			String line = "";
			while(scan.hasNextLine()) {
				line = scan.nextLine();
				while(line.contains("primary_listing_photo")) {
					line = line.substring(line.indexOf("primary_listing_photo")-30); // Trim to start of listing
					line = line.substring(line.indexOf("id\":")+5);					// Trim to ID
					ids.add(line.substring(0,line.indexOf("\"")));
					line = line.substring(line.indexOf("primary_listing_photo")+30); // Trim to start of listing
				}
			}		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void storeIDsToFile() {
		// Save IDs to file
		// DELETE PREVIOUS IDs FILE
		File fileName = new File("IDs.txt");
		FileWriter fooWriter;
		try {
			fooWriter = new FileWriter(fileName, false);
			fooWriter.write(" ");
			fooWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} // true to append
		try {
			PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter("IDs.txt", true)));
			StringBuilder sb = new StringBuilder();
			sb.append(ids);
			output.println(sb);
			output.close();
		} catch (Exception e) {
			System.out.println("\t\t >> Can't find item. [IDs.txt]");
		}
	}
	
	public void printIDs() {
		System.out.println("IDs Size: ["+ids.size()+"]");
		while(!ids.isEmpty()) {
			System.out.println("https://www.facebook.com/marketplace/item/"+ids.pop());
		}
	}
	
	public void parseAllData() {
		while(!ids.isEmpty()) {
			Car car = new Car();
			car.setData(ids.pop());
			System.out.println(car.toString());
		}
	}

	public boolean checkForNewListing() {
		try {
			getHTML();	// Get new listing IDs
			saveIDs();	// Save IDs to linked list
			
			// Compare new listings to old listings
			Scanner scan = new Scanner(new File("IDs.txt"));
			
			String line, id;
			while(scan.hasNextLine()) {
				line=scan.nextLine();
				while(!ids.isEmpty()) {
					id = ids.pop();
					if(!line.contains(id)){
						System.out.println("New ID: ["+id+"]");
						return true;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void notifyForNewListing() {
		storeIDsToFile();
		System.out.println("New Listing Detected!");
		sendNotification();
	}
	
	public void sendNotification() {
		
	}

}
