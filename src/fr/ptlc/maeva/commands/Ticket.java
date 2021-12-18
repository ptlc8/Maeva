package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.Functions;
import fr.ptlc.maeva.MessageAndSave;
import fr.ptlc.maeva.data.Client;
import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.data.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class Ticket {
    public MessageAndSave buy(Maeva save, Guild guild, User author, Message message) {
    	Server server = save.getData().getServerById(guild.getId());
        if (server.getTicketProb().size() == 0)
            return new MessageAndSave("#erreur# Erreur : les possibilités de résultats n'ont pas été programmées", save);
        Client client = server.getClientById(author.getId());
        if (client == null)
            server.addClient(new Client(author.getId(), 0));
        if (client.getCredits() < server.getTicketPrice())
            return new MessageAndSave("#die##ticket# Désolé, il vous manque " + (server.getTicketPrice() - client.getCredits()) + " crédits pour acheter un ticket surprise", save);
        int result = server.getTicketProb().get(new Random().nextInt(server.getTicketProb().size()));
        client.addCredits(result - server.getTicketPrice());
        //message.getChannel().sendTyping().queue();
        if (!message.getChannelType().equals(ChannelType.PRIVATE))
        	message.delete().queue();
        return new MessageAndSave(author.getAsMention() + "\n#die# Résultat du ticket : " + result + " crédits\n#ticket# Total des gains : " + (result - server.getTicketPrice()) + " crédits", save);
    }

    public String info(Maeva save, Guild guild) {
    	Server server = save.getData().getServerById(guild.getId());
        float m = 0.0f;
        for (int prob : server.getTicketProb())
            m += (float)prob;
        m = m / (float)server.getTicketProb().size() - (float)server.getTicketPrice();
        return "#die# Coût : " + server.getTicketPrice() + " crédits\n#ticket# Résultats possibles : " + server.getTicketProb().toString() + "\n#stats# Gains moyens : " + m + " crédits";
    }

    public MessageAndSave set(boolean isOp, Maeva save, Guild guild, String[] args) {
        if (!isOp)
        	return new MessageAndSave("#cadena# Vous n'êtes pas administrateur b0t", save);
        if (args.length == 2){
        	args = new String[3];
        	args[2] = "";
        }
        Server server = save.getData().getServerById(guild.getId());
        switch (args[2]) {
        case "price":
            if (args.length < 4)
            	return new MessageAndSave("#syntaxe# Syntaxe : #prefix#ticket set price [prix]", save);
            if (!Functions.isInt(args[3]))
            	return new MessageAndSave("#erreur# Erreur : Le prix doit être un nombre", save);
            server.setTicketPrice(Integer.parseInt(args[3]));
            return new MessageAndSave("#die##ticket# Le prix d'un ticket surprise est désormais de " + server.getTicketPrice() + " crédits", save);
        case "results":
            if (args.length < 4)
            	return new MessageAndSave("#syntaxe# Syntaxe : #prefix#ticket set results [resultat1] [resultat2] ...", save);
            List<Integer> newResults = new ArrayList<Integer>();
            for (int i = 3; i < args.length; i++)
            	if (Functions.isInt(args[i]))
            		newResults.add(Integer.parseInt(args[i]));
            if (newResults.isEmpty())
            	return new MessageAndSave("#erreur# Erreur : Aucun résultat n'est un nombre", save);
            server.setTicketProb(newResults);
            return new MessageAndSave("#die##ticket# Les possibilités de résultats sont maintenant : " + server.getTicketProb().toString(), save);
        default:
        	return new MessageAndSave("#syntaxe# Syntaxe : #prefix#ticket set <price|results>", save);
        }
      }
}

