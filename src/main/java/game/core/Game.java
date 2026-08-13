package game.core;

import game.audio.AudioEngine;
import game.data.Env;
import game.data.PlayerData;
import game.input.Keyboard;
import game.input.Mouse;
import game.render.core.Texture;
import game.scenes.core.SceneFactory;
import game.scenes.core.SceneManager;
import game.window.Window;

import static game.core.GlobalConsts.*;

public class Game {
    final double frameTime = 1.0 / (double) FPS;

    @SuppressWarnings("BusyWait")
    public Game() {
        PlayerData.init();
        Window.init(TITLE);

        AudioEngine.init();
        Texture.init();
        SceneManager.push(STARTING_SCENE);

        if (Env.get("CHOOSE_SCENE", "false").equals("true")) {
            String name = Env.get("SCENE_NAME");
            String args = Env.get("SCENE_ARGS");
            SceneManager.push(SceneFactory.createScene(name, args));
        }

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

            Window.pollEvents();
            Window.swapBuffers();
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
