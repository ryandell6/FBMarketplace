import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEventListener extends ListenerAdapter {
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {	
		super.onMessageReceived(event);
		System.out.println(event.getMember()+" sent: ["+event.getMessage()+"] in ["+event.getChannel()+"]");	
		// Member:SneakieBot(id=637841877462417408, user=User:sneakiebot(id=637841877462417408), guild=Guild:FB-Cards(id=1207675500013559808)) 
		// sent: [ReceivedMessage(id=1207707468243927061, author=sneakiebot, content= ...)] in [TextChannel:general(id=1207675500508749867)]
		
	}

}
