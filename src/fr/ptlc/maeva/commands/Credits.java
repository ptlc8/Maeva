package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.Functions;
import fr.ptlc.maeva.MessageAndSave;
import fr.ptlc.maeva.data.Client;
import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.data.Server;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class Credits {
    public MessageAndSave add(Maeva save, boolean isOp, String[] args, Message message, Guild guild, User author) {
        if (!isOp)
            return new MessageAndSave("#cadena# Vous n'\u00eates pas administrateur b0t", save);
        if (args.length < 3)
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#add [nombre] <@client>", save);
        if (message.getMentionedUsers().size() == 0)
            return new MessageAndSave("#erreur# Erreur : Le client doit \u00eatre mentionn\u00e9", save);
        if (args.length < 3)
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#add [nombre] " + message.getMentionedUsers().get(0).getAsMention(), save);
        if (!Functions.isInt(args[1]))
        	return new MessageAndSave("#syntaxe# Syntaxe : #prefix#add [nombre] " + message.getMentionedUsers().get(0).getAsMention(), save);
        Server server = save.getData().getServerById(guild.getId());
        if (server.getClientById(message.getMentionedUsers().get(0).getId()) != null) {
        	server.getClientById(message.getMentionedUsers().get(0).getId()).addCredits(Integer.valueOf(args[1]));
        	return new MessageAndSave(message.getMentionedUsers().get(0).getAsMention() + " a re\u00e7u " + args[1] + " cr\u00e9dits, il en poss\u00e8de d\u00e9sormais " + server.getClientById(message.getMentionedUsers().get(0).getId()).getCredits() + " cr\u00e9dits", save);
        }
        server.addClient(new Client(author.getId(), Integer.parseInt(args[1])));
        return new MessageAndSave(message.getMentionedUsers().get(0).getAsMention() + " fait d\u00e9sormais partie des clients avec " + args[1] + " cr\u00e9dits", save);
    }

    public MessageAndSave remove(Maeva save, Guild guild, boolean isOp, String[] args, Message message, User author) {
        if (!isOp)
            return new MessageAndSave("#cadena# Vous n'\u00eates pas administrateur b0t", save);
        if (args.length < 3)
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#remove [nombre] <@client>", save);
        if (message.getMentionedUsers().size() == 0)
            return new MessageAndSave("#erreur# Erreur : Le client doit \u00eatre mentionn\u00e9", save);
        if (args.length < 3)
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#remove [nombre] " + message.getMentionedUsers().get(0).getAsMention(), save);
        if (!Functions.isInt(args[1]))
        	return new MessageAndSave("#syntaxe# Syntaxe : #prefix#remove [nombre] " + message.getMentionedUsers().get(0).getAsMention(), save);
        Server server = save.getData().getServerById(guild.getId());
        Client client = server.getClientById(message.getMentionedUsers().get(0).getId());
        if (client != null) {
        	client.removeCredits(Integer.valueOf(args[1]));
        	return new MessageAndSave(message.getMentionedUsers().get(0).getAsMention() + " a perdu " + args[1] + " cr\u00e9dits, il en poss\u00e8de d\u00e9sormais " + client.getCredits() + " cr\u00e9dits", save);
        }
        server.addClient(new Client(author.getId(), Integer.parseInt(args[1])));
        return new MessageAndSave(message.getMentionedUsers().get(0).getAsMention() + " fait d\u00e9sormais partie des clients avec " + (- Integer.parseInt(args[1])) + " cr\u00e9dits", save);
    }
    
    public String howmany(Maeva save, Guild guild, Message message, User author) {
    	Server server = save.getData().getServerById(guild.getId());
        if (message.getMentionedUsers().size() == 0) {
            if (server.getClientById(author.getId()) == null)
                server.addClient(new Client(author.getId(), 0));
            if (!message.getChannelType().equals(ChannelType.PRIVATE)) message.delete().complete();
            return author.getAsMention() + ", vous poss\u00e9dez " + server.getClientById(author.getId()).getCredits() + " cr\u00e9dits";
        }
        if (server.getClientById(message.getMentionedUsers().get(0).getId()) == null)
            server.addClient(new Client(message.getMentionedUsers().get(0).getId(), 0));
        return message.getMentionedUsers().get(0).getAsMention() + " possède " + server.getClientById(message.getMentionedUsers().get(0).getId()).getCredits() + " cr\u00e9dits";
    }
    
    public MessageAndSave transfert(Maeva save, Guild guild, String[] args, User author, Message message) {
        if (args.length < 3 || !Functions.isInt(args[1]) || message.getMentionedUsers().size() == 0)
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#transfert [nombre de cr\u00e9dits] <@user>", save);
            if (Integer.parseInt(args[1]) < 0)
            	return new MessageAndSave("Tu te crois malin(e) ?", save);
            Server server = save.getData().getServerById(guild.getId());
            Client client1 = server.getClientById(author.getId());
            Client client2 = server.getClientById(message.getMentionedUsers().get(0).getId());
            if (client1 == null)
            	return new MessageAndSave("#erreur# Erreur : Vous ne poss\u00e9dez aucun cr\u00e9dit", save);
            if (client1.getCredits() < Integer.parseInt(args[1]))
            	return new MessageAndSave("#erreur# Erreur : Vous n'avez pas assez de cr\u00e9dits", save);
            client1.removeCredits(Integer.parseInt(args[1]));
            if (client2 != null)
            	client2.addCredits(Integer.parseInt(args[1]));
            else server.addClient(new Client(message.getMentionedUsers().get(0).getId(), Integer.parseInt(args[1])));
            return new MessageAndSave(args[1] + " cr\u00e9dits ont bien \u00e9t\u00e9 transf\u00e9r\u00e9s de " + author.getAsMention() + " vers " + message.getMentionedUsers().get(0).getAsMention(), save);
    }
}