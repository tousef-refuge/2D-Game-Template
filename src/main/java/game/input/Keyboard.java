package game.input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Keyboard {
    private final static boolean[] keys = new boolean[GLFW_KEY_LAST];
    private final static boolean[] prevKeys = new boolean[GLFW_KEY_LAST];

    public static void update() {
        System.arraycopy(keys, 0, prevKeys, 0, GLFW_KEY_LAST);
    }

    @SuppressWarnings("unused")
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key >= 0 && key < GLFW_KEY_LAST) keys[key] = action != GLFW_RELEASE;
    }

    public static boolean isPressed(int key) {
        return keys[key];
    }

    public static boolean isPressed(int[] keys) {
        for (int key : keys) if (isPressed(key)) return true;
        return false;
    }

    public static boolean isTapped(int key) {
        return keys[key] && !prevKeys[key];
    }

    public static boolean isTapped(int[] keys) {
        for (int key : keys) if (isTapped(key)) return true;
        return false;
    }

    public static boolean isReleased(int key) {
        return !keys[key] && prevKeys[key];
    }

    public static boolean isReleased(int[] keys) {
        for (int key : keys) if (isReleased(key)) return true;
        return false;
    }
}
