import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IDChecker {
	private MyBot bot;
    private List<String> idList, nonCarList;
    private String idsFilePath, nonCarsFilePath = "NonCars.txt";
    private int carCounter = 1;

    public IDChecker(String filePath, MyBot bot) {
    	this.bot = bot;
        this.idsFilePath = filePath;
        idList = new ArrayList<>();
        nonCarList = new ArrayList<>();
        loadIDsFromFile();
        loadNotACarIDsFromFile();
    }
    // METHODS
    private void loadIDsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(idsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
            	line = line.substring(line.indexOf("ID: ")+4);
            	line = line.substring(0,line.indexOf("\t")).trim();
                idList.add(line);
            }
            carCounter = idList.size()+1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadNotACarIDsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(nonCarsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
            	line = line.trim();
            	nonCarList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void checkID(String id, boolean terminate) {
    	String num = formatCarCounter();
    	
    	// Found or not
    	if (idList.contains(id)) {
    		id = formatId(id);
    		System.out.println("\t" + id + " found in Data.txt.");
    	} else if(nonCarList.contains(id)) {
    		id = formatId(id);
    		System.out.println("\t" + id + " already determined wasn't a car");
   		} else if(verifyCar(id) == true) {
   			id = formatId(id);
   			System.out.print(num+". ID " + id + " not found in Data.txt. Saving item data to file... ");
   			saveItemDataToFile(id);
   			System.out.print("saved.");
   			sendDiscordMessage(getLatestItemFromFile());
   			carCounter++;
    	}
    	if(terminate) { System.exit(0); }
    }
    public void saveItemDataToFile(String id) {
    	if(id!=null) {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(idsFilePath, true))) {
				bw.write(getItemData(id));
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
	}
    public void saveNonCarToFile(String id) {
    	if(id!=null) {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(nonCarsFilePath, true))) {
				bw.write(id);
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
	}
    
    private void sendDiscordMessage(String message) {
    	String num="", id="", city="", price="", miles="", link="";
    	num = message.substring(0,message.indexOf("."));
    	id = message.substring(message.indexOf("ID: ")+("ID: ").length(),message.indexOf("City")).trim();
    	city = message.substring(message.indexOf("City: ")+("City: ").length(),message.indexOf("Price")).trim();
    	price = message.substring(message.indexOf("Price: ")+("Price: ").length(),message.indexOf("Miles")).trim();
    	miles = message.substring(message.indexOf("Miles: ")+("Miles: ").length(),message.indexOf("Link")).trim();
    	link = message.substring(message.indexOf("Link: ")+("Link: ").length()).trim();
    	
    	
    	bot.sendMessage("Car# "+num+" -- Link: "+link+"\n> ID: "+id+"\n> City: "+city+"\n> Price: $"+price+" -- Miles: "+miles, "cars");
    }
    
    // GETTERS
    private String getLatestItemFromFile() {
    	String lastItem = "";
    	try {
			Scanner scan = new Scanner(new File("Data.txt"));
			while(scan.hasNextLine()) {
				lastItem = scan.nextLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	return lastItem;
    }
	private String getItemData(String id) {
		Car car = new Car();
	    car.setData(id);    	
		return carCounter+".\tID: "+id+"\tCity: "+car.getCity()+"\tPrice: "+car.getPrice()+"\tMiles: "+car.getMiles()+"\tLink: https://www.facebook.com/marketplace/item/"+id;
	}
	// TOOLS
	private boolean verifyCar(String id) {
		 HTMLFetcher html = new HTMLFetcher("https://www.facebook.com/marketplace/item/"+id, "HTML.txt");
		 if(html.getLineCount() > 25 && html.contains("Cars &amp") && !html.contains("parts") && !html.contains("appointment")) {
			 return true;
		 } else {
			 id = formatId(id);
			 System.out.println("\t"+id+" - Not a car");
			 saveNonCarToFile(id);
			 nonCarList.add(id.trim());
			 return false;
		 }
	}
	public void terminate() {
		System.exit(0);
	}
	private String formatCarCounter() {
    	String num = String.valueOf(carCounter);
    	if(carCounter < 100) {
			num = "0"+num;
			if(carCounter < 10) {
				num = "0"+num;
			}
		}
    	return num;
    }
    private String formatId(String id) {
    	if(id.length() == 15) {
		   		id+=" ";
		}
    	return id;
    }
}