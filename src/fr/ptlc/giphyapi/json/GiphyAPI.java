package fr.ptlc.giphyapi.json;

import fr.ptlc.giphyapi.json.Data;
import fr.ptlc.giphyapi.json.Meta;
import fr.ptlc.giphyapi.json.Pagination;

public class GiphyAPI {
    private Data[] data;
    private Pagination pagination;
    private Meta meta;

    public Data[] getData() {
        return this.data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return this.pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String toString() {
        return "[data = " + this.data + ", pagination = " + this.pagination + ", meta = " + this.meta + "]";
    }
}

