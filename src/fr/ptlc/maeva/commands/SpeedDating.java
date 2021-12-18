package fr.ptlc.maeva.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.ptlc.maeva.Functions;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class SpeedDating {
	
	private static Map<String, Map<String, Person>> preSpeedDatings = new HashMap<String, Map<String, Person>>();
	private static Map<String, List<List<Date>>> speedDatings = new HashMap<String, List<List<Date>>>();
	
	public static String syntax = "#syntaxe# Syntaxe : #prefix#speeddating <join|leave|prelist|start>";
	public static String joinSyntax = "#syntaxe# Syntaxe : #prefix#speeddating join <âge> <sexe> <orientation>\n(homme, femme ou non-binaire ; hommes, femmes ou pansexuel)";
	public static String leaveSyntax = "#syntaxe# Syntaxe : #prefix#speeddating leave OU #prefix#speeddating <@participant	>";
	
	private static List<String> maleValues = Arrays.asList("homme","2","man","male","mâle");
	private static List<String> femaleValues = Arrays.asList("femme","3","woman","female","femelle");
	private static List<String> nbValues = Arrays.asList("non-binaire","1","non-binary","autre","other");
	private static List<String> malesValues = Arrays.asList("hommes","2","men","males","mâles");
	private static List<String> femalesValues = Arrays.asList("femmes","3","women","females","femelles");
	private static List<String> panValues = Arrays.asList("pansexuel","6","pansexuelle","panromantique","pansexual","panromantique");
	
	public String join(Guild guild, User user, String[] args) {
		if (speedDatings.get(guild.getId()) != null)
			return "Un speed dating est déjà en cours sur ce serveur.";
		if (preSpeedDatings.get(guild.getId()) == null)
			preSpeedDatings.put(guild.getId(), new HashMap<String, SpeedDating.Person>());
		boolean was = false;
		if (args.length < 5) return joinSyntax;
		int gender;
		if (maleValues.contains(args[3]))
			gender = 2;
		else if (femaleValues.contains(args[3]))
			gender = 3;
		else if (nbValues.contains(args[3]))
			gender = 1;
		else
			return "#erreur# Le sexe doit être *homme* ou *femme* ou *non-binaire*.";
			//+"\n("+String.join(",", maleValues)+","+String.join(",", femaleValues)+","+String.join(",", nbValues)+")";
		int orientation;
		if (malesValues.contains(args[4]))
			orientation = 2;
		else if (femalesValues.contains(args[4]))
			orientation = 3;
		else if (panValues.contains(args[4]))
			orientation = 6;
		else
			return "#erreur# L'orientation doit être un nombre ou *homme* ou *femme* ou *pansexuel*.\n(homme : 2 ; femme : 3 ; pansexuel : 6)";
		if (!Functions.isInt(args[2]))
			return "#erreur# L'âge doit être un nombre.";
		if (preSpeedDatings.get(guild.getId()).get(user.getId()) != null)
			was = true;
		preSpeedDatings.get(guild.getId()).put(user.getId(), new Person(user.getId(), Integer.parseInt(args[2]), gender, orientation));
		if (was)
			return "#coeur# " + user.getAsMention() + ", ton inscription au speed dating a bien été mise à jour !";
		return "#coeur# " + user.getAsMention() + ", tu es désormais inscrit(e) au speed dating !";
	}
	
	public String leave(boolean isModo, Guild guild, User user, Message message) {
		if (speedDatings.get(guild.getId()) != null)
			return "Un speed dating est déjà en cours sur ce serveur.";
		if (preSpeedDatings.get(guild.getId()) == null)
			preSpeedDatings.put(guild.getId(), new HashMap<String, SpeedDating.Person>());
		if (!isModo && message.getMentionedUsers().size() > 0)
			return "#erreur# Seuls les admins serveur peuvent désinscrire les autres";
		boolean byItSelf = message.getMentionedUsers().size() <= 0;
		User userToLeave = byItSelf ? user : message.getMentionedUsers().get(0);
		if (preSpeedDatings.get(guild.getId()).get(userToLeave.getId()) == null)
			return "#erreur# " + (byItSelf?"Tu n'es":userToLeave.getName()+"n'est") + " pas incrit(e) au speed dating...";
		preSpeedDatings.get(guild.getId()).remove(userToLeave.getId());
		return ":broken_heart: " + (byItSelf?userToLeave.getAsMention()+", tu es":userToLeave.getName()+" est") + " désormais désinscrit(e) du speed dating !";
	}
	
	public String prelist(Guild guild) {
		if (speedDatings.get(guild.getId()) != null)
			return "Un speed dating est déjà en cours sur ce serveur.";
		if (preSpeedDatings.get(guild.getId()) == null)
			preSpeedDatings.put(guild.getId(), new HashMap<String, SpeedDating.Person>());
		String response = ":heart: __Voici la liste des inscrits :__";
		Collection<Person> persons = preSpeedDatings.get(guild.getId()).values();
		//persons.removeIf(p -> guild.getMemberById(p.discordId)==null);
		persons.removeIf(p -> guild.retrieveMemberById(p.discordId).complete()==null);
		for (Person person : persons) {
			response += "\n - " + guild.retrieveMemberById(person.discordId).complete().getUser().getName() + ", "
					+ person.age + " ans, "
					+ (person.sex==2?"homme":person.sex==3?"femme":person.sex==1?"non-binaire":"autre") + ", cherche "
					+ (person.orient==2?"homme":person.orient==3?"femme":person.orient==6?"tout":"autre");
		}
		return response;
	}
	
	public String start(boolean isModo, Guild guild, User author, MessageChannel channel, String[] args) {
		if (!isModo)
			return "#cadena# Vous n'êtes pas administrateur serveur";
		if (speedDatings.get(guild.getId()) != null)
			return "Un speed dating est déjà en cours sur ce serveur.";
		if (args.length < 3)
			return "#syntaxe# Syntaxe : #prefix#speeddating start [categoryId]";
		Category category = guild.getCategoryById(args[2]);
		if (category == null)
			return "#erreur# Aucune catégorie ne correspond à cet indentifiant";
		if (!guild.retrieveMember(author).complete().getVoiceState().inVoiceChannel())
			return "#erreur# Vous devez être dans un salon vocal où les participants seront déplacés pour attendre leurs dates";
		if (!guild.getSelfMember().hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS, Permission.MESSAGE_WRITE, Permission.VOICE_MOVE_OTHERS))
			return "#erreur Il me faut ces permissions : MANAGE_CHANNEL, MANAGE_PERMISSIONS, MESSAGE_WRITE, VOICE_MOVE_OTHERS";
		VoiceChannel hubDating = guild.getMember(author).getVoiceState().getChannel();
		List<Person> persons = preSpeedDatings.get(guild.getId())==null
				? new ArrayList<SpeedDating.Person>()
				: new ArrayList<SpeedDating.Person>(preSpeedDatings.get(guild.getId()).values());
		persons.removeIf(p -> guild.retrieveMemberById(p.discordId).complete()==null);
		if (persons.size() < 2)
			return "#erreur# Le speed dating manque d'inscriptions, actuellement moins de 2...";
		List<Date> dates = new ArrayList<SpeedDating.Date>();
	    for (int i = 0; i < persons.size(); i++) {
	        for (int j = i+1; j < persons.size(); j++) {
	            if (persons.get(i).orient % persons.get(j).sex != 0 || persons.get(j).orient % persons.get(i).sex != 0) continue;
	            if ((persons.get(i).age < 18 || persons.get(j).age < 18) && Math.abs(persons.get(i).age-persons.get(j).age) > 2) continue;
	            if (Math.abs(persons.get(i).age-persons.get(j).age) > 4) continue;
	            dates.add(new Date(persons.get(i), persons.get(j)));
	        }
	    }
	    List<List<Date>> hours = new ArrayList<List<Date>>();
	    for (Date date : dates) {
	        for (int i = 0; ; i++) {
	            if (hours.size() <= i) hours.add(new ArrayList<SpeedDating.Date>());
	            boolean free = true;
	            for (int j = 0; j < hours.get(i).size(); j++) {
	                if (hours.get(i).get(j).isIncompatible(date))
	                    free = false;
	            }
	            if (!free) continue;
	            hours.get(i).add(date);
	            break;
	        }
	    }
	    preSpeedDatings.remove(guild.getId());
	    speedDatings.put(guild.getId(), hours);
	    int i = 1;
	    int jmax = 0;
	    for (List<Date> hour : hours) {
	    	String textPlanning = "__**Horaire " + i + " :**__ (**" + hour.size() + "** dates)\n";
			int j = 0;
			for (Date date : hour) {
				j++;
				Member member1 = guild.getMemberById(date.pers1.discordId);
				Member member2 = guild.getMemberById(date.pers2.discordId);
				textPlanning += member1.getAsMention() + " et " + member2.getAsMention() + "\n";
				int ih = i;
				int jh = j;
				Thread t = new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep((ih*6-5)*60000+jh*100);
						} catch (InterruptedException e) {e.printStackTrace();}
						VoiceChannel voiceChannel = category.createVoiceChannel("Date n°" + jh).complete();
						voiceChannel.getManager().setUserLimit(2).queue();
						voiceChannel.getManager().putPermissionOverride(member1, Permission.VOICE_CONNECT.getRawValue(), 0).complete();
						voiceChannel.getManager().putPermissionOverride(member2, Permission.VOICE_CONNECT.getRawValue(), 0).complete();
						voiceChannel.getManager().putPermissionOverride(member1, Permission.VOICE_CONNECT.getRawValue(), 0).complete(); // re
						voiceChannel.getManager().putPermissionOverride(member2, Permission.VOICE_CONNECT.getRawValue(), 0).complete(); // re
						channel.sendMessage(":heart: " + member1.getAsMention() + " et " + member2.getAsMention() + " vous avez date dans le salon vocal `" + voiceChannel.getName() + "` ! ^^").queue();
						if (member1.getVoiceState().inVoiceChannel())
							guild.moveVoiceMember(member1, voiceChannel).queue();
						if (member2.getVoiceState().inVoiceChannel())
							guild.moveVoiceMember(member2, voiceChannel).queue();
						Thread t = new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(330*1000);
								} catch (InterruptedException e) {e.printStackTrace();}
								if (member1.getVoiceState().inVoiceChannel())
									guild.moveVoiceMember(member1, hubDating).queue();
								if (member2.getVoiceState().inVoiceChannel())
									guild.moveVoiceMember(member2, hubDating).queue();
								voiceChannel.delete().queueAfter(30, TimeUnit.SECONDS);
							}
						});
						t.start();
					}
				});
				t.start();
				if (j > jmax) jmax = j;
			}
			channel.sendMessage(":heart: C'est la fin du date !!! " + (i<dates.size() ? "Une minute avant le prochain et **30 secondes** avant le transfert dans "+hubDating.getName()+". :)" : "\n Tous les dates ont eu lieu ! :heart_eyes:")).queueAfter(i*6, TimeUnit.MINUTES);
			if (i<dates.size()) {
				channel.sendMessage("10 secondes...").queueAfter(i*6*60+20, TimeUnit.SECONDS);
				channel.sendMessage("5 secondes...").queueAfter(i*6*60+25, TimeUnit.SECONDS);
				channel.sendMessage("3 secondes...").queueAfter(i*6*60+27, TimeUnit.SECONDS);
				channel.sendMessage("2 secondes...").queueAfter(i*6*60+28, TimeUnit.SECONDS);
				channel.sendMessage("1 secondes...").queueAfter(i*6*60+29, TimeUnit.SECONDS);
			}
			channel.sendMessage(textPlanning).queueAfter(i*100, TimeUnit.MILLISECONDS);
			i++;
		}
	    Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000*60*6*dates.size());
					speedDatings.remove(guild.getId());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	    t.start();
	    String textPlanning = "\n**Nombre de participants :** " + persons.size();
	    textPlanning += "\n**Nombre de dates :** " + dates.size();
	    textPlanning += "\n**Durée du speed dating :** " + (hours.size()*6) + " minutes";
	    textPlanning += "\n**Maximum de date en même temps :** " + jmax;
	    channel.sendMessage(textPlanning).queueAfter(i*100, TimeUnit.MILLISECONDS);
	    return null;
	}
	
	private class Date {
		Person pers1;
		Person pers2;
		public Date(Person pers1, Person pers2) {
			this.pers1 = pers1;
			this.pers2 = pers2;
		}
		public boolean contains(Person person) {
			return pers1 == person || pers2 == person;
		}
		public boolean isIncompatible(Date date) {
			return contains(date.pers1) || contains(date.pers2);
		}
	}
	
	private class Person {
		String discordId;
		int age;
		int sex;
		int orient;
		public Person(String discordId, int age, int sex, int orient) {
			this.discordId = discordId;
			this.age = age;
			this.sex = sex;
			this.orient = orient;
		}
	}
	
}
