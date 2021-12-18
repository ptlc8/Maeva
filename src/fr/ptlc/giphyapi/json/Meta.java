/*
 * Decompiled with CFR 0_123.
 */
package fr.ptlc.giphyapi.json;

public class Meta {
    private String status;
    private String msg;
    private String response_id;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResponse_id() {
        return this.response_id;
    }

    public void setResponse_id(String response_id) {
        this.response_id = response_id;
    }

    public String toString() {
        return "[status = " + this.status + ", msg = " + this.msg + ", response_id = " + this.response_id + "]";
    }
}

