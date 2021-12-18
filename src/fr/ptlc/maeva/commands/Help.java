package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.Functions;
import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.values.UserProfession;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Help {
	
	public enum HelpPage {
		DEFAULT(),
		OP(),
		MODO();
	}
	
	public MessageEmbed get(Maeva save, Guild guild, HelpPage helpPage, JDA jda) {
		EmbedBuilder eb = new EmbedBuilder();
		String prefix = "maeva ";
		if (guild != null)
			prefix = save.getData().getServerById(guild.getId()).getPrefix();
		switch (helpPage) {
			case DEFAULT:
				eb.setTitle("Menu d'aide :");
				eb.addField(prefix + "buy [id]", "pour acheter dans la boutique", false);
				eb.addField(prefix + "daily", "pour recevoir un salaire selon son grade", false);
				eb.addField(prefix + "daily list", "pour obtenir la liste de tous les salaires", false);
				eb.addField(prefix + "feedback [commentaire]", "pour envoyer un commentaire, une proposition ou une remarque sur moi \u00e0 " + Functions.getUser(jda, UserProfession.CREATOR).getAsMention(), false);
				eb.addField(prefix + "gif <arguments de recherche>", "pour afficher des gifs", false);
				eb.addField(prefix + "howmany", "pour conna\u00eetre son nombre de cr\u00e9dits (ou hm)", false);
				eb.addField(prefix + "howmany <@quelqu'un>", "pour conna\u00eetre le nombre de cr\u00e9dits de quelqu'un (ou hm)", false);
				eb.addField(prefix + "img <@quelqu'un>", "pour obtenir le lien de l'avatar de quelqu'un", false);
				eb.addField(prefix + "info", "pour obtenir quelques infos sur moi", false);
				eb.addField(prefix + "invite", "pour m'ajouter sur un serveur Discord", false);
				eb.addField(prefix + "recipes", "pour afficher 10 recettes de cuisine", false);
				eb.addField(prefix + "role <nom du role>", "pour obtenir un rôle", false);
				eb.addField(prefix + "rules", "pour s'informer des r\u00e8gles du serveur", false);
				eb.addField(prefix + "say <texte>", "pour me faire parler", false);
				eb.addField(prefix + "server", "pour obtenir quelques infos sur le serveur", false);
				eb.addField(prefix + "shop", "pour afficher la boutique", false);
				eb.addField(prefix + "speeddating", "pour participer à un speed dating sur le serveur", false);
				eb.addField(prefix + "ticket", "pour obtenir un ticket surprise", false);
				eb.addField(prefix + "ticket info", "pour obtenir les statistiques actuelles d'un ticket", false);
				eb.addField(prefix + "transfert [nombre de cr\u00e9dits] <@quelqu'un>", "pour donner quelques de ses cr\u00e9dits \u00e0 quelqu'un", false);
				eb.setColor(Color.GREEN);
				return eb.build();
			case OP:
				eb.setTitle("Menu d'aide (admin b0t) :");
				eb.addField(prefix + "add [nombre] <@client>", "pour donner des cr\u00e9dits \u00e0 un client", false);
				eb.addField(prefix + "admin <add|list|remove>", "pour g\u00e9rer les aministrateurs b0t", false);
				eb.addField(prefix + "as <@utilisateur> <commande>", "pour executer une commande \u00e0 la place d'un autre utilisateur", false);
				eb.addField(prefix + "daily set [salaire] <@grade|everyone>", "pour d\u00e9finir un salaire \u00e0 un grade", false);
				eb.addField(prefix + "daily remove <@grade|everyone>", "pour supprimer le salaire d'un grade", false);
				eb.addField(prefix + "log <save|list|read>", "pour g\u00e9rer les messages enregistrer en fichiers externes", false);
				eb.addField(prefix + "remove [nombre] <@client>", "pour retirer des cr\u00e9dits \u00e0 un client", false);
				eb.addField(prefix + "send <@utilisateur> <message super cool>", "pour envoyer un message priv\u00e9 via moi \u00e0 un certain utilisateur", false);
				eb.addField(prefix + "shop add <nomDeL'article> [prix] image:<url image> description:\"<description>\" <argument1> <argument2> ...", "pour ajouter un article", false);
				eb.addField(prefix + "shop remove [id]", "pour supprimer un article", false);
				eb.addField(prefix + "status <online|idle|do_not_disturb|invisible> <play|listen|stream|watch|custom> <nouveau statut>", "pour changer mon statut", false);
				eb.addField(prefix + "ticket set <price|results>", "pour changer le prix ou les r\u00e9sultats d'un ticket", false);
				eb.setColor(Color.RED);
				return eb.build();
			case MODO:
				eb.setTitle("Menu d'aide (admin serveur) :");
				eb.addField(prefix + "autorole <add|list|remove>", "pour gérer les rôles disponibles via la commande " + prefix + "role", false);
				eb.addField(prefix + "prefix <nouveau pr\u00e9fix>", "pour changer le pr\u00e9fix", false);
				eb.addField(prefix + "rules set \"title1\" \"description1\" [R1] [G1] [B1] \"title2\" \"description2\" [R2] [G2] [B2] ...", "pour d\u00e9finir les r\u00e8gles du serveurs", false);
				eb.addField(prefix + "speeddating start", "pour démarrer le speed dating avec les gens pré-inscrits", false);
				eb.addField(prefix + "twitch sync <#twitch_channel>", "pour relier le chat d'un live Twitch \u00e0 un salon Discord", false);
				eb.setColor(Color.CYAN);
				return eb.build();
		}
		return null;
	}
}
