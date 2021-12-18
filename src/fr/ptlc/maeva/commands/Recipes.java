package fr.ptlc.maeva.commands;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Recipes {
	
	private static OkHttpClient client = new OkHttpClient();
	
	public static MessageEmbed getLast10() {
		Request request = new Request.Builder().url("https://cuisine-libre.fr/?page=distrib").build();
		String responseText;
		try {
			Response response = client.newCall(request).execute();
			responseText = response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		Pattern p = Pattern.compile("<li><a href=\"(.+?)\".+?>(.+?)<");
		Matcher m = p.matcher(responseText);
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Quelques recettes !", "");
		eb.setThumbnail("https://ambi.dev/maeva/maeva-toque.png");
		while (m.find())
			eb.appendDescription("["+m.group(2)+"]("+m.group(1)+")\n");
		//eb.appendDescription("");
		eb.setFooter("Depuis cuisine-libre.fr", "https://cuisine-libre.fr/promo/vu-sur-cuisine-libre.png");
		return eb.build();
	}
	
}
