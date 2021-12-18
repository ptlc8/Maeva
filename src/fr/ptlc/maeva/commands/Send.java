package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.Functions;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class Send {
    public String send(boolean isOp, String[] args, Message message, JDA jda) {
        if (!isOp)
            return "#cadena# Vous n'êtes pas administrateur b0t";
        if (args.length < 2 || message.getMentionedUsers().size() == 0)
            return "#syntaxe# Syntaxe : #prefix#send <@utilisateur> <message super cool>";
        String texte = args[2];
        for(int i = 3; i < args.length; i++)
            texte = String.valueOf(texte) + " " + args[i];
        User target = message.getMentionedUsers().get(0);
        if (!message.getChannelType().equals((Object)ChannelType.PRIVATE))
            message.delete().complete();
        Functions.sendPrivate(target, texte);
        return "#lettre# Le message a bien été envoyé à " + message.getMentionedUsers().get(0).getName();
    }
}

