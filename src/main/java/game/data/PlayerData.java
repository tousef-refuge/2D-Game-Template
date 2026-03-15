package game.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class PlayerData {
    private static JsonObject data = new JsonObject();
    private static final Gson gson = new Gson();
    private static final String SETTINGS_FILE = "gamedata/.playerdata.json",
            COPY_REF = "jsons/playerdata.json";

    @SuppressWarnings("ConstantConditions")
    public static void init() {
        File file = new File(SETTINGS_FILE);

        if (!file.exists()) {
            try (InputStream in = PlayerData.class.getClassLoader().getResourceAsStream(COPY_REF)) {
                if (in != null) Files.copy(in, file.toPath());
                else {
                    data = new JsonObject();
                    save();
                    return;
                }
            } catch (IOException ignored) {}
        }

        try (FileReader reader = new FileReader(file)) {
            data = gson.fromJson(reader, JsonObject.class);
            if (data == null) data = new JsonObject();
        } catch (IOException ignored) {}
    }

    public static boolean getBoolean(String key) {
        if (!data.has(key)) throw new IllegalArgumentException("No such key: " + key);
        return data.get(key).getAsBoolean();
    }

    public static float getFloat(String key) {
        if (!data.has(key)) throw new IllegalArgumentException("No such key: " + key);
        return data.get(key).getAsFloat();
    }

    public static int getInt(String key) {
        if (!data.has(key)) throw new IllegalArgumentException("No such key: " + key);
        return data.get(key).getAsInt();
    }

    public static String getString(String key) {
        if (!data.has(key)) throw new IllegalArgumentException("No such key: " + key);
        return data.get(key).getAsString();
    }

    public static void set(String key, Object val) {
        if (val instanceof Boolean bool) data.addProperty(key, bool);
        else if (val instanceof Number num) data.addProperty(key, num);
        else if (val instanceof String str) data.addProperty(key, str);
        else if (val instanceof JsonElement elem) data.add(key, elem);
        else throw new IllegalArgumentException("Unsupported type: " + val.getClass());
        save();
    }

    public static <T> ArrayList<T> getArrayList(String key, Class<T> klass) {
        if (!data.has(key)) throw new IllegalArgumentException("No such key: " + key);
        if (!data.get(key).isJsonArray()) throw new IllegalArgumentException("Not a json array: " + key);

        JsonArray arr = data.getAsJsonArray(key);
        Type type = TypeToken.getParameterized(List.class, klass).getType();

        return gson.fromJson(arr, type);
    }

    public static <T> void setArrayList(String key, ArrayList<T> list) {
        data.add(key, gson.toJsonTree(list));
        save();
    }

    public static <T> HashMap<String, T> getHashMap(String key, Class<T> klass) {
        if (!data.has(key)) throw new IllegalArgumentException("No such key: " + key);
        if (!data.get(key).isJsonObject()) throw new IllegalArgumentException("Not a json object: " + key);

        JsonObject obj = data.getAsJsonObject(key);
        Type type = TypeToken.getParameterized(HashMap.class, String.class, klass).getType();

        return gson.fromJson(obj, type);
    }

    public static <T> void setHashMap(String key, HashMap<String, T> map) {
        data.add(key, gson.toJsonTree(map));
        save();
    }

    private static void save() {
        try (FileWriter writer = new FileWriter(SETTINGS_FILE)) {
            gson.toJson(data, writer);
        } catch (IOException ignored) {}
    }
}
