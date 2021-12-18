/*
 * Decompiled with CFR 0_123.
 */
package fr.ptlc.giphyapi.json;

public class Pagination {
    private String count;
    private String offset;
    private String total_count;

    public String getCount() {
        return this.count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOffset() {
        return this.offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getTotal_count() {
        return this.total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public String toString() {
        return "[count = " + this.count + ", offset = " + this.offset + ", total_count = " + this.total_count + "]";
    }
}

