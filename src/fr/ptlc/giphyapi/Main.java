package fr.ptlc.giphyapi;

import com.google.gson.Gson;
import fr.ptlc.giphyapi.json.GiphyAPI;
import java.io.IOException;
import java.util.Random;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main {
    private static OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {
        System.out.println(Main.getRandomGifUrl("cat hunter", "CeYnxeJsQprThusdyN6nFh6LdxYkHyQz", 5));
    }

    public static String getJSON(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static GiphyAPI searchGifs(String search, String apiKey, int limit) {
        String json = "";
        try {
            json = Main.getJSON("http://api.giphy.com/v1/gifs/search?q=" + search + "&api_key=" + apiKey + "&limit=" + limit);
        }
        catch (IOException e) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, GiphyAPI.class);
    }

    public static String getRandomGifUrl(String search, String apiKey, int limit) {
        if (Main.searchGifs(search, apiKey, limit).getData().length > 0) {
            return Main.searchGifs(search, apiKey, limit).getData()[new Random().nextInt(Main.searchGifs(search, apiKey, limit).getData().length)].getUrl();
        }
        return "Pas de r\u00e9sultat pour cette recherche...";
    }
}

