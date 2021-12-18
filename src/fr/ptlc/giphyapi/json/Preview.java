/*
 * Decompiled with CFR 0_123.
 */
package fr.ptlc.giphyapi.json;

public class Preview {
    private String height;
    private String mp4_size;
    private String width;
    private String mp4;

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMp4_size() {
        return this.mp4_size;
    }

    public void setMp4_size(String mp4_size) {
        this.mp4_size = mp4_size;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getMp4() {
        return this.mp4;
    }

    public void setMp4(String mp4) {
        this.mp4 = mp4;
    }

    public String toString() {
        return "[height = " + this.height + ", mp4_size = " + this.mp4_size + ", width = " + this.width + ", mp4 = " + this.mp4 + "]";
    }
}

