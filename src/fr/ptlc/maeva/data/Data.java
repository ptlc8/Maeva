package fr.ptlc.maeva.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class Data {
	
	private List<String> ops = new ArrayList<String>();
	private List<Server> servers;
	private List<Role> roles;
	private int lastDailyReset;
	
	public Data(List<Client> clients, List<Server> servers, List<Role> roles) {
		this.servers = servers;
		this.roles = roles;
		this.lastDailyReset = 0;
	}

	public List<String> getOps() {
		if (ops == null) ops = new ArrayList<String>();
		return ops;
	}

	public boolean isOp(String id) {
		return ops != null && ops.contains(id);
	}

	public List<Server> getServers() {
		return servers != null ? Collections.unmodifiableList(servers) : new ArrayList<Server>();
	}

	public Server getServerById(String id) {
		for (Server server :  servers)
			if (server.getId().equals(id))
				return server;
		return null;
	}

	public void addServer(Server server) {
		servers.add(server);
	}

	public List<Role> getRoles() {
		return roles != null ? Collections.unmodifiableList(roles) : new ArrayList<Role>();
	}

	public Role getRoleById(String id) {
		for (Role role : roles)
			if (role.getId().equals(id))
				return role;
		return null;
	}

	public boolean isRegisteredRole(String id) {
		for (Role role : roles)
			if (role.getId().equals(id))
				return true;
		return false;
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	public void removeRole(String id) {
		for (Role role : roles)
			if (role.getId().equals(id))
				roles.remove(role);
	}
	
	public boolean isTimeToResetDailies(int resetHour) {
		GregorianCalendar calendar = new GregorianCalendar();
		int today = calendar.get(Calendar.YEAR)*10000+calendar.get(Calendar.MONTH)*100+calendar.get(Calendar.DAY_OF_MONTH);
		return (today > lastDailyReset+1) || (today > lastDailyReset && calendar.get(Calendar.HOUR_OF_DAY) >= resetHour);
	}

	public void resetAllSalaire() {
		for (Server server : servers) for (Client client : server.getClients())
			client.resetTakeSalaire();
		GregorianCalendar calendar = new GregorianCalendar();
		lastDailyReset = calendar.get(Calendar.YEAR)*10000+calendar.get(Calendar.MONTH)*100+calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public String toString() {
		return "[" + this.ops.toString() + ", " + this.servers.toString() + ", " + this.roles.toString() + ", " + this.lastDailyReset + "]";
	}
	
}
