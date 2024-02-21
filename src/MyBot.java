import net.dv8tion.jda.api.JDABuilder;

public class MyBot {
    public static void main(String[]args) {
        String token = "MTIwNzY3MTc0NTcwNDc1NTMxMA.GbfQiy.GpdGb0YYNraXnkzOBbCjCli0CzuDPM4ZKRw8Ts"; // Replace this with your bot token
        String channelId = "1207675500508749867"; // Replace this with the ID of the channel you want to send the message to

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.addEventListeners(new MyListener(channelId));
        builder.build();
    }
    
    // remove the test messages x2
    // remove it worked message
    // format message
    // send message via discord
}