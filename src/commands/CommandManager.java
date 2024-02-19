package commands;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class CommandManager extends ListenerAdapter{
	
	
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		String command = event.getName();	// Get command and check if it equals something specific
		if(command.equals("welcome")) {
			String userTag = event.getUser().getAsTag();
			event.reply("welcome to the sever, **"+userTag+"**!").queue();
		}
	}
	
	// Guild command
	public void onGuildReady(@NotNull GuildReadyEvent event) {
		List<CommandData> commandData = new ArrayList<CommandData>();
		commandData.add(Commands.slash("welcome", "description here"));
		event.getGuild().updateCommands().addCommands(commandData).queue();
	}
}
