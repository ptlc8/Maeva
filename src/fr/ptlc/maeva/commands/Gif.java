package fr.ptlc.maeva.commands;

import fr.ptlc.giphyapi.Main;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class Gif {
	public String get(String[] args, User author, Message message) {
		if (args.length < 2)
			return "#syntaxe# Syntaxe : #prefix#gif <arguments de recherche>";
		String searchArgs = "";
		for (int i = 1; i < args.length; i++)
			searchArgs = String.valueOf(searchArgs) + args[i] + " ";
		if (!message.getChannelType().equals((Object)ChannelType.PRIVATE))
			message.delete().queue();
		return author.getAsMention() + ", r\u00e9sultat pour *" + searchArgs.trim() + "*\n" + Main.getRandomGifUrl(searchArgs, "CeYnxeJsQprThusdyN6nFh6LdxYkHyQz", 5);
	}
}