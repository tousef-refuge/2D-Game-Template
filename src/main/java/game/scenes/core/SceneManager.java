package game.scenes.core;

import java.util.ArrayDeque;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

@SuppressWarnings("DataFlowIssue")
public class SceneManager {
    private static final ArrayDeque<Scene> scenes = new ArrayDeque<>();

    public static boolean isEmpty() {
        return scenes.isEmpty();
    }

    public static void push(Scene scene) {
        scenes.push(scene);
    }

    public static void pop(boolean resetPrevious) {
        if (!isEmpty()) scenes.pop().cleanup();
        if (!isEmpty() && resetPrevious) scenes.peek().reset();
    }

    public static void update(double dt) {
        if (!isEmpty()) scenes.peek().update(dt);
    }

    public static void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        if (!isEmpty()) scenes.peek().render();
    }
}