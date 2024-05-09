import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Car {
	private String id,city,price,miles,url;
	
	// Setters
	public void setData(String id) {
		this.url = "https://www.facebook.com/marketplace/item/"+id;
		this.id = id.trim();
		getHtml();
		setCity();
		setPrice();
		setMiles();
	}
	private void setCity() {
		try {
			String line = getLineInTxt("Facebook Marketplace</title>","HTML.txt");
			while(line.contains("-")) {
				line = line.substring(line.indexOf("-")+1);
			}
			this.city = line.substring(0,line.indexOf("|")).trim();
			
			// FORMATTING
			while(city.length() < 29) {
				city=city+" ";
			}
		}catch(Exception e) {
			this.city = "ERROR - car.setCity()";
			e.printStackTrace();
		}
	}
	private void setPrice() { 
		try {
			String keyphrase = "\"listing_price\":{\"amount_with_offset\":\"",line = getLineInTxt(keyphrase,"HTML.txt");
			line = line.substring(line.indexOf(keyphrase)+keyphrase.length());
			this.price = line.substring(0,line.indexOf("\"")-2);
			if(this.price == null || this.price == "0") {
				throw new Exception();
			}
		}catch(Exception e) {
			try {
				Scanner scan = new Scanner(new File("HTML.txt"));
				String line = "", key = "amount_with_offset_in_currency";
				@SuppressWarnings("unused")
				int counter  = 1;
				while(scan.hasNextLine()) {
					line = scan.nextLine();
					if(line.contains(this.id)) {
						line = line.substring(line.indexOf(this.id));
						if(line.contains(key)){
							line = line.substring(line.indexOf(key)+key.length()+3);
							line = line.substring(0,line.indexOf("\"")-2);
							price = line.trim();
						}
					}
					counter++;
				}
			}catch(Exception e1) {
				this.price = "ERROR - car.setPrice()";
				e1.printStackTrace();
			}
		}
		// FORMATTING
		while(price != null && price.length() <= 6) {
			price=price+" ";
		}
					
		if(this.price != null) {  this.price = addCommas(price.trim()); }
	}
	private void setMiles() {
		try {
			String keyphrase = "\"MILES\",\"value\":",line = getLineInTxt(keyphrase,"HTML.txt");
			line = line.substring(line.indexOf(keyphrase)+keyphrase.length());
			this.miles = line.substring(0,line.indexOf("}")).trim();
			
		}catch(Exception e) {
			try {	// SPECIAL CASE
				String keyphrase = "vehicle_odometer_data",line = getLineInTxt(keyphrase,"HTML.txt");
				line = line.substring(line.indexOf(keyphrase)+keyphrase.length()+23);
				this.miles = line.substring(0,line.indexOf("}")).trim();
				
				// FORMATTING
				if(miles.length() < 1) {
					System.out.println("Flag1");
					miles = "Missing";
				} else while(miles.length() < 6) {
					miles=miles+" ";
				}
			} catch(Exception b) {
				this.miles = "Missing";
				//e.printStackTrace();
			}
		}
		// FORMATTING
		if(!miles.equals("Missing")) {
			while(miles.length() < 6) {
				miles=miles+" ";
			}
			miles = addCommas(miles.trim());
		}
	}
	
	// METHODS
	private String addCommas(String str) {
		try {
			// Convert string to number
			long number = Long.parseLong(str);
			// Format the number with commas
			DecimalFormat formatter = new DecimalFormat("#,###");
			String output = formatter.format(number);
			while(output.length() <= 5) {
				output+=" ";
			}
			return output;
		} catch (NumberFormatException e) {
			// Handle invalid input
			System.out.println("Invalid input: " + e.getMessage());
			return null;
		}
	}
	
	// GETTERS
	private void getHtml() {
		new HTMLFetcher(url,"HTML.txt");
	}
	private String getLineInTxt(String str, String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader("HTML.txt"))) {
        	String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(str)) {
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
	}
	public String getId() {
		return id;
	}
 	public String getCity() {
		return city;
	}	
	public String getPrice() {
		return price;
	}
	public String getMiles() {
		return miles;
	}
}
