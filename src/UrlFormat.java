import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UrlFormat {
	private String url = "", locationCity = "Rancho Cordova";
	private boolean filterDayOld = true, sortByCreationTime = true, urlFlag = false;
	private String location, dayOld, sortBy, searchBar;
	private int minPrice, maxPrice, originalMinPrice, originalMaxPrice;
	
	public UrlFormat() {
		wait(5);
		setVariables();
		setUrl();
	}
	
	public void loop() {
		minPrice+=1;
		setUrl();
	}

	public void resetLoop(){
		minPrice = originalMinPrice;
	}
	
	private void setVariables() {
		String line, data = "";
		try (BufferedReader reader = new BufferedReader(new FileReader("UrlData.txt"))) {
           while ((line = reader.readLine()) != null) {
        	   data+=line+"\n";
	       }
		} catch (IOException e) {
	       System.out.println("An error occurred while reading the file.");
	       e.printStackTrace();
		}
		
		data = data.substring(data.indexOf("Search: "));
		this.searchBar = data.substring(data.indexOf("Search: ")+8,data.indexOf("\n")).trim();
		data = data.substring(data.indexOf("\n")+1);
		this.locationCity = data.substring(data.indexOf("City: ")+6,data.indexOf("\n")).trim();
		data = data.substring(data.indexOf("\n")+1);
		String filterString = data.substring(data.indexOf("Sort: ")+6,data.indexOf("\n")).trim();
		data = data.substring(data.indexOf("\n")+1);
		this.minPrice = Integer.valueOf(data.substring(data.indexOf("MinPrice: ")+10,data.indexOf("\n")).trim());
		data = data.substring(data.indexOf("\n")+1);
		this.maxPrice = Integer.valueOf(data.substring(data.indexOf("MaxPrice: ")+10,data.indexOf("\n")).trim());
		
		if(filterString.toLowerCase().trim().equals("newest first")) {
			sortByCreationTime = true;
		} else {
			sortByCreationTime = false;
		}
		
		//System.out.println("----- Found -----\nCity: ["+locationCity+"]\nCreationTime: ["+sortByCreationTime+"]\nsearchBar: ["+searchBar+"]\nminPrice: ["+minPrice+"]\nmaxPrice: ["+maxPrice+"]");
		format();
	}
	
	private void format() {
		location = setLocation();
		sortBy = setSortBy();
		dayOld = setDayOld();
		searchBar = setSearchBar();
	}
	
	private String setLocation() {
		switch(locationCity.toLowerCase()){
			case "rancho cordova":
				return "105598082806892";
			default: 
				return "";
		}
	}
	
	private String setDayOld() {
		if(filterDayOld) { 
			return "daysSinceListed=1";
		} else {
			return "";
		}
	}
	private String setSortBy() {
		if(sortByCreationTime) {
			return "sortBy=creation_time_descend";
		} else {
			return "";
		}
	}
	private String setSearchBar() {
		String newSearchBar = "query=";
		for(int x=0; x<searchBar.length(); x++) {
			if(searchBar.charAt(x) == ' ') {
				newSearchBar+="%20";
			} else {
				newSearchBar+=searchBar.charAt(x);
			}
		}
		
		return newSearchBar;
	}
	
	private void setUrl() { // https://www.facebook.com/marketplace/105598082806892/search?minPrice=1000&maxPrice=10000&daysSinceListed=1&sortBy=creation_time_descend&query=cars%20for%20sale&exact=false
		url = "https://www.facebook.com/marketplace/";
		String locationStr = location;
		String initialSearchStr = "/search?";
		String minPriceStr = formatUrlBit("minPrice="+minPrice);
		String maxPriceStr = formatUrlBit("maxPrice="+maxPrice);
		String dayOldStr = formatUrlBit(dayOld);
		String sortByStr = formatUrlBit(sortBy);
		String searchBarStr = formatUrlBit(searchBar);
		String finalPartStr = formatUrlBit("exact=false");
		
		url += locationStr+initialSearchStr+minPriceStr+maxPriceStr+dayOldStr+sortByStr+searchBarStr+finalPartStr;
		System.out.println("Gathering Cars From: ["+url+"]");
	}
	
	private String formatUrlBit(String bit) {
		String newBit="";
		if(bit!="" & !urlFlag) {
			newBit = bit;
			urlFlag = true;
		} else if(bit!="" & urlFlag) {
			newBit = "&"+bit;
		}
		return newBit;
	}
	
	public String getUrl() {
		return url;
	}
	
	public static void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			System.out.println("Error in wait()..");
		}
	}
}
