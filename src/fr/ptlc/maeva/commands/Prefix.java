package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.MessageAndSave;
import fr.ptlc.maeva.data.Maeva;
import net.dv8tion.jda.api.entities.Guild;

public class Prefix {
    public MessageAndSave set(Maeva save, boolean isModo, String[] args, Guild guild) {
        if (!isModo)
            return new MessageAndSave("#cadena# Vous n'\u00eates pas administrateur serveur", save);
        if (args.length < 2)
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#prefix <nouveau[space]pr\u00e9fix>", save);
        String prefix = args[1];
        for (int i = 2; i < args.length; i++)
            prefix = String.valueOf(prefix) + " " + args[i];
        prefix = prefix.replace("[space]", " ");
        save.getData().getServerById(guild.getId()).setPrefix(prefix);
        return new MessageAndSave("Le pr\u00e9fix est d\u00e9sormais " + prefix + "\n     exemple : #prefix#help", save);
    }
}

