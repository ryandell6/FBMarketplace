import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class MyBot {
    public MyBot() {
        String token = "MTIwNzY3MTc0NTcwNDc1NTMxMA.GKwAf9.5NhyvpbDfuCFzQHzwVB0W1wRYEnLh9VKzz4oX4"; // Replace this with your bot token
        String channelId = "1207675500508749867"; // Replace this with the ID of the channel you want to send the message to

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.addEventListeners(new MyListener(channelId));
        builder.setStatus(OnlineStatus.ONLINE);
	builder.setActivity(Activity.watching("The Marketplace"));
        builder.build();
    }
}