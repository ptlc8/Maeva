package fr.ptlc.maeva.commands;

import net.dv8tion.jda.api.entities.Message;

public class Img {
    public String get(String[] args, Message message) {
        if (args.length < 2)
            return "#syntaxe# Syntaxe : #prefix#img <@quelqu'un>";
        if (message.getMentionedUsers().size() == 1)
        	return "#erreur# Erreur : L'utilisateur doit \u00eatre mentionn\u00e9e";
        return message.getMentionedUsers().get(0).getAvatarUrl();
    }
}

