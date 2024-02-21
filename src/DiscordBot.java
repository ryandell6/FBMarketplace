import commands.CommandManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class DiscordBot {
private final ShardManager shardManager;
	
	public DiscordBot() {
		String token = "";
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
		builder.setStatus(OnlineStatus.ONLINE);
		builder.setActivity(Activity.watching("The Marketplace"));
		shardManager = builder.build();
		
		// Register Listeners
		shardManager.addEventListener(new ReadyEventListener(), new MessageEventListener(), new CommandManager());
		
		sendMessage();
	}
	
	// Post
	public void sendMessage() {
		
	}
	
	public ShardManager getShardManager() {
		return shardManager;
	}
}
