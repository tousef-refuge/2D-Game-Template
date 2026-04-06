package game.scenes.core;

import game.exceptions.AssetNotFoundException;
import game.scenes.other.EmptyScene;

public class SceneFactory {
    public static Scene createScene(String name, String args) {
        return switch (name.toLowerCase()) {
            case "emptyscene" -> new EmptyScene();
            default -> throw new AssetNotFoundException("scene", name);
        };
    }
}
