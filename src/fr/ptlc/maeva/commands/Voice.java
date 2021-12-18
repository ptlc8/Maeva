package fr.ptlc.maeva.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class Voice {
	
	public static String moveAll(boolean isModo, Guild guild, Message message, String[] args) {
		if (!isModo) return "#cadena# Tu n'es pas administrateur serveur";
		if (!message.getMember().getVoiceState().inVoiceChannel()) return "#erreur# Tu n'es pas en vocal";
		if (args.length < 2) return "#syntaxe# Tu dois préciser l'id du salon vocal de destination";
		if (guild.getVoiceChannelById(args[1]) == null) return "#erreur# Cet identifiant est invalide";
		if (isModo && message.getMember().getVoiceState().inVoiceChannel() && args.length > 1 && guild.getVoiceChannelById(args[1])!=null)
			for (Member m : message.getMember().getVoiceState().getChannel().getMembers())
				guild.moveVoiceMember(m, guild.getVoiceChannelById(args[1])).complete();
		return null;
	}
	
}
