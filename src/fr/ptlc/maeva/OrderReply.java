package fr.ptlc.maeva;

import fr.ptlc.maeva.data.Article;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class OrderReply {
    User author;
    MessageChannel messageChannel;
    List<String> allArgs;
    List<String> alreadyArgs;
    Article article;

    public OrderReply(User author, MessageChannel messageChannel, List<String> allArgs, List<String> alreadyArgs, Article article) {
        this.author = author;
        this.messageChannel = messageChannel;
        this.allArgs = allArgs;
        this.alreadyArgs = alreadyArgs;
        this.article = article;
    }

    public User getAuthor() {
        return this.author;
    }

    public MessageChannel getMessageChannel() {
        return this.messageChannel;
    }

    public List<String> getAllArgs() {
        return this.allArgs;
    }

    public List<String> getAlreadyArgs() {
        return this.alreadyArgs;
    }

    public void addNewArgs(String newArg) {
        this.alreadyArgs.add(newArg);
    }

    public Article getArticle() {
        return this.article;
    }
}

