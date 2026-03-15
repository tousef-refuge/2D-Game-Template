package game.core;

import game.render.Texture;

import static game.core.GlobalConsts.FPS;

public class Game {
    Window window;
    final double frameTime = 1.0 / (double) FPS;

    @SuppressWarnings("BusyWait")
    public Game(String title) {
        window = new Window(title);

        Texture.init();

        while (!window.shouldClose()) {
            double startTime = time();
            double endTime = time();

            double elapsed = endTime - startTime;
            if (elapsed < frameTime) {
                try {
                    Thread.sleep((long)((frameTime - elapsed) * 1000.0));
                } catch (InterruptedException ignored) {}
            }

            window.update();
            window.clear();
        }
        Texture.cleanupAll();
        window.destroy();
    }

    private double time() {
        return System.nanoTime() / 1_000_000_000.0;
    }
}
