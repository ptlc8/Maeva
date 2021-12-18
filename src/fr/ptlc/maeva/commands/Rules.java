package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.Functions;
import fr.ptlc.maeva.MessageAndSave;
import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.data.Rule;
import fr.ptlc.maeva.data.Server;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Rules {
	
    public MessageEmbed print(Maeva save, Guild guild, int part) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (save.getData().getServerById(guild.getId()).getRules().isEmpty())
            return null;
        Server server = save.getData().getServerById(guild.getId());
        List<Rule> rules = server.getRules();
        embedBuilder.setTitle(rules.get(part).getTitle());
        embedBuilder.setDescription(rules.get(part).getDescription());
        embedBuilder.setColor(rules.get(part).getColor());
        if (rules.size() == part + 1)
            embedBuilder.setFooter("Selon " + guild.getJDA().getUserById(server.getRulesUpdaterId()).getName() + ", " + server.getRulesUpdateDate(), guild.getJDA().getUserById(server.getRulesUpdaterId()).getAvatarUrl());
        return embedBuilder.build();
    }

    public String no() {
        return "#erreur# Auncun règlement n'a été enregistré sur ce serveur";
    }

    public MessageAndSave set(boolean isModo, Maeva save, Guild guild, String[] args, User author) {
        if (!isModo)
            return new MessageAndSave("#cadena# Vous n'\u00eates pas administrateur serveur", save);
        if (!args[1].equals("set") || args.length < 7)
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#rules set \"title1\" \"description1\" [R1] [G1] [B1] \"title2\" \"description2\" [R2] [G2] [B2] ...", save);
        ArrayList<String> argsWithQuotes = new ArrayList<String>();
        boolean inQuotes = false;
        String tmp = "";
        for (int i = 2; i < args.length; i++) {
            if (args[i].startsWith("\"") && args[i].endsWith("\"")) {
                argsWithQuotes.add(args[i].substring(1, args[i].length() - 1));
            } else if (args[i].startsWith("\"") && args[i].endsWith("\"")) {
                argsWithQuotes.add(args[i].substring(1, args[i].length()));
            } else if (args[i].startsWith("\"")) {
                inQuotes = true;
                tmp = args[i].substring(1);
            } else if (inQuotes && args[i].endsWith("\"")) {
                inQuotes = false;
                tmp = String.valueOf(tmp) + " " + args[i].substring(0, args[i].length() - 1);
                argsWithQuotes.add(tmp);
            } else if (inQuotes) {
                tmp = String.valueOf(tmp) + " " + args[i];
            } else {
                argsWithQuotes.add(args[i]);
            }
        }
        if (argsWithQuotes.size() % 5 != 0)
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#rules set \"title1\" \"description1\" [R1] [G1] [B1] \"title2\" \"description2\" [R2] [G2] [B2] ...", save);
        ArrayList<Rule> rules = new ArrayList<Rule>();
        for (int i = 0; i < argsWithQuotes.size() / 5; i++) {
            if (!(Functions.isInt((String)argsWithQuotes.get(i * 5 + 2)) && Functions.isInt((String)argsWithQuotes.get(i * 5 + 3)) && Functions.isInt((String)argsWithQuotes.get(i * 5 + 4))))
                return new MessageAndSave("#syntaxe# Syntaxe : #prefix#rules set \"title1\" \"description1\" [R1] [G1] [B1] \"title2\" \"description2\" [R2] [G2] [B2] ...", save);
            float[] HSBColor = Color.RGBtoHSB(Integer.parseInt((String)argsWithQuotes.get(i * 5 + 2)), Integer.parseInt((String)argsWithQuotes.get(i * 5 + 3)), Integer.parseInt((String)argsWithQuotes.get(i * 5 + 4)), new float[3]);
            rules.add(new Rule((String)argsWithQuotes.get(i * 5), (String)argsWithQuotes.get(i * 5 + 1), Color.getHSBColor(HSBColor[0], HSBColor[1], HSBColor[2])));
        }
        save.getData().getServerById(guild.getId()).setRules(rules, author.getId());
        return new MessageAndSave("Le règlement du serveur " + guild.getName() + " a bien été modifié\nPour l'afficher : #prefix#rules", save);
    }
}

