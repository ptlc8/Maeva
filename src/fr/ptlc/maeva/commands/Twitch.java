package fr.ptlc.maeva.commands;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import fr.ptlc.maeva.BotListener;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Twitch {
	
	public static final String oauth = "oauth:cacl26ellpy0kmskpvgq9de415p93s";
	
    public String sync(boolean isModo, String[] args, Message message, MessageChannel messageChannel) {
        if (!isModo)
            return "#cadena# Vous n'\u00eates pas administrateur serveur";
        if (args.length < 3)
            return "#syntaxe# Syntaxe : #prefix#twitch sync <#twitch_channel>";
        if (!args[2].startsWith("#") && message.getMentionedChannels().size() == 0)
            return "#erreur# Erreur : Le nom de la chaîne Twitch doit commencer par `#`";
        String tv = args[2].startsWith("#") ? args[2] : "#" + message.getMentionedChannels().get(0).getName();
        BotListener.onTwitch.addSync(messageChannel, tv.toLowerCase());
        return "Si la chaîne Twitch " + tv + " existe, je l'ai rejoint et je transmettrai tous les messages.\nLes commandes sont désormais désactiver.\nPour que j'arr\u00eate dites tout simplement `STOP` en \u00e9tant administrateur serveur";
    }
    
    public String user(String[] args, Message message) {
    	if (args.length < 3)
            return "#syntaxe# Syntaxe : #prefix#twitch channel <#twitch_channel>";
    	if (!args[2].startsWith("#") && message.getMentionedChannels().size() == 0)
            return "#erreur# Erreur : Le nom de la chaîne Twitch doit commencer par `#`";
    	String tv = args[2].startsWith("#") ? args[2] : "#" + message.getMentionedChannels().get(0).getName();
    	PircBot pircBot = new PircBot() {
    		protected void onUserList(String channel, User[] users) {
    			message.getChannel().sendMessage("La chaîne " + channel + " est actuellement regardée par " + users.length + " utilisateurs").queue();
    			disconnect();
    		}
    		/*protected void onTopic(String channel, String topic) {
    			System.out.println("topic");
    		}*/
		};
		try {
			pircBot.connect("irc.twitch.tv", 6667, oauth);
		} catch (IOException | IrcException e) {
			return "erreur de co a twitch";
		}
		pircBot.setVerbose(true);
		pircBot.joinChannel(tv);
    	return "";
    }
}

