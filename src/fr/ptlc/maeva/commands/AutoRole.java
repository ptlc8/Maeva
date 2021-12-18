package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.MessageAndSave;
import fr.ptlc.maeva.data.Maeva;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class AutoRole {
	
	public static final String syntax = "#syntaxe# Syntaxe : #prefix#autorole <add|list|remove>";
	
	public String auto(Maeva save, String[] args, Guild guild, User author) {
		if (args.length < 2)
			return list(save, guild);
		String roleName = args[1];
		for (int i = 2; i < args.length; i++)
			roleName += " " + args[i];
		if (save.getData().getServerById(guild.getId()).getAutoRoleById(roleName) == null)
			return "#erreur# Erreur : Ce r�le n'est pas obtenable de cette fa�on"
					+ "\nPour avoir la liste des r�les disponibles : #prefix#role";
		if (guild.getRolesByName(roleName, false).isEmpty())
			return "#erreur# Erreur : Ce r�le n'est pas pr�sent sur ce serveur";
		Role role = guild.getRolesByName(roleName, false).get(0);
		boolean hasRole = false;
		for (Role r : guild.getMember(author).getRoles())
			if (r.getId().equals(role.getId()))
				hasRole = true;
		if (hasRole) {
			guild.removeRoleFromMember(guild.getMember(author), role).queue();
			return "Le r�le " + roleName + " vous a �t� retir�";
		} else {
			guild.addRoleToMember(guild.getMember(author), role).queue();
			return "Vous avez re�u le r�le " + roleName;
		}
	}
	
	public MessageAndSave add(Maeva save, boolean isModo, String[] args, Guild guild) {
		if (!isModo)
			return new MessageAndSave("#cadena# Vous n'�tes pas administrateur serveur", save);
		if (args.length < 3)
			return new MessageAndSave("#syntaxe# Syntaxe : #prefix#autorole add <nom du role>", save);
		String roleName = args[2];
		for (int i = 3; i < args.length; i++)
			roleName += " " + args[i];
		if (save.getData().getServerById(guild.getId()).getAutoRoles().contains(roleName))
			return new MessageAndSave("#erreur# Erreur : Ce r�le est d�j� obtenable de cet fa�on", save);
		if (guild.getRolesByName(roleName, false).isEmpty())
			return new MessageAndSave("#erreur# Erreur : Ce r�le n'existe pas sur ce serveur", save);
		save.getData().getServerById(guild.getId()).addAutoRole(roleName);
		return new MessageAndSave("Le r�le " + roleName + " est d�sormais obtenable via la commande : #prefix#role " + roleName, save);
	}
	
	public String list(Maeva save, Guild guild) {
		if (save.getData().getServerById(guild.getId()).getAutoRoles().isEmpty())
			return "Aucun r�le n'est disponible sur ce serveur";
		String list = "";
		for (String autorole : save.getData().getServerById(guild.getId()).getAutoRoles())
			list += "\n  - " + autorole;
		return "Liste de tous les r�les disponibles sur ce serveur : " + list
				+ "\nPour obtenir l'un de ces r�les : #prefix#role <nom du role>";
	}
	
	public MessageAndSave remove(Maeva save, boolean isModo, String[] args, Guild guild) {
		if (!isModo)
			return new MessageAndSave("#cadena# Vous n'�tes pas administrateur serveur", save);
		if (args.length < 3)
			return new MessageAndSave("#syntaxe# Syntaxe : #prefix#autorole remove <nom du role>", save);
		String roleName = args[2];
		for (int i = 3; i < args.length; i++)
			roleName += " " + args[i];
		if (!save.getData().getServerById(guild.getId()).getAutoRoles().contains(roleName))
			return new MessageAndSave("#erreur# Erreur : Ce r�le n'est pas obtenable de cet fa�on", save);
		save.getData().getServerById(guild.getId()).removeAutoRole(roleName);
		return new MessageAndSave("Le r�le " + roleName + " n'est plus obtenable via la commande : #prefix#role " + roleName, save);
	}
	
}
