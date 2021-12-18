package fr.ptlc.maeva.commands;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import fr.ptlc.maeva.Functions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Say {
	public String say(Message message, String[] args) {
		if (args.length <= 1)
			return "#syntaxe# Syntaxe : #prefix#say <texte>";
		String msg = "";
		for(int i = 1; args.length > i; i++)
			msg = String.valueOf(msg) + " " + args[i];
		if (!message.getChannelType().equals(ChannelType.PRIVATE))
			message.delete().complete();
		return msg;
	}
	
	public static String schedule(Message message, String[] args) {
		if (args.length <= 2)
			return "#syntaxe# Syntaxe : #prefix#schedule [minutes] <texte>";
		if (!Functions.isLong(args[1]))
			return "#Syntaxe# Le nombre de minutes soit être un nombre";
		String msg = "";
		for(int i = 2; args.length > i; i++)
			msg = String.valueOf(msg) + " " + args[i];
		if (!message.getChannelType().equals(ChannelType.PRIVATE))
			message.delete().complete();
		message.getChannel().sendMessage(msg).queueAfter(Long.parseLong(args[1]), TimeUnit.MINUTES);
		return "";
	}
	
	public static MessageEmbed embed(boolean isModo, Message message, String[] args) {
		EmbedBuilder eb = new EmbedBuilder();
		if (args.length < 3/* || !args[2].startsWith("\"")*/) {
			eb.setTitle("Syntaxe :");
			eb.setDescription("embed <couleur> <titre>\n<description>");
			return eb.build();
		}
		Color color = args[1].equalsIgnoreCase("black") ? Color.black
				: args[1].equalsIgnoreCase("blue") ? Color.blue
				: args[1].equalsIgnoreCase("cyan") ? Color.cyan
				: args[1].equalsIgnoreCase("darkgray") ? Color.darkGray
				: args[1].equalsIgnoreCase("gray") ? Color.gray
				: args[1].equalsIgnoreCase("green") ? Color.green
				: args[1].equalsIgnoreCase("lightgray") ? Color.lightGray
				: args[1].equalsIgnoreCase("magenta") ? Color.magenta
				: args[1].equalsIgnoreCase("orange") ? Color.orange
				: args[1].equalsIgnoreCase("pink") ? Color.pink
				: args[1].equalsIgnoreCase("red") ? Color.red
				: args[1].equalsIgnoreCase("white") ? Color.white
				: args[1].equalsIgnoreCase("yellow") ? Color.yellow
				: null;
		if (color == null) {
			eb.setTitle("Couleur invalide");
			eb.setDescription("black, blue, cyan, darkgray, gray, green, lightgray, magenta, orange, pink, red, white, yellow");
			return eb.build();
		}
		eb.setColor(color);
		/*String title = "";
		int i = 2;
		do {
			title += args[i].replace("\"", "") + " ";
			i++;
		} while (!args[i-1].endsWith("\"") && i < args.length);
		String description = "";
		for (; i < args.length; i++) {
			description += args[i] + " ";
		}*/
		String titleDescription = message.getContentRaw().replaceFirst(".+embed [a-zA-Z]+ ", "");
		String title = titleDescription.split("\n")[0].trim();
		String description = titleDescription.substring(title.length()).trim();
		eb.setTitle(title);
		eb.setDescription(description);
		message.delete().complete();
		return eb.build();
	}
}