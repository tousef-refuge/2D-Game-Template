package game.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import game.exceptions.AssetNotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonData {
    JsonObject data;

    public JsonData(String name) {
        String path = "jsons/" + name;
        final Gson gson = new Gson();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(path)) {
            if (in == null) throw new AssetNotFoundException("json", path);

            try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                data = gson.fromJson(reader, JsonObject.class);
                if (data == null) data = new JsonObject();
            }
        } catch (IOException ignored) {}
    }

    public boolean getAsBoolean(String memberName) {
        return data.get(memberName).getAsBoolean();
    }

    public int getAsInt(String memberName) {
        return data.get(memberName).getAsInt();
    }

    public float getAsFloat(String memberName) {
        return data.get(memberName).getAsFloat();
    }

    public double getAsDouble(String memberName) {
        return data.get(memberName).getAsDouble();
    }

    public String getAsString(String memberName) {
        return data.get(memberName).getAsString();
    }

    public JsonElement getAsJsonElement(String memberName) {
        return data.get(memberName);
    }

    public JsonArray getAsJsonArray(String memberName) {
        return data.getAsJsonArray(memberName);
    }

    public JsonObject getAsJsonObject(String memberName) {
        return data.getAsJsonObject(memberName);
    }
}
