package fr.ptlc.maeva.commands;

import fr.ptlc.maeva.ChatReply;
import fr.ptlc.maeva.OrderReply;
import fr.ptlc.maeva.Functions;
import fr.ptlc.maeva.MessageAndSave;
import fr.ptlc.maeva.data.Article;
import fr.ptlc.maeva.data.Client;
import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.data.Server;
import fr.ptlc.maeva.values.UserProfession;

import java.awt.Color;
import java.util.ArrayList;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Shop {
    public MessageAndSave add(Maeva save, String[] args, Guild guild, boolean isOp) {
        if (!isOp)
            return new MessageAndSave("#cadena# Vous n'\u00eates pas administrateur b0t", save);
        if (args.length <= 2)
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#shop add <nomDeL'article> [prix] image:<url image> description:\"<description>\" <argument1> <argument2> ...", save);
        try {
            Integer.parseInt(args[3].trim());
            boolean haveDescription = false;
            String description = "";
            boolean haveImageUrl = false;
            String imageUrl = "";
            ArrayList<String> tmpList = new ArrayList<String>();
            for (int i = 4; i < args.length; i++) {
                if (args[i].startsWith("description:\"")) {
                    if (!haveDescription) {
                        if (!args[i].endsWith("\"")) {
                            description = args[i].replaceFirst("description:\"", "");
                            for (int j = i + 1; !args[j].endsWith("\""); j++) {
                                description = String.valueOf(description) + " " + args[j];
                                i++;
                            }
                            description = String.valueOf(description) + " " + args[++i].replace("\"", "");
                        }
                        haveDescription = true;
                    }
                } else if (args[i].startsWith("image:")) {
                    if (!haveImageUrl) {
                        imageUrl = args[i].replace("image:", "");
                        haveImageUrl = true;
                    }
                } else
                    tmpList.add(args[i]);
            }
            save.getData().getServerById(guild.getId()).getShop().addArticle(new Article(args[2], Integer.parseInt(args[3]), description, imageUrl, tmpList));
            return new MessageAndSave("#colis# L'article \"" + args[2] + "\" a \u00e9t\u00e9 ajout\u00e9 avec pour prix " + args[3] + " cr\u00e9dits, pour image " + imageUrl + ", pour description \"" + description + "\" et pour arguments \"" + tmpList.toString() + "\" avec pour id " + (save.getData().getServerById(guild.getId()).getShop().getArticles().size() - 1), save);
        }
        catch (NumberFormatException e) {
            return new MessageAndSave("#syntaxe# Syntaxe : #prefix#shop add " + args[2] + " [prix] ...", save);
        }
    }

    public MessageAndSave remove(Maeva save, boolean isOp, String[] args, Guild guild) {
        if (!isOp)
            return new MessageAndSave("#cadena# Vous n'\u00eates pas administrateur b0t", save);
        if (args.length < 3 || !Functions.isInt(args[2]))
        	return new MessageAndSave("#syntaxe# Syntaxe : #prefix#shop remove [id]", save);
        fr.ptlc.maeva.data.Shop shop = save.getData().getServerById(guild.getId()).getShop();
        if (Integer.parseInt(args[2]) >= shop.getArticles().size() || Integer.parseInt(args[2]) < 0)
        	return new MessageAndSave("#erreur# Erreur : Cet identifiant ne correspond à aucun article", save);
        String exArticleName = shop.getArticleById(Integer.parseInt(args[2])).getName();
        shop.removeArticle(Integer.parseInt(args[2]));
        return new MessageAndSave("#colis# L'article " + exArticleName + ", ayant pour id " + args[2] + ", \u00e0 bien \u00e9t\u00e9 supprim\u00e9 \nAttention, les articles restants peuvent avoir chang\u00e9s d'id", save);
    }

    public MessageAndSave buy(Maeva save, Guild guild, String[] args, User author, MessageChannel messageChannel, JDA jda, int alias) {
    	ChatReply chatReply = new ChatReply();
    	if (args.length < 3+alias || !Functions.isInt(args[2 + alias]))
    		return new MessageAndSave("#syntaxe# Syntaxe : #prefix#buy [id]", save);
    	int id = Integer.parseInt(args[2 + alias]);
    	fr.ptlc.maeva.data.Shop shop = save.getData().getServerById(guild.getId()).getShop();
    	if (id >= shop.getArticles().size())
    		return new MessageAndSave("#erreur# Erreur : Cet identifiant ne correspond \u00e0 aucun article", save);
    	Server server = save.getData().getServerById(guild.getId());
    	Client client = server.getClientById(author.getId());
    	if (client == null)
    		server.addClient(new Client(author.getId(), 0));
    	if (shop.getArticleById(id).getPrice() > client.getCredits())
    		return new MessageAndSave("#colis# D\u00e9sol\u00e9, vous n'avez pas assez de cr\u00e9dits", save);
    	if (shop.getArticleById(id).getArgs().isEmpty()) {
    		client.removeCredits(shop.getArticleById(id).getPrice());
    		Functions.sendPrivate(jda, UserProfession.LIVREUR, "#colis# " + author.getName() + " a achet\u00e9 " + shop.getArticleById(id).getName());
    		return new MessageAndSave("#colis# " + author.getAsMention() + ", vous avez achet\u00e9 " + shop.getArticleById(id).getName(), save);
    	}
    	if (chatReply.getOrderReplyId(author, messageChannel) != null)
    		return new MessageAndSave("#erreur# Erreur : Vous avez d\u00e9j\u00e0 une commande en enregistrement dans le channel " + chatReply.getOrderReply(author).getMessageChannel().getName(), null);
    	ChatReply.orderReplies.add(new OrderReply(author, messageChannel, shop.getArticleById(id).getArgs(), new ArrayList<String>(), shop.getArticleById(id)));
    	chatReply.reply(save, guild, messageChannel, "", author, jda);
    	return new MessageAndSave("", save);
    }

    public MessageEmbed print(Maeva save, Guild guild, JDA jda) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        fr.ptlc.maeva.data.Shop shop = save.getData().getServerById(guild.getId()).getShop();
        if (shop.getArticles().size() == 0) {
            embedBuilder.setTitle("La boutique est actuellement vide");
            embedBuilder.setDescription("Veuillez contacter un administrateur pour plus d'informations");
            return embedBuilder.build();
        }
        embedBuilder.setTitle("Boutique");
        embedBuilder.setDescription("Ici, vous pouvez acheter tout pleins d'article propos\u00e9s par " + guild.getName());
        for (Article article : shop.getArticles())
            embedBuilder.addField(String.valueOf(article.getName()) + " (" + article.getPrice() + "cr\u00e9dits)   ", "id : " + shop.getArticles().indexOf(article) + " - " + article.getDescription(), false);
        embedBuilder.setColor(Color.MAGENTA);
        return embedBuilder.build();
    }
}

