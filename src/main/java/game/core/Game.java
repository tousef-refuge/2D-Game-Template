package game.core;

import game.audio.AudioEngine;
import game.data.PlayerData;
import game.input.Keyboard;
import game.input.Mouse;
import game.render.Texture;
import game.scenes.core.SceneManager;
import game.window.Window;

import static game.core.GlobalConsts.FPS;
import static game.core.GlobalConsts.STARTING_SCENE;

public class Game {
    final double frameTime = 1.0 / (double) FPS;

    @SuppressWarnings("BusyWait")
    public Game(String title) {
        PlayerData.init();
        Window.init(title);

        AudioEngine.init();
        Texture.init();
        SceneManager.push(STARTING_SCENE);

        double prevTime = time();
        while (!Window.shouldClose()) {
            if (SceneManager.isEmpty()) {
                Window.requestClose();
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

            Window.update();
            Window.clear();

            AudioEngine.update();
        }
        AudioEngine.cleanup();
        Texture.cleanupAll();
        Window.destroy();
    }

    private double time() {
        return System.nanoTime() / 1_000_000_000.0;
    }
}
