package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.Functions;
import fr.ptlc.maeva.Main;
import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.values.UserProfession;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Info {
	
	private static final int convert = 1000 * 1000;
	private static final String u = "Mo";
	
    public MessageEmbed get(JDA jda, Maeva save, int part) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        switch (part) {
            case 1:
            	Runtime runtime = Runtime.getRuntime();
                embedBuilder.setAuthor("Informations", null, jda.getSelfUser().getAvatarUrl());
                embedBuilder.setDescription(jda.getSelfUser().getName());
                embedBuilder.setColor(Color.MAGENTA);
                embedBuilder.addField("Identifiant Discord", jda.getSelfUser().getId(), true);
                embedBuilder.addField("Version", Main.getVersion() + "(" + Main.getDate() + ")", true);
                embedBuilder.addField("Date de cr\u00e9ation", jda.getSelfUser().getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), true);
                embedBuilder.addField("Nombres de serveurs", String.valueOf(jda.getGuilds().size()), true);
                //embedBuilder.addField("Ping (Rest)", jda.getRestPing() + "ms", true);
                embedBuilder.addField("Ping (Gateway)", jda.getGatewayPing() + "ms", true);
                embedBuilder.addField("Version de Java", System.getProperty("java.version"), true);
                embedBuilder.addField("OS de l'h�bergeur", System.getProperty("os.name"), true);
                embedBuilder.addField("Version de l'OS", System.getProperty("os.version"), true);
                embedBuilder.addField("Arch de l'OS", System.getProperty("os.arch"), true);
                embedBuilder.addField("Processeurs disponibles", runtime.availableProcessors()+"", true);
                embedBuilder.addField("M�moire maximum", runtime.maxMemory()/convert + u, true);
                embedBuilder.addField("M�moire libre", runtime.freeMemory()/convert + u, true);
                embedBuilder.addField("M�moire utilis�", (runtime.totalMemory()-runtime.freeMemory())/convert + u, true);
                embedBuilder.addField("M�moire allou�e", runtime.totalMemory()/convert + u, true);
                embedBuilder.addField("M�moire libre totale", (runtime.freeMemory()+(runtime.maxMemory()-runtime.totalMemory()))/convert + u, true);
                embedBuilder.setFooter(Functions.getUser(jda, UserProfession.CREATOR).getAsTag(), Functions.getUser(jda, UserProfession.CREATOR).getAvatarUrl());
                return embedBuilder.build();
            case 2:
                embedBuilder.clearFields();
                embedBuilder.setDescription("");
                embedBuilder.setTitle("Cr\u00e9ateur");
                embedBuilder.setAuthor(Functions.getUser(jda, UserProfession.CREATOR).getName(), "https://ambi.dev", Functions.getUser(jda, UserProfession.CREATOR).getAvatarUrl());
                embedBuilder.setColor(Color.CYAN);
                return embedBuilder.build();
            case 3:
                embedBuilder.setTitle("Propri\u00e9taire");
                embedBuilder.setAuthor(Functions.getUser(jda, UserProfession.LIVREUR).getName(), null, Functions.getUser(jda, UserProfession.LIVREUR).getAvatarUrl());
                embedBuilder.setColor(Color.ORANGE);
                return embedBuilder.build();
        }
        return null;
    }
}

