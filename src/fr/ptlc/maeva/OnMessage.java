package fr.ptlc.maeva;

import fr.ptlc.maeva.BotListener;
import fr.ptlc.maeva.ChatReply;
import fr.ptlc.maeva.Commands;
import fr.ptlc.maeva.Main;
import fr.ptlc.maeva.commands.ChatBot;
import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.data.Server;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class OnMessage {
    ChatReply chatReply = new ChatReply();

    public Maeva privateChannel(MessageReceivedEvent event, Maeva save) {
        String prefix = "maeva ";
        if (Main.getVersion().contains("beta")) {
            prefix = "beta ";
        }
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) {
            return null;
        }
        if (BotListener.onTwitch.isSync(event.getChannel())) {
            if (event.getMessage().getContentRaw().equals("STOP")) {
                event.getChannel().sendMessage("La syncronisation Twitch avec la cha\u00eene " + BotListener.onTwitch.getSync(event.getChannel()).getTwitchChannel() + " vient d'\u00eatre arr\u00eat\u00e9e").queue();
                BotListener.onTwitch.removeSync(event.getChannel());
            } else
                BotListener.onTwitch.send(event.getChannel(), "Sur Discord, " + event.getAuthor().getName() + " : " + event.getMessage().getContentRaw());
            return null;
        }
        for (String hi : save.getOptions().getHis())
            if (event.getMessage().getContentRaw().toLowerCase().startsWith(hi.toLowerCase()))
                event.getChannel().sendMessage(hi + " " + event.getAuthor().getAsMention() + " !").queue();
        if (event.getMessage().getContentRaw().toLowerCase().equals("maeva") || event.getMessage().getContentRaw().toLowerCase().equals("help") || event.getMessage().getContentRaw().equals("<@!"+event.getJDA().getSelfUser().getId()+">"))
            event.getChannel().sendMessage("Pour avoir de l'aide : " + prefix + "help").queue();
        if (event.getMessage().getContentRaw().startsWith(prefix)) {
            String[] args = event.getMessage().getContentRaw().substring(prefix.length()).split(" ");
            Commands.command(save, args, event, event.getAuthor(), event.getMessage(), event.getJDA(), event.getChannel(), null);
        }
        return null;
    }

    public Maeva guildChannel(MessageReceivedEvent event, Maeva save) {
        if (save.getData().getServerById(event.getGuild().getId()) == null)
            save.getData().addServer(new Server(event.getGuild().getId()));
        if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE))
            return null;
        String prefix = save.getData().getServerById(event.getGuild().getId()).getPrefix();
        if (Main.getVersion().contains("beta"))
            prefix = "beta ";
        if (save.getOptions().getConsoleMode().equals("all") || save.getOptions().getConsoleMode().equals("maeva") && event.getMessage().getContentRaw().toLowerCase().contains("maeva") || save.getOptions().getConsoleMode().equals("commands") && event.getMessage().getContentRaw().startsWith(prefix)) {
            if (event.getMessage().getEmbeds().size() == 0)
                System.out.println(String.valueOf(event.getGuild().getName()) + " -->   <" + event.getAuthor().getName() + "> " + event.getMessage().getContentRaw().replace("\n", "\n\t"));
            else
                System.out.println(String.valueOf(event.getGuild().getName()) + " -->   <" + event.getAuthor().getName() + "> " + event.getMessage().getEmbeds().get(0).getTitle() + " (embed)");
        }
        if (event.getChannel().getId().equals(save.getOptions().getCommandsChannelId())) {
        	if (event.getAuthor().equals(event.getJDA().getSelfUser())) {
        		event.getMessage().addReaction(event.getJDA().getEmotesByName(save.getOptions().getEmote1Name(), false).get(0)).queue();
        		event.getMessage().addReaction(event.getJDA().getEmotesByName(save.getOptions().getEmote2Name(), false).get(0)).queue();
        	} else {
        		for (Message m : Functions.getMessagesBefore(event.getChannel(), event.getMessageId(), 20))
        			if (Functions.getReaction(m, save.getOptions().getEmote3Name()) != null && Functions.getReaction(m, save.getOptions().getEmote3Name()).retrieveUsers().complete().contains(event.getJDA().getSelfUser()))
        				return null;
        		event.getChannel().sendMessage(Functions.convertEmotes(event.getJDA(), "Est-ce une question :"+save.getOptions().getEmote1Name()+": ou une commande :"+save.getOptions().getEmote2Name()+": ?")).queue();
        	}
        	return null;
        }
        if (event.getAuthor().equals(event.getJDA().getSelfUser()))
            return null;
        if (event.getTextChannel().getName().equals("maeva-part-en-couilles") || event.getTextChannel().getName().equals("maeva-part-en-cacahuètes")) {
        	if (!event.getMessage().getContentRaw().startsWith("!")) {
        		String response = ChatBot.chat(event.getMessage().getContentRaw());
        		if (!event.getMessage().getContentRaw().equals(response)) event.getTextChannel().sendMessage(response).queue();
        	}
        	return null;
        }
        if (this.chatReply.getOrderReplyId(event.getAuthor(), event.getChannel()) != null)
            return this.chatReply.reply(save, event.getGuild(), event.getChannel(), event.getMessage().getContentRaw(), event.getAuthor(), event.getJDA());
        if (BotListener.onTwitch.isSync(event.getChannel())) {
            if (event.getMessage().getContentRaw().equals("STOP") && Functions.isModo(event.getMember())) {
                event.getChannel().sendMessage("La syncronisation Twitch avec la cha\u00eene " + BotListener.onTwitch.getSync(event.getChannel()).getTwitchChannel() + " vient d'\u00eatre arr\u00eat\u00e9e").queue();
                BotListener.onTwitch.removeSync(event.getChannel());
            } else
                BotListener.onTwitch.send(event.getChannel(), "Sur Discord, " + event.getAuthor().getName() + " : " + event.getMessage().getContentRaw());
        }
        for (String hi : save.getOptions().getHis())
            if (event.getMessage().getContentRaw().toLowerCase().startsWith(hi.toLowerCase()))
                event.getTextChannel().sendMessage(hi + " " + event.getAuthor().getAsMention() + " !").queue();
        if (event.getMessage().getContentRaw().toLowerCase().equals("maeva") || event.getMessage().getContentRaw().toLowerCase().equals(prefix.trim()) || event.getMessage().getContentRaw().toLowerCase().equals("help") || event.getMessage().getContentRaw().equals("<@!"+event.getJDA().getSelfUser().getId()+">"))
            event.getTextChannel().sendMessage("Pour avoir de l'aide : " + prefix + "help").queue();
        if (event.getMessage().getContentRaw().startsWith(prefix)) {
            String sub = "";
            for (int i = 1; i < event.getGuild().getName().length() + 11 + event.getAuthor().getName().length(); i++)
                sub = String.valueOf(sub) + " ";
            for (int i = 0; i < prefix.length(); i++)
                sub = String.valueOf(sub) + "^";
            System.out.println(sub);
            String[] args = event.getMessage().getContentRaw().substring(prefix.length()).split(" ");
            return Commands.command(save, args, event, event.getAuthor(), event.getMessage(), event.getJDA(), event.getTextChannel(), event.getGuild());
        }
        return null;
    }
}

