package huongbien.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Json {
    public static JsonObject read(String filepath) {
        try (FileReader reader = new FileReader(filepath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean write(Object object, String filepath) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filepath)) {
            gson.toJson(object, writer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JsonObject jsonToJsonObject(String json) {
        JsonElement element = JsonParser.parseString(json);
        return element.getAsJsonObject();
    }
}
