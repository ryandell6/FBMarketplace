import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class MyBot {	
	private String tokenURL = "https://www.reddit.com/user/LeadExtreme5159/comments/1az3ma8/code", tokenInitiator = "MTI";
	
    public MyBot() {
        JDABuilder builder = JDABuilder.createDefault(getToken());
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.addEventListeners(new CommandListener("1209498557564977182")); 
        builder.addEventListeners(new CarListener("1225583021516914708")); 
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("The Marketplace"));
        builder.build();
    }
    
    public void sendMessage(String message, String category) {
    	TextChannel channel = null;
    	if(category.equals("settings")) {
    		channel = CommandListener.getChannel();
    		channel.sendMessage(message).queue();
    		System.out.println(" - DiscordMessage sent successfully.");
    	} else if(category.equals("cars")) {
    		channel = CarListener.getChannel();
    		channel.sendMessage(message).queue();
    		System.out.println(" - DiscordMessage sent successfully.");
    	} else {
    		System.out.println(" - DiscordMessage ** NOT ** sent successfully.");
    	}
    }
    
    private String getToken() {
    	GetHTML html = new GetHTML();	// Get the page the tokens hosted on
    	html.run(tokenURL);
    	String token = "null", line = "";
    	boolean found = false;
    	
    	try {
			Scanner scan = new Scanner(new File("DiscordHTML.txt"));
			while(scan.hasNextLine()) {
				line = scan.nextLine();
				if(line.contains(tokenInitiator)) {
					token = line.trim();
					found = true;
				}
			}
			
			if(!found) {
				System.out.println("Token Not Found: ["+line.trim()+"]");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	   	
    	return token;
    }
}
