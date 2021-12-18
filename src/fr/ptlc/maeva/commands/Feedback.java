package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.Functions;
import fr.ptlc.maeva.values.UserProfession;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class Feedback {
    public String send(String[] args, User author, JDA jda, Message message) {
        if (args.length < 2)
            return "#syntaxe# Syntaxe : #prefix#feedback <commentaire>";
        String tmp = args[1];
        for (int i = 2; i < args.length; i++)
            tmp = tmp + " " + args[i];
        Functions.sendPrivate(jda, UserProfession.CREATOR, "#lettre# " + author.getName() + " : \"" + tmp + "\"");
        if (!message.isFromType(ChannelType.PRIVATE)) message.delete().queue();
        return "#lettre# Votre message a été envoyé au créateur";
    }
}

