import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Car {
	// Location, ID(url), Price, Year, Make, Model, Miles -- URL -> "https://www.facebook.com/marketplace/item/"+ID
	private String ID, price, year, data ="", make, model, location, milage, link;
	private int carNumber=1;
	
	public void setData(String id) {
		this.ID = id;
		GetHTML html = new GetHTML();
		html.run("https://www.facebook.com/marketplace/item/"+id);
		HTMLtoString();	// Stores the data from HTML to the 'data' String
		String price=data, basicInfo = data, location=data, milage=data;
		
		// GET DATA FROM ITEM
					basicInfo = basicInfo.substring(basicInfo.indexOf("<meta property=\"og:title\" content=\"")+35);	
		this.year = checkDataCharacters(basicInfo.substring(0,basicInfo.indexOf(" ")));
					basicInfo = basicInfo.substring(basicInfo.indexOf(" ")+1);
		this.make = checkDataCharacters(basicInfo.substring(0,basicInfo.indexOf(" ")));
					basicInfo = basicInfo.substring(basicInfo.indexOf(" ")+1);			// Trim to Model
		this.model = checkDataCharacters(basicInfo.substring(0,basicInfo.indexOf(" ")));	
					location = location.substring(location.indexOf(", California")-20);	// Trim to Location
					location = location.substring(location.indexOf("-")+2);
		this.location = checkDataCharacters(location.substring(0,location.indexOf("|")-1));
					price = price.substring(price.indexOf("\",\"currency\":\"USD\",\"amount\":\"")-80);	// Trim to Price
					price = price.substring(price.indexOf("$"));
		this.price = checkDataCharacters(price.substring(0,price.indexOf("\"")));		
					milage = milage.substring(milage.indexOf("\"unit\":\"MILES\",\"value\":")+23);		// Trim to Miles
		this.milage = checkDataCharacters(milage.substring(0,milage.indexOf("}")));
		
		//System.out.println("Location: ["+milage+"]");
		
		link="https://www.facebook.com/marketplace/item/"+ID;
	}	
	
	private String checkDataCharacters(String data) {
		String output = "";
		for(int x = 0; x < data.length(); x++) {
			if(data.charAt(x) != '\"') {
				output+=data.charAt(x);
			}
		}
		return output;
	}

	private void HTMLtoString() {
		try {
			Scanner scan = new Scanner(new File("HTML.txt"));
			while(scan.hasNextLine()) {
				data += scan.nextLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private void saveNewHTML() {
		try {
			FileWriter writerObj = new FileWriter("HTML.txt", false);
			writerObj.write(data);
			writerObj.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		//System.out.println("Data: ["+data+"]");
		return "Car #"+carNumber+" -- Link: "+link+"\n> ID: ["+ID+"]\n> Year:    ["+year+"]\n> Make:  ["+make+"]\n> Model:["+model+"]\n> City:    ["+location+"]\n> Price:  ["+price+"]\n> Miles:  ["+milage+"]";
	}
}