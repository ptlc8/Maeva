/*
 * Decompiled with CFR 0_123.
 */
package fr.ptlc.maeva.data;

import java.awt.Color;

public class Rule {
    String title;
    String description;
    Color color;

    public Rule(String title, String desciption, Color color) {
        this.title = title;
        this.description = desciption;
        this.color = color;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Color getColor() {
        return this.color;
    }

    public String toString() {
        return "[" + this.title + ", " + this.description + " ," + this.color + "]";
    }
}

