/*
 * Decompiled with CFR 0_123.
 */
package fr.ptlc.giphyapi.json;

public class Looping {
    private String mp4_size;
    private String mp4;

    public String getMp4_size() {
        return this.mp4_size;
    }

    public void setMp4_size(String mp4_size) {
        this.mp4_size = mp4_size;
    }

    public String getMp4() {
        return this.mp4;
    }

    public void setMp4(String mp4) {
        this.mp4 = mp4;
    }

    public String toString() {
        return "[mp4_size = " + this.mp4_size + ", mp4 = " + this.mp4 + "]";
    }
}

