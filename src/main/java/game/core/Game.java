package game.core;

public class Game {
    public Game(String title) {
        Window window = new Window(title);

        while (!window.shouldClose()) {
            window.clear();
        }
        window.destroy();
    }
}
