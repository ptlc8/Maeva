package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.MessageAndSave;
import fr.ptlc.maeva.data.Maeva;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;

public class Admin {
    public String list(Maeva save, JDA jda) {
        if (save.getData().getOps().size() < 1)
            return "#debloque# Aucun administrateur b0t n'est enregistr\u00e9, rajoutez en via le fichier `data.json`";
        String adminList = "";
        for (int i = 0; i < save.getData().getOps().size(); i++)
            adminList = jda.getUserById(save.getData().getOps().get(i)) == null ? String.valueOf(adminList) + ", " + save.getData().getOps().get(i) : String.valueOf(adminList) + ", " + jda.getUserById(save.getData().getOps().get(i)).getName();
        return "#debloque# Administrateurs b0t actuels : " + adminList.replaceFirst(",", "");
    }

    public MessageAndSave add(Maeva save, boolean isOp, String[] args, Message message) {
        if (!isOp)
            return new MessageAndSave("#cadena# Vous n'\u00eates pas administrateur b0t", save);
        if (args.length < 3)
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#admin add <@nouvelAdmin>", save);
        if (message.getMentionedUsers().size() == 0)
            return new MessageAndSave("#erreur# Erreur : Le nouvel aministrateur b0t doit \u00eatre mentionn\u00e9", save);
        String futureAdminId = message.getMentionedUsers().get(0).getId();
        String futureAdmin = message.getMentionedUsers().get(0).getName();
        if (save.getData().isOp(futureAdminId))
            return new MessageAndSave("#erreur# Erreur : l'utilisateur " + futureAdmin + " est d\u00e9j\u00e0 administrateur b0t", save);
        save.getData().getOps().add(futureAdminId);
        return new MessageAndSave("#debloque# L'utilisateur " + futureAdmin + " est d\u00e9sormais administrateur b0t", save);
    }

    public MessageAndSave remove(Maeva save, boolean isOp, String[] args, Message message, JDA jda) {
        if (!isOp)
            return new MessageAndSave("#cadena# Vous n'\u00eates pas administrateur b0t", save);
        if (args.length < 3)
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#admin remove <@exAdmin>", save);
        if (message.getMentionedUsers().size() == 0)
            return new MessageAndSave("#erreur# Erreur : L'ex-administrateur b0t doit \u00eatre mentionn\u00e9", save);
        String exAdminId = message.getMentionedUsers().get(0).getId();
        String exAdmin = message.getMentionedUsers().get(0).getName();
        if (!save.getData().isOp(exAdminId))
            return new MessageAndSave("#erreur# Erreur : l'utilisateur " + exAdmin + " n'est pas administrateur b0t", save);
        save.getData().getOps().remove(exAdminId);
        return new MessageAndSave("#debloque# L'utilisateur " + exAdmin + " n'est plus administrateur b0t", save);
    }
}

