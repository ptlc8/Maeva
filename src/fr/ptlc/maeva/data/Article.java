package fr.ptlc.maeva.data;

import java.util.Collections;
import java.util.List;

public class Article {
    private String name;
    private int price;
    private String description;
    private String imageUrl;
    private List<String> args;

    public Article(String name, int price, String description, String imageUrl, List<String> args) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.args = args;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public List<String> getArgs() {
        return Collections.unmodifiableList(args);
    }

    public String toString() {
        return "[" + this.name + ", " + this.price + ", " + this.description + ", " + this.imageUrl + ", " + this.args.toString() + "]";
    }
}

