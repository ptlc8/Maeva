package fr.ptlc.maeva.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import fr.ptlc.maeva.MessageAndSave;
import fr.ptlc.maeva.data.Maeva;

public class AntiSpam {

	public static MessageAndSave set(Maeva save, boolean isModo, Guild guild, Message message, String[] args) {
		if (!isModo)
			return new MessageAndSave("#cadena# Vous n'êtes pas administrateur serveur", save);
		if (!message.getContentRaw().contains("/") || message.getMentionedChannels().isEmpty()
				|| message.getMentionedRoles().isEmpty() || message.getEmotes().size() < 2)
			return new MessageAndSave(!message.getContentRaw().contains("/") ? "Il manque un ***/***"
					: message.getMentionedChannels().isEmpty() ? "Aucun salon n'a été mentionné"
					: message.getMentionedRoles().isEmpty() ? "Aucun role n'a été mentionné"
					: "Il faut au moins 2 emotes custom"
				+ "\n#syntaxe# Syntaxe : #prefix#antispam <#salon d'accueil> <@role bloquant> <:emote_debloquant:> ***/*** <requête super cool avec :emote2: :emote3: etc...>", save);
		String request = "";
		int i;
		for (i = 1; i < args.length && !args[i].contains("/"); i++);
		for (i++; i < args.length; i++)
			request += args[i] + " ";
		String[] emotes = new String[message.getEmotes().size() - 1];
		for (int j = 1; j < message.getEmotes().size(); j++)
			emotes[j - 1] = message.getEmotes().get(j).getName();
		fr.ptlc.maeva.data.Server server = save.getData().getServerById(guild.getId());
		server.setAntiSpam(request, message.getMentionedChannels().get(0).getId(), message.getMentionedRoles().get(0).getId(), message.getEmotes().get(0).getName(), emotes);
		String back = "Le mode antispam a été activé.\n"
				+  "J'attribuerai désormais le rôle " + guild.getRoleById(server.getAntiSpamRoleId()).getAsMention()
				+ " aux nouveaux membres, et je leur indiquerais la requête suivante : \"" + server.getAntiSpamRequest()
				+ "\" dans le salon " + guild.getTextChannelById(server.getAntiSpamChannelId()).getAsMention()
				+ ".\n"
				+ "Pour réelement rejoindre le serveur le nouveau membre doit réagir avec l'emote "
				+ guild.getEmotesByName(server.getAntiSpamEmote(), false).get(0).getAsMention()
				+ " sur cette requête, ainsi celle-ci doit pouvoir mener à cette action.\n"
				+ "Les autres emotes ";
		for (String emote : server.getAntiSpamEmotes())
			back += guild.getEmotesByName(emote, false).get(0).getAsMention() + " ";
		back += "sont des leurres antispam. :D";
		return new MessageAndSave(back, save);
	}

}
