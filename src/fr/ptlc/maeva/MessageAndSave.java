/*
 * Decompiled with CFR 0_123.
 */
package fr.ptlc.maeva;

import fr.ptlc.maeva.data.Maeva;

public class MessageAndSave {
    String message;
    Maeva save;

    public MessageAndSave(String message, Maeva save) {
        this.message = message;
        this.save = save;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Maeva getSave() {
        return this.save;
    }

    public void setMaeva(Maeva save) {
        this.save = save;
    }
}

