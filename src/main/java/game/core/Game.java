package game.core;

import game.audio.AudioEngine;
import game.input.Keyboard;
import game.input.Mouse;
import game.render.Texture;
import game.scenes.core.SceneManager;
import game.scenes.other.EmptyScene;

import static game.core.GlobalConsts.FPS;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;

public class Game {
    Window window;
    final double frameTime = 1.0 / (double) FPS;

    @SuppressWarnings("BusyWait")
    public Game(String title) {
        window = new Window(title);

        AudioEngine.init();
        Texture.init();

        SceneManager.push(new EmptyScene());

        double prevTime = time();
        while (!window.shouldClose()) {
            if (SceneManager.isEmpty()) {
                window.requestClose();
                break;
            }

            double startTime = time();
            double currentTime = time();
            double dt = currentTime - prevTime;
            prevTime = currentTime;

            SceneManager.update(dt);
            SceneManager.render();

            double endTime = time();
            double elapsed = endTime - startTime;
            if (elapsed < frameTime) {
                try {
                    Thread.sleep((long)((frameTime - elapsed) * 1000.0));
                } catch (InterruptedException ignored) {}
            }

            Keyboard.update();
            Mouse.update();
            window.update();
            if (Keyboard.isTapped(GLFW_KEY_F11)) window.toggleFullscreen();

            window.clear();
            AudioEngine.update();
        }
        AudioEngine.cleanup();
        Texture.cleanupAll();
        window.destroy();
    }

    private double time() {
        return System.nanoTime() / 1_000_000_000.0;
    }
}
