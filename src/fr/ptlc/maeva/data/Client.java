package fr.ptlc.maeva.data;

public class Client {
    private String id;
    private int credits;
    private boolean hadTakeSalaire;
    private int xp = 2;

    public Client(String id, int credits) {
        this.id = id;
        this.credits = credits;
        this.hadTakeSalaire = false;
    }

    public String getId() {
        return this.id;
    }
    
    public int getXp() {
    	return xp;
    }
    
    public void setXp(int xp){
    	this.xp = xp;
    }
    
    public int getCredits() {
        return this.credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void addCredits(int addCredits) {
        this.credits += addCredits;
    }

    public void removeCredits(int removeCredits) {
        this.credits -= removeCredits;
    }

    public boolean hadTakeSalaire() {
        return this.hadTakeSalaire;
    }

    public void takeSalaire() {
        this.hadTakeSalaire = true;
    }

    public void resetTakeSalaire() {
        this.hadTakeSalaire = false;
    }

    public String toString() {
        return "[" + this.id + ", " + this.credits + ", " + this.hadTakeSalaire + ", " + this.xp + "]";
    }
}

