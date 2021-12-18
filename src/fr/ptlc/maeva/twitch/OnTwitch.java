package fr.ptlc.maeva.twitch;

import fr.ptlc.maeva.twitch.MessageChannelAndTwitchChat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.MessageChannel;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

public class OnTwitch extends PircBot {
    List<MessageChannelAndTwitchChat> syncs = new ArrayList<MessageChannelAndTwitchChat>();

    public OnTwitch(String oauth) {
        this.setName("MaevaOnTwitch");
        try {
            this.connect("irc.twitch.tv", 6667, oauth);
        }
        catch (IOException | IrcException e) {
            System.out.println("Erreur : Impossible de se connecter à Twitch");
        }
        this.setVerbose(false);
    }

    public void addSync(MessageChannel messageChannel, String twitchChannel) {
        this.joinChannel(twitchChannel);
        this.syncs.add(new MessageChannelAndTwitchChat(messageChannel, twitchChannel));
    }

    public void removeSync(MessageChannel messageChannel) {
        int i = 0;
        while (i < this.syncs.size()) {
            if (this.syncs.get(i).getMessageChannel().equals(messageChannel)) {
                this.syncs.remove(i);
            }
            ++i;
        }
    }

    public boolean isSync(MessageChannel messageChannel) {
    	//System.out.println(getChannels().length);
        int i = 0;
        while (i < this.syncs.size()) {
            if (this.syncs.get(i).getMessageChannel().equals(messageChannel)) {
                return true;
            }
            ++i;
        }
        return false;
    }

    public MessageChannelAndTwitchChat getSync(MessageChannel messageChannel) {
        int i = 0;
        while (i < this.syncs.size()) {
            if (this.syncs.get(i).getMessageChannel().equals(messageChannel)) {
                return this.syncs.get(i);
            }
            ++i;
        }
        return null;
    }

    public void send(MessageChannel messageChannel, String message) {
        int i = 0;
        while (i < this.syncs.size()) {
            if (this.syncs.get(i).getMessageChannel().equals(messageChannel)) {
                this.sendMessage(this.syncs.get(i).getTwitchChannel(), message);
            }
            ++i;
        }
    }
    
    public MessageChannel getMessageChannel(String channel) {
    	for (MessageChannelAndTwitchChat sync : syncs)
    		if (sync.getTwitchChannel().equals(channel))
    			return sync.getMessageChannel();
    	return null;
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
    	if (getMessageChannel(channel) != null)
    		getMessageChannel(channel).sendMessage("Sur Twitch, **" + sender + "** : " + message).queue();
    }
    
    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
    	if (getMessageChannel(channel) != null && sender.equals(getName()))
    		getMessageChannel(channel).sendMessage("J'ai rejoint la chaîne twitch " + channel).queue();
    }
}

