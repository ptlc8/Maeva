package fr.ptlc.maeva.data;

import fr.ptlc.maeva.data.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class Server {
    private String id;
    private String prefix = "maeva ";
    
    private List<Rule> rules = new ArrayList<Rule>();
    private String rulesUpdateDate;
    private String rulesUpdaterId;
    
    private List<String> autoRoles = new ArrayList<String>();
    
	private Shop shop = new Shop();
	private List<Client> clients = new ArrayList<Client>();
	
	private int ticketPrice = 10;
	private List<Integer> ticketProb = new ArrayList<Integer>();
	
    private String antiSpamRequest = "";
    private String antiSpamChannelId = null;
    private String antiSpamRoleId = null;
    private String antiSpamEmote = "";
    private List<String> antiSpamEmotes = new ArrayList<String>();
    
    private boolean isLEA = false;

    public Server(String id) {
        this.id = id;
        this.prefix = "maeva ";
        this.rules = new ArrayList<Rule>();
        this.autoRoles = new ArrayList<String>();
        this.isLEA = id.equals("294055260501311498");
    }

    public String getId() {
        return this.id;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<Rule> getRules() {
    	return rules != null ? Collections.unmodifiableList(rules) : new ArrayList<Rule>();
    }

    public String getRulesUpdateDate() {
        return this.rulesUpdateDate;
    }

    public String getRulesUpdaterId() {
        return this.rulesUpdaterId;
    }

    public void setRules(List<Rule> rules, String idUpdater) {
        this.rules = rules;
        this.rulesUpdaterId = idUpdater;
        GregorianCalendar calendar = new GregorianCalendar();
        this.rulesUpdateDate = String.valueOf(calendar.get(5)) + "/" + (calendar.get(2) + 1) + "/" + calendar.get(1);
    }
    
    public List<String> getAutoRoles() {
        return autoRoles != null ? Collections.unmodifiableList(autoRoles) : new ArrayList<String>();
    }

    public String getAutoRoleById(String name) {
        for (String autoRole : autoRoles)
            if (autoRole.equals(name))
                return autoRole;
        return null;
    }

    public void addAutoRole(String roleName) {
    	if (autoRoles == null) autoRoles = new ArrayList<String>();
        autoRoles.add(roleName);
    }
    
    public void removeAutoRole(String roleName) {
    	autoRoles.remove(roleName);
    }
	
	public Shop getShop() {
		if (shop == null) shop = new Shop();
		return shop;
	}
	
	public List<Client> getClients() {
		if (clients == null) clients = new ArrayList<Client>();
		return Collections.unmodifiableList(clients);
	}

	public Client getClientById(String id) {
		if (clients == null) clients = new ArrayList<Client>();
		for (Client client : clients)
			if (client.getId().equals(id))
				return client;
		return null;
	}
	
	public void addClient(Client client) {
		clients.add(client);
	}

	public int getAllCredits() {
		if (clients == null) clients = new ArrayList<Client>();
		int allCredits = 0;
		for (Client client : clients)
			allCredits += client.getCredits();
		return allCredits;
	}

	public int getTicketPrice() {
		return this.ticketPrice;
	}

	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public List<Integer> getTicketProb() {
		return ticketProb != null ? Collections.unmodifiableList(ticketProb) : new ArrayList<Integer>();
	}

	public void setTicketProb(List<Integer> ticketProb) {
		this.ticketProb = ticketProb;
	}
    
    public String getAntiSpamRequest() {
    	return antiSpamRequest;
    }
    
    public String getAntiSpamChannelId() {
    	return antiSpamChannelId;
    }
    
    public String getAntiSpamRoleId() {
    	return antiSpamRoleId;
    }
    
    public String getAntiSpamEmote() {
    	return antiSpamEmote;
    }
    
    public List<String> getAntiSpamEmotes() {
    	return Collections.unmodifiableList(antiSpamEmotes);
    }
    
    public void setAntiSpam(String request, String channelId, String roleId, String emote, String... emotes) {
    	this.antiSpamRequest = request;
    	this.antiSpamChannelId = channelId;
    	this.antiSpamRoleId = roleId;
    	this.antiSpamEmote = emote;
    	this.antiSpamEmotes = Arrays.asList(emotes);
    }

    public boolean isLEA() {
        return this.isLEA;
    }

    public String toString() {
        return "[" + this.id + ", " + this.prefix + ", " + this.rules + ", " + this.rulesUpdateDate + ", " + this.rulesUpdaterId + ", " + shop + ", " + clients + ", " + ticketPrice + ", " + ticketProb.toString() + ", " + this.autoRoles + ", " + this.isLEA + "]";
    }
}

