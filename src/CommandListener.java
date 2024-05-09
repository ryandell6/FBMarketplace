import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	public String settingsChannelId = "";
	private static TextChannel channel;
	
	public CommandListener(String channelId) {
		this.settingsChannelId = channelId;
	}

	 public void onReady(ReadyEvent event) {
	        System.out.print("   Settings Bot is ready! - ");
	        channel = event.getJDA().getTextChannelById(settingsChannelId);
	        
	        if (channel == null) {
	            System.out.println("Channel not found with ID: " + settingsChannelId);
	        } else {
	        	System.out.println("Channel "+settingsChannelId+ " found.");
	        }
	        
	        // Get latest settings message
	        TextChannel settingsChannel = event.getJDA().getTextChannelById(settingsChannelId);
	        
	        if (settingsChannel != null) {
	        	settingsChannel.retrieveMessageById(settingsChannel.getLatestMessageId()).queue(message -> {	 // Fetch the last message sent in the channel
	        		//System.out.println("Last message in the channel:\n" + urlData);
	        		saveUrlData(message.getContentDisplay());
	        	}, throwable -> {
	                System.out.println("Failed to fetch the last message: " + throwable.getMessage());
	            });
	        } else {
	            System.out.println("Channel not found!");
	        }
	}
	 
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Ignore messages from bots to prevent potential loops
        if (event.getAuthor().isBot()) {
            return;
        }
        // Check if the message is from the desired text channel
        TextChannel settingsChannel = event.getJDA().getTextChannelById(settingsChannelId);
        if (settingsChannel == null || !event.getChannel().getId().equals(settingsChannelId)) {
            return;
        }

        String message = event.getMessage().getContentRaw().toLowerCase();
        // delete messages in chunks of 100
        settingsChannel.getIterableHistory().forEach(messages -> {
        	settingsChannel.purgeMessages(messages);
        	try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); } // avoid rate limits
        });
        
        if (message.startsWith("/")) { // Commands start with "/"
            String[] command = message.substring(1).split(" "); // Remove "!" and split the command
            
            switch (command[0].toLowerCase()) {
        		case "search":
        			editUrl("Search:", message.substring(message.indexOf(" ")+1));
        			raiseUpdatedFlag();
        			break;
        		case "city":
        			editUrl("City:", message.substring(message.indexOf(" ")+1));
        			raiseUpdatedFlag();
        			break;
        		case "sort":
        			editUrl("Sort:", message.substring(message.indexOf(" ")+1));
        			raiseUpdatedFlag();
        			break;
                case "minprice":
                	if(isInteger(command[1])) { editUrl("MinPrice:", command[1]); } else { event.getChannel().sendMessage("> Command: \""+message+"\" must be a whole number").queue(); }
                	raiseUpdatedFlag();
                    break;
                case "maxprice":
                	if(isInteger(command[1])) { editUrl("MaxPrice:", command[1]); } else { event.getChannel().sendMessage("> Command: \""+message+"\" must be a whole number").queue(); }
                	raiseUpdatedFlag();
                    break;
                default:
                    event.getChannel().sendMessage("Unknown command: \""+message+"\"").queue();
            }
            updateSettingsOnDiscord();
        }
    }
    
    public void raiseUpdatedFlag() {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter("UpdatedFlag.txt"))) {
			writer.write("True");
			System.out.println("Updated UpdatedFlag.txt");
    	} catch (IOException e) {
    		System.out.println("An error occurred while updating the file.");
    	}
    }
    public void editUrl(String toEdit, String newValue) {
    	try {
    		String line = "", context = "";
			Scanner scan = new Scanner(new File("UrlData.txt"));
			while(scan.hasNextLine()) {
				line = scan.nextLine();
				if(line.contains(toEdit)) {
					// set the new value
					line = line.substring(0,line.indexOf(":"));
					line+=": "+newValue;
				}
				context+=line+"\n";
			}
			saveUrlData(context);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    public void saveUrlData(String contents) {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter("UrlData.txt"))) {
			writer.write(contents);
			System.out.println("Updated UrlData.txt");
    	} catch (IOException e) {
    		System.out.println("An error occurred while updating the file.");
    	}
    }
    
    public void updateSettingsOnDiscord() {
    	// update settings on discord
        String settings = "", fileName = "UrlData.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                settings+=line+"\n";
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        channel.sendMessage(settings).queue();
        System.out.println("Settings message updated via discord.");
    }
    public boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
        	System.out.println("Value: "+value+" could not be verified as a number.");
            return false;
        }
    }
    
    public static TextChannel getChannel() {
    	return channel;
    }
}
