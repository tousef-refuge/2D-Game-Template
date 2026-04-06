package game.window;

import static game.window.Window.HEIGHT;
import static game.window.Window.WIDTH;

public enum Alignment {
    TOP_LEFT, TOP, TOP_RIGHT,
    LEFT, CENTER, RIGHT,
    BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT;

    public static Alignment get(String name) {
        for (var align : Alignment.values()) {
            if (align.name().equalsIgnoreCase(name)) return align;
        }
        throw new IllegalArgumentException("No such alignment: " + name);
    }

    public record Coordinates(float x, float y) {}
    public Coordinates getCoords() {
        return switch (this) {
            case TOP_LEFT -> new Coordinates(0f, 0f);
            case TOP -> new Coordinates(WIDTH * 0.5f, 0f);
            case TOP_RIGHT -> new Coordinates(WIDTH, 0f);
            case LEFT -> new Coordinates(0f, HEIGHT * 0.5f);
            case CENTER -> new Coordinates(WIDTH * 0.5f, HEIGHT * 0.5f);
            case RIGHT -> new Coordinates(WIDTH, HEIGHT * 0.5f);
            case BOTTOM_LEFT -> new Coordinates(0f, HEIGHT);
            case BOTTOM -> new Coordinates(WIDTH * 0.5f, HEIGHT);
            case BOTTOM_RIGHT -> new Coordinates(WIDTH, HEIGHT);
        };
    }
}
