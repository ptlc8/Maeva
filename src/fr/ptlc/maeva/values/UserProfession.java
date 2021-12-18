package fr.ptlc.maeva.values;

public enum UserProfession {
	
    CREATOR(702224551433732216L), // ex : 329352394301374465L
    LIVREUR(186494406742900736L);
    
    private long id;
    
    UserProfession(long id) {
    	this.id = id;
    }
    
    public long getId() {
    	return id;
    }
    
}

