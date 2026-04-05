package game.core;

import game.audio.AudioEngine;
import game.input.Keyboard;
import game.input.Mouse;
import game.render.Texture;
import game.scenes.core.SceneManager;
import game.scenes.other.EmptyScene;

import static game.core.GlobalConsts.FPS;

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
