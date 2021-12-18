package fr.ptlc.maeva.commands;

import java.io.IOException;

import com.google.gson.Gson;

import fr.ptlc.maeva.ChatBotResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBot {
	
	private static OkHttpClient client = new OkHttpClient();
	
	public static String chat(String message) {
		Request request = new Request.Builder().method("POST", RequestBody.create(MediaType.parse("application/json"), "{"
			 + "\"instance\":\"35925292\"," // 35923595
			 + "\"application\":\"2106609370303710383\","
			 + "\"message\":\"" + message + "\""
			 + "}")).url("https://www.botlibre.com/rest/json/chat").build();
		String responseText;
		try {
			Response response = client.newCall(request).execute();
			responseText = response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return new Gson().fromJson(responseText, ChatBotResponse.class).message;
	}
	
}
