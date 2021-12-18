package fr.ptlc.maeva.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Options {
	
	private String consoleMode;
	private int resetHour = 3;
	private List<String> his;
	
	private String commandsChannelId = "";  // 
	private String orderChannelId = "";     // 
	private String emote1 = "";             // Only LEA :thinking:
	private String emote2 = "";             // 
	private String emote3 = "";             // 
	
	public Options(String consoleMode, int resetHour, List<String> his) {
		this.consoleMode = consoleMode;
		this.resetHour = resetHour;
		this.his = his;
	}
	
	public String getConsoleMode() {
		return this.consoleMode;
	}

	public void setConsoleMode(String consoleMode) {
		this.consoleMode = consoleMode;
	}
	
	public int getResetHour() {
		return this.resetHour;
	}

	public void setResetHour(int resetHour) {
		this.resetHour = resetHour;
	}

	public List<String> getHis() {
		return his != null ? Collections.unmodifiableList(his) : new ArrayList<String>();
	}

	public void setHis(List<String> his) {
		this.his = his;
	}
	
	public String getCommandsChannelId() {
		return commandsChannelId;
	}
	
	public String getOrderChannelId() {
		return orderChannelId;
	}
	
	public String getEmote1Name() {
		return emote1;
	}
	
	public String getEmote2Name() {
		return emote2;
	}
	
	public String getEmote3Name() {
		return emote3;
	}	
	
	public String toString() {
		return "[" + this.consoleMode + ", " + this.resetHour + ", " + this.his.toString() + "]";
	}
	
}
