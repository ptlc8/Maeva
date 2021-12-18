package fr.ptlc.maeva.twitch;

import net.dv8tion.jda.api.entities.MessageChannel;

public class MessageChannelAndTwitchChat {
    MessageChannel messageChannel;
    String twitchChannel;

    public MessageChannelAndTwitchChat(MessageChannel messageChannel, String twitchChannel) {
        this.messageChannel = messageChannel;
        this.twitchChannel = twitchChannel;
    }

    public MessageChannel getMessageChannel() {
        return this.messageChannel;
    }

    public String getTwitchChannel() {
        return this.twitchChannel;
    }
}

