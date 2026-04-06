package game.data;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    public static String get(String key) {
        String val = dotenv.get(key);
        if (val == null) throw new NullPointerException(key + " does not exist in .env");
        return val;
    }

    public static String get(String key, String defaultValue) {
        return dotenv.get(key, defaultValue);
    }
}
