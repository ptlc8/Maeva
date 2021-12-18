package fr.ptlc.maeva.data;

import fr.ptlc.maeva.data.Article;
import fr.ptlc.maeva.data.Client;
import fr.ptlc.maeva.data.Role;
import fr.ptlc.maeva.data.Server;

import java.util.List;

public class Maeva {
	private Options options;
	private Data data;
	private String LEAId = "294055260501311498";

	public Maeva(String consoleMode, List<Client> clients, List<Server> servers, int resetHour, int ticketPrice, List<Integer> ticketProb, List<String> his, List<Article> articles, List<Role> roles) {
		this.options = new Options(consoleMode, resetHour, his);
		this.data = new Data(clients, servers, roles);
	}
	
	public Maeva(Options options, Data data) {
		this.options = options;
		this.data = data;
	}

	public Options getOptions() {
		return options;
	}
	
	public Data getData() {
		return data;
	}

	public String getLEAId() {
		return this.LEAId;
	}

	public String toString() {
		return "[" + this.options.toString() + ", " + this.data.toString() + "]";
	}
}

