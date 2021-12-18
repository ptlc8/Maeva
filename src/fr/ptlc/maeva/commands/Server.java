package fr.ptlc.maeva.commands;

import java.time.format.DateTimeFormatter;
import java.util.Random;

import fr.ptlc.maeva.data.Maeva;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Server {
    public MessageEmbed getInfo(Guild guild, Maeva save) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        Random rdm = new Random();
        embedBuilder.setAuthor("Informations sur le serveur :", null, guild.getIconUrl());
        embedBuilder.setDescription(guild.getName());
        embedBuilder.addField("Cr\u00e9ation", guild.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), true);
        embedBuilder.addField("Nombre de membres", String.valueOf(guild.getMembers().size()), true);
        embedBuilder.addField("Propri\u00e9taire", guild.getOwner().getUser().getName(), true);
        embedBuilder.addField("Niveau de v\u00e9rification", guild.getVerificationLevel().name(), true);
        embedBuilder.addField("Nombre de grades", String.valueOf(guild.getRoles().size()), true);
        embedBuilder.addField("Nombre de boost", guild.getBoostCount() + " (tier : " + guild.getBoostTier().name() + ")", true);
        /*if (guild.getRoles().size() > 0) {
            String tmp = guild.getRoles().get(0).getName();
            for (int i = 1; i < guild.getRoles().size(); i++)
                tmp = String.valueOf(tmp) + " - " + guild.getRoles().get(i).getName();
            embedBuilder.addField("Grades", tmp, false);
        }*/ // si trop de rôles le message devient trop long --'
        if (guild.getEmotes().size() > 0)
            embedBuilder.addField("Emote al\u00e9atoire", guild.getEmotes().get(rdm.nextInt(guild.getEmotes().size())).getAsMention(), true);
        int allCredits = save.getData().getServerById(guild.getId()).getAllCredits();
        if (allCredits > 0) embedBuilder.addField("Crédits en circulation", allCredits + " crédits", true);
        return embedBuilder.build();
    }
}

