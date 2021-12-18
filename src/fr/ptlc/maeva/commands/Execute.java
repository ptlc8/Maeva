package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.Commands;
import fr.ptlc.maeva.MessageAndSave;
import fr.ptlc.maeva.data.Maeva;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Execute {
    public MessageAndSave as(boolean isOp, Maeva save, String[] args, MessageReceivedEvent event, User author, Message message, JDA jda, MessageChannel messageChannel, Guild guild) {
        if (!isOp)
            return new MessageAndSave("#cadena# Vous n'êtes pas administrateur b0t", save);
        if (args.length < 2 || message.getMentionedUsers().size() == 0)
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#as <@utilisateur> <commande>", save);
        String commande = args[2];
        for (int i = 3; i < args.length; i++)
            commande += " " + args[i];
        User target = message.getMentionedUsers().get(0);
        //problème de mantions d'utilisateurs à décaler de 1
        System.out.println(commande);
        return new MessageAndSave("", Commands.command(save, commande.split(" "), event, target, message, jda, messageChannel, guild));
    }
}

