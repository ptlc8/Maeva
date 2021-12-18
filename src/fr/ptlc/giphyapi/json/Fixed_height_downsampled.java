/*
 * Decompiled with CFR 0_123.
 */
package fr.ptlc.giphyapi.json;

public class Fixed_height_downsampled {
    private String webp;
    private String height;
    private String width;
    private String url;
    private String webp_size;
    private String size;

    public String getWebp() {
        return this.webp;
    }

    public void setWebp(String webp) {
        this.webp = webp;
    }

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

    public String getWebp_size() {
        return this.webp_size;
    }

    public void setWebp_size(String webp_size) {
        this.webp_size = webp_size;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String toString() {
        return "[webp = " + this.webp + ", height = " + this.height + ", width = " + this.width + ", url = " + this.url + ", webp_size = " + this.webp_size + ", size = " + this.size + "]";
    }
}

