import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MyListener extends ListenerAdapter {
    private final String channelId;
    private int timer = 8;

    public MyListener(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Bot is ready!");
        TextChannel channel = event.getJDA().getTextChannelById(channelId);
        String message = "null in onReady()";
        
        if (channel != null) {
        	FBMarketplace fb = new FBMarketplace();
    		while(true) {
    			for(int x = 0; x < timer; x++) {
    				System.out.print(".");
    				wait(1);
    			}
    			System.out.println("\nEnd of wait.");
    			fb.run();
				if(fb.message!=null) {
					sendMessage(channel, fb.message);
					fb.resetMessage();
				}
    		} 
        } else {
            System.out.println("Channel not found with ID: " + channelId);
        }
    }
    
    private void sendMessage(TextChannel channel, String message) {
    	channel.sendMessage(message).queue();
        System.out.println("Message sent successfully.");
    }
    
    private static void wait(int seconds) {
		try {
			Thread.sleep(Math.round(seconds * 1000.00));
		} catch (InterruptedException e) {
			System.out.println("Error in MyListener -> wait(int) method.");
		}
	}
}