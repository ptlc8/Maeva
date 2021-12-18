package fr.ptlc.maeva;

import fr.ptlc.maeva.OrderReply;
import fr.ptlc.maeva.Functions;
import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.values.UserProfession;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class ChatReply {
	
    public static List<OrderReply> orderReplies = new ArrayList<OrderReply>();

    public Integer getOrderReplyId(User author, MessageChannel messageChannel) {
        for (int i = 0; i < orderReplies.size(); i++)
            if (orderReplies.get(i).getAuthor().equals(author) && orderReplies.get(i).getMessageChannel().equals(messageChannel))
                return i;
        return null;
    }

    public OrderReply getOrderReply(User author) {
        for (OrderReply o : orderReplies)
            if (o.getAuthor().equals(author))
                return o;
        return null;
    }

    public Maeva reply(Maeva save, Guild guild, MessageChannel messageChannel, String content, User user, JDA jda) {
        int userListId = this.getOrderReplyId(user, messageChannel);
        if (content.equals("")) {
            messageChannel.sendMessage(String.valueOf(user.getAsMention()) + ", " + orderReplies.get(userListId).getAllArgs().get(0) + " ? (`cancel` pour annuler)").queue();
            return null;
        }
        if (content.equals("cancel")) {
            orderReplies.remove(userListId);
            messageChannel.sendMessage(String.valueOf(user.getAsMention()) + " a annul\u00e9 son achat :sob:").queue();
            return null;
        }
        orderReplies.get(userListId).addNewArgs(content);
        if (orderReplies.get(userListId).getAllArgs().size() == orderReplies.get(userListId).getAlreadyArgs().size()) {
            save.getData().getServerById(guild.getId()).getClientById(user.getId()).removeCredits(orderReplies.get(userListId).getArticle().getPrice());
            messageChannel.sendMessage(String.valueOf(user.getAsMention()) + ", vous avez achet\u00e9 " + orderReplies.get(userListId).getArticle().getName() + " " + orderReplies.get(userListId).getAlreadyArgs().toString()).queue();
            Functions.sendPrivate(jda, UserProfession.LIVREUR, String.valueOf(user.getName()) + " a achet\u00e9 " + orderReplies.get(userListId).getArticle().getName() + " " + orderReplies.get(userListId).getAlreadyArgs().toString());
            orderReplies.remove(userListId);
            return save;
        }
        messageChannel.sendMessage(String.valueOf(user.getAsMention()) + ", " + orderReplies.get(userListId).getAllArgs().get(orderReplies.get(userListId).getAlreadyArgs().size()) + " ? (`cancel` pour annuler)").queue();
        return null;
    }
}

