package fr.ptlc.maeva;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;

public class Main {
	
	private static final String version = "1.4.3";
	private static final String creationDate = "09/09/2017";
	private static final String date = "21/05/2021";
	
	public static void main(String args[]) {
		JDA jda;
		System.out.println("->Maeva " + getVersion() + ", par PTLC_, le " + getDate() + ", pour ->Lykos_Archos, vive la PogonaArmy !");
		jda = null;
		try {
			jda = JDABuilder.createDefault(Tokens.DISCORD).build();
			//jda = (new JDABuilder(AccountType.BOT)).setToken(Tokens.DISCORD).build();
			System.out.println("B0t connect� !");
			jda.addEventListener(new BotListener());
			jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.of(ActivityType.WATCHING, "ambi.dev/maeva"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getVersion() {
		return version;
	}
	
	public static String getDate() {
		return date;
	}

}
