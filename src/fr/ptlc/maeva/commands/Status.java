package fr.ptlc.maeva.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;

public class Status {
	
    public static final String syntaxe = "#syntaxe# Syntaxe : #prefix#status <online|idle|do_not_disturb|invisible> <play|listen|stream|watch|custom> <nouveau statut>";

    public String set(boolean isOp, String[] args, JDA jda) {
    	if (!isOp)
    		return "#cadena# Vous n'êtes pas administrateur b0t";
    	if (args.length < 2)
    		return syntaxe;
    	OnlineStatus onlineStatus =
    		args[1].equals("online") ? OnlineStatus.ONLINE :
    		args[1].equals("idle") ? OnlineStatus.IDLE :
    		args[1].equals("do_not_disturb") ? OnlineStatus.DO_NOT_DISTURB :
    		args[1].equals("invisible") ? OnlineStatus.INVISIBLE :
    		null;
    	if (args.length < 4) {
    		jda.getPresence().setStatus(onlineStatus);
    		return "Statut de connexion mis à jour";
    	}
    	ActivityType gameType =
    		args[2].equals("play") ? ActivityType.DEFAULT :
    		args[2].equals("listen") ? ActivityType.LISTENING :
    		args[2].equals("stream") ? ActivityType.STREAMING :
    		args[2].equals("watch") ? ActivityType.WATCHING :
    		args[2].equals("custom") ? ActivityType.CUSTOM_STATUS :
    		null;
    	if (onlineStatus == null || gameType == null)
    		return syntaxe;
    	if (gameType == ActivityType.CUSTOM_STATUS) return "Les statuts custom sont actuellement indisponibles :/";
    	String statut = args[3];
    	for (int i = 4; i < args.length; i++)
    		statut += " " + args[i];
    	jda.getPresence().setPresence(onlineStatus, Activity.of(gameType, statut));
    	switch (gameType) {
    	case DEFAULT:
    		return "Joue à " + statut;
		case LISTENING:
			return "Ecoute " + statut;
		case STREAMING:
			return "En stream " + statut;
		case WATCHING:
			return "Regarde " + statut;
		case CUSTOM_STATUS:
			return statut;
		default:
			return "Erreur : gameType inconnu";
    	}
    }
}

