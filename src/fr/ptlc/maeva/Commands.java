package fr.ptlc.maeva;

import java.util.concurrent.TimeUnit;

import fr.ptlc.maeva.commands.*;
import fr.ptlc.maeva.commands.Help.HelpPage;
import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.values.UserProfession;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Commands {
	
	public static Maeva command(Maeva save, String[] args, MessageReceivedEvent event, User author, Message message, JDA jda, MessageChannel messageChannel, Guild guild) {
		boolean isLEA = false;
		if (guild != null) isLEA = save.getData().getServerById(guild.getId()).isLEA();
		boolean isOp = (save.getData().isOp(author.getId())) || (event.getAuthor().getIdLong() == UserProfession.CREATOR.getId());
		boolean isModo = isOp || ((guild == null) ? true : (Functions.isModo(message.getMember())));
		switch (args[0]) {
		
		case "help":
			resend(null, new Help().get(save, guild, HelpPage.DEFAULT, jda), event);
			if (isModo) resend(null, new Help().get(save, guild, HelpPage.MODO, jda), event);
			if (isOp) resend(null, new Help().get(save, guild, HelpPage.OP, jda), event);
			return null;
		
		
		case "say":
			return resend(save, false, new Say().say(message, args), event);
		
		
		case "info":
			Info info = new Info();
			return resend(null, info.get(jda, save, 1), event);
			//resend(null, info.get(jda, save, 2), event);
			//resend(null, info.get(jda, save, 3), event);
		
		
		case "server":
			if (guild == null) return resend(save, false, "#erreur# Erreur : Impossible d'utiliser cette commande en dehors d'un serveur", event);
			Server server = new Server();
			return resend(null, server.getInfo(guild, save), event);
		
		
		case "save":
			return resend(save, true, "Sauvegarde faite !", event);
		
		
		case "add":
			return resend(new Credits().add(save, isOp, args, message, guild, author), true, event);
		
		
		case "remove":
			return resend(new Credits().remove(save, guild, isOp, args, message, author), true, event);
		
		
		case "hm":
		case "howmany":
			return resend(save, false, new Credits().howmany(save, guild, message, author), event);
			
			
		case "shop":
			if (args.length == 1) return resend(save, new Shop().print(save, guild, jda), event);
			switch (args[1]) {
			case "add":
				return resend(new Shop().add(save, args, guild, isOp), true, event);
			case "remove":
				return resend(new Shop().remove(save, isOp, args, guild), true, event);
			case "buy":
				if (!isLEA) return resend(save, Functions.notLEA(save, jda), event);
				return resend(new Shop().buy(save, guild, args, author, messageChannel, jda, 0), true, event);
			default: 
				return resend(save, false, "#syntaxe# Syntaxe : #prefix#shop <add|remove|buy>", event);
			}
		
		
		case "buy":
			if (!isLEA) return resend(save, Functions.notLEA(save, jda), event);
			return resend(new Shop().buy(save, guild, args, author, messageChannel, jda, -1), true, event);
		
		
		case "img":
			return resend(save, false, new Img().get(args, message), event);
		
		
		case "prefix":
			if (guild == null) return resend(save, false, "#erreur# Erreur : Impossible d'utiliser cette commande en dehors d'un serveur", event);
			return resend(new Prefix().set(save, isModo, args, guild), true, event);
		
		
		case "admin":
			if (args.length == 1) {args = new String[2]; args[1] = "";}
			switch (args[1]) {
			case "add":
				return resend(new Admin().add(save, isOp, args, message), true, event);
			case "list":
				return resend(save, false, new Admin().list(save, jda), event);
			case "remove":
				return resend(new Admin().remove(save, isOp, args, message, jda), true, event);
			default:
				return resend(save, false, "#syntaxe# Syntaxe : #prefix#admin <add|list|remove>", event);
			}
		
		
		case "statut": // <- deprecated
		case "status":
			return resend(save, false, new Status().set(isOp, args, jda), event);
		
		
		case "feedback":
			return resend(save, false, new Feedback().send(args, author, jda, message), event);
		
		
		case "send":
			return resend(save, false, new Send().send(isOp, args, message, jda), event);
		
		
		case "daily":
			if (guild == null) return resend(save, false, "#erreur# Erreur : Impossible d'utiliser cette commande en dehors d'un serveur", event);
			if (args.length == 1)
				return resend(new Daily().get(save, guild, author, message), true, event);
			switch (args[1]) {
			case "list":
				return resend(save, false, new Daily().list(save, jda), event);
			case "set":
				return resend(new Daily().set(isOp, save, args, message, jda), true, event);
			case "remove":
				return resend(new Daily().remove(isOp, save, message), true, event);
			case "reset":
				return resend(new Daily().reset(isOp, save), true, event);
			default:
				return resend(save, false, "#syntaxe# Syntaxe : #prefix#daily <set|remove|list> OU #prefix#daily", event);
			}
		
		
		case "rules":
			if (guild == null) return resend(save, false, "#erreur# Erreur : Impossible d'utiliser cette commande en dehors d'un serveur", event);
			if (args.length > 1)
				return resend(new Rules().set(isModo, save, guild, args, author), true, event);
			if (save.getData().getServerById(guild.getId()).getRules().isEmpty())
				return resend(save, false, new Rules().no(), event);
			for (int i = 0; i < save.getData().getServerById(guild.getId()).getRules().size(); i++)
				resend(save, new Rules().print(save, guild, i), event);
			return null;
		
		
		case "log":
			if (args.length == 1) return resend(save, false, Log.syntaxe, event);
			switch (args[1]) {
			case "save":
				return resend(save, false, new Log().save(isOp, args, author, messageChannel, guild), event);
			case "list":
				return resend(save, false, new Log().list(isOp), event);
			case "read":
				return resend(save, false, new Log().read(isOp, args), event);
			default:
				return resend(save, false, Log.syntaxe, event);
			}
		
		
		case "ticket":
			if (args.length == 1)
				return resend(new Ticket().buy(save, guild, author, message), true, event);
			switch (args[1]) {
			case "info": 
				return resend(save, false, new Ticket().info(save, guild), event);
			case "set":
				return resend(new Ticket().set(isOp, save, guild, args), true, event);
			default:
				return resend(save, false, "#syntaxe# Syntaxe : #prefix#ticket OU #prefix#ticket <info|set>", event);
			}
		
		
		case "transfert":
			return resend(new Credits().transfert(save, guild, args, author, message), true, event);
		
		
		case "gif":
			return resend(save, false, new Gif().get(args, author, message), event);
		
		
		case "twitch":
			if (args.length < 2) return resend(save, false, "#syntaxe# Syntaxe : #prefix#twitch sync <#twitch_channel>", event);
			switch (args[1]) {
			case "sync":
				return resend(save, false, new Twitch().sync(isModo, args, message, messageChannel), event);
			case "channel":
				return resend(save, false, new Twitch().user(args, message), event);
			default:
				return resend(save, false, "#syntaxe# Syntaxe : #prefix#twitch sync <#twitch_channel>", event);
			}
		
		
		case "as":
		case "execute":
			return resend(new Execute().as(isOp, save, args, event, author, message, jda, messageChannel, guild), true, event);
		
		
		case "typing":
			message.getChannel().sendTyping().complete();
			return null;
		
		
		case "role":
			return resend(save, false, new AutoRole().auto(save, args, guild, author), event);
		
		
		case "autorole":
			if (args.length < 2) return resend(save, false, AutoRole.syntax, event);
			switch (args[1]) {
			case "add":
				return resend(new AutoRole().add(save, isModo, args, guild), true, event);
			case "list":
				return resend(save, false, new AutoRole().list(save, guild), event);
			case "remove":
				return resend(new AutoRole().remove(save, isModo, args, guild), true, event);
			default:
				return resend(save, false, AutoRole.syntax, event);
			}
		
		
		case "antispam":
			return resend(AntiSpam.set(save, isModo, guild, message, args), true, event);
		
		
		case "speeddating":
		case "sd":
			if (args.length < 2) return resend(save, false, SpeedDating.syntax, event);
			switch (args[1]) {
			case "join":
				return resend(save, false, new SpeedDating().join(guild, author, args), event);
			case "start":
				return resend(save, false, new SpeedDating().start(isModo, guild, author, messageChannel, args), event);
			case "list":
			case "prelist":
				return resend(save, false, new SpeedDating().prelist(guild), event);
			case "leave":
				return resend(save, false, new SpeedDating().leave(isModo, guild, author, message), event);
			default:
				return resend(save, false, SpeedDating.syntax, event);
			}
		
		
		case "join":
			if (guild.getMember(author).getVoiceState().getChannel()==null) return null;
			guild.getAudioManager().openAudioConnection(guild.getMember(author).getVoiceState().getChannel());
			guild.getAudioManager().setSelfMuted(true);
			guild.getAudioManager().setSelfDeafened(true);
			return null;
		
		
		case "leave":
			guild.getAudioManager().closeAudioConnection();
			return null;
		
		
		case "recipes":
			return resend(null, Recipes.getLast10(), event);
		
		
		case "invite":
			return resend(save, false, "Voici un lien pour m'ajouter sur un serveur Discord :\n<https://discord.com/oauth2/authorize?client_id="+jda.getSelfUser().getId()+"&scope=bot&permissions=18213977>", event);
		
		
		case "embed":
			return resend(save, Say.embed(isModo, message, args), event);
		
		
		case "moveall":
			return resend(save, false, Voice.moveAll(isModo, guild, message, args), event);
		
		
		case "schedule":
			return resend(save, false, Say.schedule(message, args), event);
		
		
		default:
			return null;
		}
	}
	
	
	private static Maeva resend(MessageAndSave ms, boolean doSave, Event event) {
		return Commands.resend(ms.getSave(), doSave, ms.getMessage(), event);
	}
	
	private static Maeva resend(Maeva save, boolean doSave, String message, Event event) {
		if (message == null || message.equals("")) return doSave ? save : null;
		message = message
				.replace("#syntaxe#", "🔴")
				.replace("#colis#", "📦")
				.replace("#cadena#", "🔒")
				.replace("#lettre#", "✉")
				.replace("#erreur#", "❌")
				.replace("#debloque#", "🔓")
				.replace("#folder#", "📁")
				.replace("#soon#", "🔜")
				.replace("#die#", "🎲")
				.replace("#stats#", "📈")
				.replace("#ticket#", "🎟")
				.replace("#cadeau#", "🎁")
				.replace("#coeur#", "♥");
		message = ((MessageReceivedEvent)event).isFromType(ChannelType.PRIVATE)
				? message = message.replace("#prefix#", "maeva ")
				: message.replace("#prefix#", save.getData().getServerById(((MessageReceivedEvent) event).getGuild().getId()).getPrefix());
				
				((MessageReceivedEvent) event).getChannel().sendTyping().queue();
				((MessageReceivedEvent) event).getChannel().sendMessage(message).queueAfter(400, TimeUnit.MILLISECONDS);
				if (doSave) return save;
				else return null;
	}
	
	private static Maeva resend(Maeva save, MessageEmbed embed, Event event) {
		if (embed == null) return save;
		if (!((MessageReceivedEvent)event).isFromType(ChannelType.PRIVATE)) if (!((MessageReceivedEvent) event).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS))
			return resend(save, false, "#erreur# Erreur : Impossible d'envoyer d'embed dans ce channel", event);
		((MessageReceivedEvent) event).getChannel().sendMessage(embed).queue();
		return save;
	}
}
