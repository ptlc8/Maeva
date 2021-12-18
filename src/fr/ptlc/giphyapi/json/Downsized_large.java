/*
 * Decompiled with CFR 0_123.
 */
package fr.ptlc.giphyapi.json;

public class Downsized_large {
    private String height;
    private String width;
    private String url;
    private String size;

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String toString() {
        return "[height = " + this.height + ", width = " + this.width + ", url = " + this.url + ", size = " + this.size + "]";
    }
}

