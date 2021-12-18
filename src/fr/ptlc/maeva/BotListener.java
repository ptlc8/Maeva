package fr.ptlc.maeva;

import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.ptlc.maeva.Functions;
import fr.ptlc.maeva.OnMessage;
import fr.ptlc.maeva.commands.Twitch;
import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.data.Save;
import fr.ptlc.maeva.data.Server;
import fr.ptlc.maeva.twitch.OnTwitch;
import fr.ptlc.maeva.values.UserProfession;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class BotListener implements EventListener {
	 OnMessage on = new OnMessage();
	 public static OnTwitch onTwitch = new OnTwitch(Twitch.oauth);

	 @Override
	 public void onEvent(GenericEvent event) {
			if (event instanceof MessageReceivedEvent) {
				Maeva updateMaeva = ((MessageReceivedEvent)event).isFromType(ChannelType.PRIVATE) ? this.on.privateChannel((MessageReceivedEvent)event, Save.getMaeva()) : this.on.guildChannel((MessageReceivedEvent)event, Save.getMaeva());
				if (updateMaeva != null)
					 Save.writeDown(updateMaeva);
			}
			else if (event instanceof GuildMessageReactionAddEvent) {
				GuildMessageReactionAddEvent e = (GuildMessageReactionAddEvent) event;
				if (e.getReaction().isSelf()) return;
				Maeva save = Save.getMaeva();
				String emoteName = e.getReactionEmote().isEmoji() ? e.getReactionEmote().getEmoji() : e.getReactionEmote().getEmote().getName();
				//pour les interactions
				if (e.getReaction().retrieveUsers().complete().contains(e.getJDA().getSelfUser())) {
					//pour le channel de commandes
					if (e.getChannel().getId().equals(save.getOptions().getCommandsChannelId())) {
						if (emoteName.equals(save.getOptions().getEmote2Name())) {
							Message maevaQuery	= Functions.getMessage(e.getChannel(), e.getMessageId(), 0);
							Message userQuery = Functions.getMessage(e.getChannel(), e.getMessageId(), -1);
							userQuery.delete().queue();
							maevaQuery.editMessage("Pour passer une commande, veuillez utiliser la commande `"+save.getData().getServerById(e.getGuild().getId()).getPrefix()+"buy [id]` dans " + Functions.getChannelTag(e.getGuild(), save.getOptions().getOrderChannelId())).queue();
							maevaQuery.delete().queueAfter(5, TimeUnit.SECONDS);
						} else if(emoteName.equals(save.getOptions().getEmote1Name())) {
							Message maevaQuery	= Functions.getMessage(e.getChannel(), e.getMessageId(), 0);
							Message userQuery = Functions.getMessage(e.getChannel(), e.getMessageId(), -1);
							userQuery.addReaction(e.getJDA().getEmotesByName(save.getOptions().getEmote3Name(), false).get(0)).queue();
							maevaQuery.editMessage(e.getUser().getAsMention() + ", une personne compétante vous répondra dès qu'elle le pourra, puis ajoutez l'emote " + Functions.getEmoteTag(e.getJDA(), save.getOptions().getEmote3Name()) + "lorsque vous serez satisfait").queue();
							maevaQuery.delete().queueAfter(5, TimeUnit.SECONDS);
						} else if(emoteName.equals(save.getOptions().getEmote3Name())) {
							System.out.println(Functions.getMessagesAfter(e.getChannel(), e.getMessageId()).size());
							List<Message> msgs = Functions.getMessagesAfter(e.getChannel(), e.getMessageId());
							Functions.getMessage(e.getChannel(), e.getMessageId(), 0).delete().complete();
							for (Message m : msgs) {
								MessageReaction r = Functions.getReaction(m, save.getOptions().getEmote3Name());
								if (r != null && r.retrieveUsers().complete().contains(e.getJDA().getSelfUser())) break;
								System.out.println(m.getContentDisplay());
								m.delete().queue();
							}
						}
					}
				} else {
					//pour le channel d'antispam
					Server server = save.getData().getServerById(e.getGuild().getId());
					if (e.getChannel().getId().equals(server.getAntiSpamChannelId())) {
						Message message = e.getChannel().retrieveMessageById(e.getMessageId()).complete();
						if (emoteName.equals(server.getAntiSpamEmote()) && message.getMentionedMembers().contains(e.getMember())
								&& e.getMember().getRoles().contains(e.getGuild().getRoleById(server.getAntiSpamRoleId()))) {
							e.getGuild().removeRoleFromMember(e.getMember(), e.getGuild().getRoleById(server.getAntiSpamRoleId())).complete();
							message.delete().queue();
							e.getChannel().sendMessage(e.getUser().getAsMention() + ", bienvenue à toi !").queue();
						}
					}
				}
			}
			else if (event instanceof GuildJoinEvent) {
				Maeva maeva = Save.getMaeva();
				maeva.getData().addServer(new Server(((GuildJoinEvent)event).getGuild().getId()));
				Save.writeDown(maeva);
				((GuildJoinEvent)event).getGuild().getDefaultChannel().sendMessage("Merci de m'avoir ajoutée sur votre serveur !\nN'hésitez pas à envoyer `" + Save.getMaeva().getData().getServerById(((GuildJoinEvent)event).getGuild().getId()).getPrefix() + "help` pour avoir de l'aide\nEt si vous rencontrez des problèmes contactez " + Functions.getUser(event.getJDA(), UserProfession.CREATOR).getAsMention()).queue();
			} else if (event instanceof GuildMemberJoinEvent) {
				Maeva save = Save.getMaeva();
				Server server = save.getData().getServerById(((GuildMemberJoinEvent)event).getGuild().getId());
				if (server.getAntiSpamRoleId() != null && server.getAntiSpamChannelId() != null) {
					Member member = ((GuildMemberJoinEvent)event).getMember();
					Guild guild = ((GuildMemberJoinEvent)event).getGuild();
					guild.addRoleToMember(member, guild.getRoleById(server.getAntiSpamRoleId())).complete();
					TextChannel channel = guild.getTextChannelById(server.getAntiSpamChannelId());
					Message message = channel.sendMessage(member.getAsMention() + ", " + server.getAntiSpamRequest()).complete();
					for (String emoteName : server.getAntiSpamEmotes())
						message.addReaction(guild.getEmotesByName(emoteName, false).get(0)).queue();
				}
		}
	}
}

