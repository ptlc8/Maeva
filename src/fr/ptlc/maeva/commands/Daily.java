package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.Functions;
import fr.ptlc.maeva.MessageAndSave;
import fr.ptlc.maeva.data.Client;
import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.data.Role;
import fr.ptlc.maeva.data.Server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class Daily {
    public MessageAndSave get(Maeva save, Guild guild, User author, Message message) {
    	if (save.getData().isTimeToResetDailies(save.getOptions().getResetHour())) {
    		save.getData().resetAllSalaire();
    		System.out.println("Tous les utilisateurs, pouvant recevoir un salaire, peuvent désormais le récupérer");
    	}
        boolean canHaveSalaire = false;
        ArrayList<String> allSalaireRole = new ArrayList<String>();
        for (Guild g : author.getMutualGuilds())
            for (net.dv8tion.jda.api.entities.Role role : g.getMember(author).getRoles()) {
                if (!save.getData().isRegisteredRole(role.getId())) continue;
                canHaveSalaire = true;
                allSalaireRole.add(role.getId());
            }
        if (save.getData().isRegisteredRole("everyone")) {
            canHaveSalaire = true;
            allSalaireRole.add("everyone");
        }
        if (!canHaveSalaire)
            return new MessageAndSave("Désolé vos grades de chaque serveur ne vous permettent pas de recevoir de salaire", save);
        Server server = save.getData().getServerById(guild.getId());
        Client client = server.getClientById(author.getId());
        if (client == null)
            server.addClient(new Client(author.getId(), 0));
        if (client.hadTakeSalaire()) {
        	if (!message.getChannelType().equals(ChannelType.PRIVATE)) message.delete().complete();
            return new MessageAndSave(String.valueOf(author.getAsMention()) + ", vous avez déj\u00e0 reçu votre salaire... (prochain dans " + Functions.substractHourMinuteSeconde(save.getOptions().getResetHour(), 0, 0, Integer.parseInt(new SimpleDateFormat("HH").format(new Date())), Integer.parseInt(new SimpleDateFormat("mm").format(new Date())), Integer.parseInt(new SimpleDateFormat("ss").format(new Date())), "HHhmmmsss") + " )", save);
        }
        int gains = 0;
        for (String salaireId : allSalaireRole)
            gains += save.getData().getRoleById(salaireId).getSalaire();
        client.takeSalaire();
        client.addCredits(gains);
        if (!message.getChannelType().equals(ChannelType.PRIVATE)) message.delete().complete();
        return new MessageAndSave(String.valueOf(author.getAsMention()) + ", vous avez reçu votre salaire : " + gains + " crédits", save);
    }

    public String list(Maeva save, JDA jda) {
    	if (save.getData().getRoles().size() == 0)
    		return "Auncun salaire n'a été défini";
        String salairesList = "";
        for (Role role : save.getData().getRoles())
            salairesList = salairesList + "\n  - " + Functions.getRoleName(jda, role.getId()) + " -> " + role.getSalaire() + " crédits";
        return "Liste de tous les salaires : " + salairesList;
    }

    public MessageAndSave set(boolean isOp, Maeva save, String[] args, Message message, JDA jda) {
        String roleId;
        if (!isOp)
            return new MessageAndSave("#cadena# Vous n'êtes pas administrateur b0t", save);
        if (args.length < 3 || !Functions.isInt(args[2]))
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#daily set [salaire] <@grade|everyone>", save);
        if (message.getMentionedRoles().size() != 0)
            roleId = message.getMentionedRoles().get(0).getId();
        else if (args.length > 3 && args[3].equals("everyone"))
            roleId = "everyone";
        else
            return new MessageAndSave("#erreur# Erreur : Le rôle doit être mentionné ou `everyone`", save);
        if (!save.getData().isRegisteredRole(roleId))
            save.getData().addRole(new Role(roleId, Integer.parseInt(args[2])));
        else
            save.getData().getRoleById(roleId).setSalaire(Integer.parseInt(args[2]));
        return new MessageAndSave("Le rôle " + Functions.getRoleName(jda, roleId) + " a désormais un salaire de " + args[2] + " crédits", save);
    }

    public MessageAndSave remove(boolean isOp, Maeva save, Message message) {
        if (!isOp)
            return new MessageAndSave("#cadena# Vous n'êtes pas administrateur b0t", save);
        if (message.getMentionedRoles().size() == 0)
            return new MessageAndSave("Le rôle doit être mentionné", save);
        if (!save.getData().isRegisteredRole(message.getMentionedRoles().get(0).getId()))
            return new MessageAndSave("#erreur# Erreur : Le rôle " + message.getMentionedRoles().get(0).getName() + " n'a pas de salaire", save);
        save.getData().removeRole(message.getMentionedRoles().get(0).getId());
        return new MessageAndSave("Le salaire du rôle " + message.getMentionedRoles().get(0).getName() + " a été supprimé", save);
    }

    public MessageAndSave reset(boolean isOp, Maeva save) {
        if (!isOp)
            return new MessageAndSave("#cadena# Vous n'êtes pas administrateur b0t", save);
        save.getData().resetAllSalaire();
        return new MessageAndSave("Tous les utilisateurs, pouvant recevoir un salaire, peuvent désormais le récupérer.", save);
    }
}

