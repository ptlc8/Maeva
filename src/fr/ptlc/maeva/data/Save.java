package fr.ptlc.maeva.data;

import com.google.gson.Gson;

import fr.ptlc.maeva.data.Client;
import fr.ptlc.maeva.data.Maeva;
import fr.ptlc.maeva.data.Role;
import fr.ptlc.maeva.data.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Save {
    private static final File fileOptions = new File("options.json");
    //private static final File fileShop = new File("shop.json");
    private static final File fileData = new File("data.json");

    public static Maeva getMaeva() {
    	Options options = Save.getJson(fileOptions) != null ?
    			new Gson().fromJson(Save.getJson(fileOptions), Options.class) :
    				new Options("all", 3, new ArrayList<String>());
    	/*Shop shop = Save.getJson(fileShop) != null ?
    			new Gson().fromJson(Save.getJson(fileShop), Shop.class) :
    				new Shop(new ArrayList<Article>());*/
    	Data data = Save.getJson(fileData) != null ?
    			new Gson().fromJson(Save.getJson(fileData), Data.class) :
    				new Data(new ArrayList<Client>(), new ArrayList<Server>(), new ArrayList<Role>());
        return new Maeva(options, data);
    }

    private static String getJson(File file) {
    	try {
                if (!file.exists()) {
                    file.createNewFile();
                    return null;
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String line;
                String json = "";
                while ((line = br.readLine()) != null)
                    json = String.valueOf(json) + "\n" + line;
                br.close();
                if (json.contains("{"))
                    json = json.substring(json.indexOf("{"));
                return json == "" ? null : json;
            }
            catch (IOException br) {
                // empty catch block
            }
        return null;
    }
    
    public static void writeDown(Maeva save) {
    	writeDown(save.getOptions(), fileOptions);
    	//writeDown(save.getShop(), fileShop);
    	writeDown(save.getData(), fileData);
    }
    
    private static void writeDown(Object toSave, File file) {
        String json = new Gson().toJson(toSave);
        try {
            if (!file.exists())
                file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            String indentation = "";
            boolean inQuotes = false;
            for (int i = 0; i < json.length(); i++) {
                if (json.charAt(i) == '\"' && json.charAt(i - 1) != '\\') {
                    inQuotes = !inQuotes;
                    bw.append(json.charAt(i));
                } else if (inQuotes) {
                    bw.append(json.charAt(i));
                } else if (json.charAt(i) == ',') {
                    bw.append(json.charAt(i));
                    bw.newLine();
                    bw.append(indentation);
                } else if (json.charAt(i) == '{' || json.charAt(i) == '[') {
                    if (json.charAt(i + 1) == '}' || json.charAt(i + 1) == ']') {
                        bw.append(json.substring(i, i + 2));
                        ++i;
                    } else {
                        bw.append(json.charAt(i));
                        bw.newLine();
                        indentation = String.valueOf(indentation) + "    ";
                        bw.append(indentation);
                    }
                } else if (json.charAt(i) == '}' || json.charAt(i) == ']') {
                    indentation = indentation.substring(0, indentation.length() - 4);
                    bw.newLine();
                    bw.append(String.valueOf(indentation) + json.charAt(i));
                } else {
                    bw.append(json.charAt(i));
                }
            }
            bw.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}

