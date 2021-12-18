package fr.ptlc.maeva;

import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.values.UserProfession;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class Functions {
	
    public static final User getUser(JDA jda, UserProfession profession) {
        if (Main.getVersion().contains("beta"))
            return jda.retrieveUserById(UserProfession.CREATOR.getId()).complete();
        return jda.retrieveUserById(profession.getId()).complete();
    }

    public static final String getRoleName(JDA jda, String id) {
        if (id.equals("everyone"))
            return "`everyone`";
        if (jda.getRoleById(id) == null)
        	return "**" + id + "**";
        return "**" + jda.getRoleById(id).getName() + "** (" + jda.getRoleById(id).getGuild().getName() + ")";
    }

    public static final void sendPrivate(JDA jda, UserProfession userProfession, String msg) {
        if (!getUser(jda, userProfession).hasPrivateChannel())
            getUser(jda, userProfession).openPrivateChannel().complete();
        getUser(jda, userProfession).openPrivateChannel().complete().sendMessage(msg).queue();
    }

    public static final void sendPrivate(User user, String msg) {
        if (!user.hasPrivateChannel()) {
            user.openPrivateChannel().complete();
        }
        user.openPrivateChannel().complete().sendMessage(msg).queue();
    }

    public static final String substractHourMinuteSeconde(int hour1, int minute1, int second1, int hour2, int minute2, int second2, String patern) {
        int second = second1 - second2;
        int minute = minute1 - minute2;
        int hour = hour1 - hour2;
        while (second < 0) {
            --minute;
            second += 60;
        }
        while (minute < 0) {
            --hour;
            minute += 60;
        }
        while (hour < 0) {
            hour += 24;
        }
        return patern.replace("HH", String.valueOf(hour)).replace("mm", String.valueOf(minute)).replace("ss", String.valueOf(second));
    }

    public static final String getTextChannelName(JDA jda, Long id) {
        int i = 0;
        while (i < jda.getGuilds().size()) {
            int j = 0;
            while (j < jda.getGuilds().get(i).getTextChannels().size()) {
                if (jda.getGuilds().get(i).getTextChannels().get(j).getIdLong() == id.longValue()) {
                    return String.valueOf(jda.getGuilds().get(i).getTextChannels().get(j).getName()) + " du serveur " + jda.getGuilds().get(i).getName();
                }
                ++j;
            }
            ++i;
        }
        return String.valueOf(id);
    }

    public static final MessageEmbed notLEA(Maeva save, JDA jda) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(jda.getGuildById(save.getLEAId()).getName(), "https://discord.me/chezlea", jda.getGuildById(save.getLEAId()).getIconUrl());
        embedBuilder.setDescription("Cette commande est disponible uniquement sur le serveur " + jda.getGuildById(save.getLEAId()).getName());
        embedBuilder.setColor(Color.MAGENTA);
        return embedBuilder.build();
    }

    public static final boolean isInt(String... toTest) {
        try {
        	for (String t : toTest)
        		Integer.parseInt(t);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static final boolean isLong(String toTest) {
        try {
            Long.parseLong(toTest);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static final boolean isModo(Member member) {
    	if (member.getUser().equals(getUser(member.getJDA(), UserProfession.CREATOR)))
    		return true;
    	if (member.hasPermission(Permission.ADMINISTRATOR) || member.isOwner())
    		return true;
    	return false;
    }

    public static int substractMonth(int year1, int month1, int year2, int month2) {
        int substractYear = year1 - year2;
        int substractMonthOnly = month1 - month2;
        return substractMonthOnly + substractYear * 12;
    }
    
    public static String getEmoteTag(JDA jda, String name) {
    	return jda.getEmotesByName(name, false).isEmpty() ? "{emote inconnu}" : jda.getEmotesByName(name, false).get(0).getAsMention();
    }
    
    public static String convertEmotes(JDA jda, String text) {
    	Pattern p = Pattern.compile("(?<=:)\\w+(?=:)");
    	Matcher m = p.matcher(text);
    	while (m.find()) {
    		String emote = m.group();
    		text = text.replaceFirst(":"+emote+":", getEmoteTag(jda, emote));
    	}
    	return text;
    }
    
    public static Message getMessage(TextChannel channel, String id, int decal) {
    	try {
    		if (decal == 0)
    			return channel.getHistoryAround(id, 1).complete().getRetrievedHistory().get(0);
    		else if (decal < 0)
    			return channel.getHistoryBefore(id, 1-decal).complete().getRetrievedHistory().get(0);
    		else
    			return channel.getHistoryAfter(id, decal).complete().getRetrievedHistory().get(decal-1);
    	} catch (Exception e) {
    		return null;
    	}
    }
    
    public static List<Message> getMessagesAfter(MessageChannel channel, String id) {
    	List<Message> msgs = invert(channel.getHistoryAfter(id, 100).complete().getRetrievedHistory());
    	if (msgs.size() % 100 == 0 && msgs.size() > 0)
    		msgs.addAll(getMessagesAfter(channel, msgs.get(msgs.size()-1).getId()));
    	return msgs;
    }
    
    public static List<Message> getMessagesBefore(MessageChannel channel, String id, int limit) {
    	List<Message> msgs = channel.getHistoryBefore(id, Math.min(100, limit)).complete().getRetrievedHistory();
    	if (msgs.size() % 100 == 0 && msgs.size() > 0)
    		msgs.addAll(getMessagesBefore(channel, msgs.get(msgs.size()-1).getId(), limit-msgs.size()));
    	return msgs;
    }
    
    public static <T> List<T> invert(List<T> list) {
    	List<T> newList = new ArrayList<T>();
    	for (int i = list.size()-1; i >= 0; i--) {
    		newList.add(list.get(i));
    	}
		return newList;
    }
    
    public static MessageReaction getReaction(Message m, String name) {
    	for (MessageReaction r : m.getReactions())
    		if (r.getReactionEmote().getName().equalsIgnoreCase(name))
    			return r;
    	return null;
    }
    
    public static String getChannelTag(Guild guild, String id) {
    	for (TextChannel c : guild.getTextChannels())
    		if (c.getId().equals(id))
    			return c.getAsMention();
    	return "{salon inconnu}";
    }
    
}
