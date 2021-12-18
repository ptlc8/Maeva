package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.Functions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Log {
	
	public static final String syntaxe = "#syntaxe# Syntaxe : #prefix#log <save|list|read>";
    
    public String save(boolean isOp, String[] args, User author, MessageChannel messageChannel, Guild guild) {
        if (!isOp)
            return "#cadena# Vous n'\u00eates pas administrateur b0t";
        int nbMsg = args.length > 2 && Functions.isInt(args[2]) ? Integer.parseInt(args[2]) : 10;
        if (nbMsg <= 0 || nbMsg >= 100) return "#syntaxe# Erreur : Le nombre de message doit \u00eatre compris entre 1 et 99";
        File logsFolder = new File("logs");
        logsFolder.mkdir();
        String fileName = "logs./log-" + author.getName() + " " + new SimpleDateFormat("yyyy").format(new Date()) + "-" + new SimpleDateFormat("MM").format(new Date()) + "-" + new SimpleDateFormat("dd").format(new Date()) + " " + new SimpleDateFormat("HH").format(new Date()) + "-" + new SimpleDateFormat("mm").format(new Date()) + ".txt".replace("\\", "").replace("/", "").replace(":", "").replace("*", "").replace("?", "").replace("\"", "").replace("<", "").replace(">", "").replace("|", "");
        File newLog = new File(fileName);
        for (int nbSuppl = 2; newLog.exists(); nbSuppl++)
            newLog = new File(fileName.replace(".txt", " (" + nbSuppl + ")" + ".txt"));
        String comment = "";
        for (int i = 3; i < args.length; i++)
            comment = String.valueOf(comment) + args[i] + " ";
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newLog), "UTF-8"));
            List<Message> history = messageChannel.getHistory().retrievePast(nbMsg + 1).complete();
            String guildName = guild == null ? "*PrivateChannel*" : guild.getName();
            bw.append("Dans le salon " + messageChannel.getName() + " du serveur " + guildName + ", " + nbMsg + " messages ont \u00e9t\u00e9 enregistr\u00e9s par " + author.getName());
            bw.newLine();
            bw.append("Commentaire : " + comment);
            bw.newLine();
            for (int i = history.size() - 1; i > 0; i--) {
                bw.append("  " + history.get(i).getAuthor().getName() + " --> " + history.get(i).getContentDisplay());
                if (history.get(i).getEmbeds().size() != 0)
                    bw.append(" (embed)");
                bw.append(" [" + history.get(i).getTimeCreated() + "]");
                if (history.get(i).isEdited())
                    bw.append(" (\u00e9dit\u00e9)");
                if (history.get(i).isPinned())
                    bw.append(" (\u00e9pingl\u00e9)");
                if (history.get(i).isTTS())
                    bw.append(" (tts)");
                if (history.get(i).getReactions().size() != 0) {
                    bw.append(" r\u00e9actions : {");
                    for (int j = 0; j < history.get(i).getReactions().size(); j++) {
                        bw.append(String.valueOf(history.get(i).getReactions().get(j).getReactionEmote().getName()) + ">");
                        List<User> usersReaction = history.get(i).getReactions().get(j).retrieveUsers().complete();
                        for (int k = 0; k < usersReaction.size(); k++)
                            bw.append((usersReaction.get(k)).getName() + ",");
                        bw.append(";");
                    }
                    bw.append("}");
                }
                if (history.get(i).getAttachments().size() != 0) {
                    bw.append(" attachements : ");
                    for (int j = 0; j < history.get(i).getAttachments().size(); j++)
                        bw.append("\"" + history.get(i).getAttachments().get(j).getUrl() + "\" ");
                }
                bw.newLine();
            }
            bw.flush();
            bw.close();
            return "#folder# Les " + nbMsg + " derniers messages ont bien \u00e9t\u00e9 enregistr\u00e9s dans le fichier " + newLog.getName();
        }
        catch (IOException e) {
            System.out.println("Erreur d'enregistrement de messages :\n" + e);
            return "#erreur# Erreur lors de l'enregistrement des " + nbMsg + " derniers messages";
        }
    }

    public String list(boolean isOp) {
        if (!isOp)
        	return "#cadena#  Vous n'\u00eates pas administrateur b0t";
        File logsDossier = new File("logs");
        logsDossier.mkdir();
        if (logsDossier.listFiles().length == 0)
            return "#erreur# Erreur : Il n'y aucun fichier de log dans le dossier `logs`";
        String allLogs = "\n0 : " + logsDossier.listFiles()[0].getName();
        for (int i = 1; i < logsDossier.listFiles().length; i++)
            allLogs = allLogs + "\n" + i + " : " + logsDossier.listFiles()[i].getName();
        return "#folder# Voici la liste de tous les fichiers de log :" + allLogs;
    }

    public String read(boolean isOp, String[] args) {
        if (!isOp)
            return "#cadena#  Vous n'\u00eates pas administrateur b0t";
        if (!Functions.isInt(args[2]))
            return "#syntaxe# Syntaxe : #prefix#log read [id du log]";
        File logsFolder = new File("logs");
        logsFolder.mkdir();
        if (logsFolder.list().length == 0)
            return "#erreur# Erreur : Le dossier `logs` ne contient aucun fichier";
        if (Integer.parseInt(args[2]) < 0 || Integer.parseInt(args[2]) >= logsFolder.list().length)
            return "#erreur# Erreur : L'id du fichier de log doit \u00eatre compris entre 0 et " + (logsFolder.listFiles().length - 1);
        File logFile = logsFolder.listFiles()[Integer.parseInt(args[2])];
        try {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
            String log = "";
            while ((line = br.readLine()) != null)
                log = String.valueOf(log) + "\n" + line;
            br.close();
            String toSend = "#folder# Voici le fichier de log `" + logFile.getName() + "` :" + log;
                return toSend.length() <= 2000 ? toSend : "#erreur# Erreur : le fichier de log `" + logFile.getName() + "` ex\u00e8de 2'000 caract\u00e8res, par cons\u00e9quant son contenu ne peut pas \u00eatre envoy\u00e9 par message";
        }
        catch (IOException e) {
            return "#erreur# Erreur lors de la lecture du fichier `" + logFile.getName() + "`";
        }
    }
}

