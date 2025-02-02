package fr.ptlc.maeva.commands;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import fr.ptlc.maeva.BotListener;
import fr.ptlc.maeva.Tokens;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Twitch {
	
    public String sync(boolean isModo, String[] args, Message message, MessageChannel messageChannel) {
        if (!isModo)
            return "#cadena# Vous n'\u00eates pas administrateur serveur";
        if (args.length < 3)
            return "#syntaxe# Syntaxe : #prefix#twitch sync <#twitch_channel>";
        if (!args[2].startsWith("#") && message.getMentionedChannels().size() == 0)
            return "#erreur# Erreur : Le nom de la cha�ne Twitch doit commencer par `#`";
        String tv = args[2].startsWith("#") ? args[2] : "#" + message.getMentionedChannels().get(0).getName();
        BotListener.onTwitch.addSync(messageChannel, tv.toLowerCase());
        return "Si la cha�ne Twitch " + tv + " existe, je l'ai rejoint et je transmettrai tous les messages.\nLes commandes sont d�sormais d�sactiver.\nPour que j'arr\u00eate dites tout simplement `STOP` en \u00e9tant administrateur serveur";
    }
    
    public String user(String[] args, Message message) {
    	if (args.length < 3)
            return "#syntaxe# Syntaxe : #prefix#twitch channel <#twitch_channel>";
    	if (!args[2].startsWith("#") && message.getMentionedChannels().size() == 0)
            return "#erreur# Erreur : Le nom de la cha�ne Twitch doit commencer par `#`";
    	String tv = args[2].startsWith("#") ? args[2] : "#" + message.getMentionedChannels().get(0).getName();
    	PircBot pircBot = new PircBot() {
    		protected void onUserList(String channel, User[] users) {
    			message.getChannel().sendMessage("La cha�ne " + channel + " est actuellement regard�e par " + users.length + " utilisateurs").queue();
    			disconnect();
    		}
    		/*protected void onTopic(String channel, String topic) {
    			System.out.println("topic");
    		}*/
		};
		try {
			pircBot.connect("irc.twitch.tv", 6667, Tokens.TWITCH);
		} catch (IOException | IrcException e) {
			return "erreur de co a twitch";
		}
		pircBot.setVerbose(true);
		pircBot.joinChannel(tv);
    	return "";
    }
}

