import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CarListener extends ListenerAdapter {
	public String carChannelId = "";
	private static TextChannel channel;
	
	public CarListener(String channelId) {
		this.carChannelId = channelId;
	}

	 public void onReady(ReadyEvent event) {
	        System.out.print("CarListener Bot is ready! - ");
	        channel = event.getJDA().getTextChannelById(carChannelId);
	        
	        if (channel == null) {
	            System.out.println("Channel not found with ID: " + carChannelId);
	        } else {
	        	System.out.println("Channel "+carChannelId+ " found.");
	        }
	}
	 
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Ignore messages from bots to prevent potential loops
        if (event.getAuthor().isBot()) {
            return;
        }
        // Check if the message is from the desired text channel
        TextChannel carChannel = event.getJDA().getTextChannelById(carChannelId);
        if (carChannel == null || !event.getChannel().getId().equals(carChannelId)) {
            return;
        }
    }
    
    public void sendMessage(String message) {
    	channel.sendMessage(message).queue();
    }
    
    public static TextChannel getChannel() {
    	return channel;
    }
}
