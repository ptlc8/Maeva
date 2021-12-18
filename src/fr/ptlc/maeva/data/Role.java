/*
 * Decompiled with CFR 0_123.
 */
package fr.ptlc.maeva.data;

public class Role {
    String id;
    int salaire;

    public Role(String id, int salaire) {
        this.id = id;
        this.salaire = salaire;
    }

    public String getId() {
        return this.id;
    }

    public int getSalaire() {
        return this.salaire;
    }

    public void setSalaire(int salaire) {
        this.salaire = salaire;
    }

    public String toString() {
        return "[" + this.id + ", " + this.salaire + "]";
    }
}

