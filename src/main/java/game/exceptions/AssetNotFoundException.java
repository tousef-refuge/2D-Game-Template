package game.exceptions;

public class AssetNotFoundException extends RuntimeException {
    public AssetNotFoundException(String message) {
        super(message);
    }

    public AssetNotFoundException(String type, String path) {
        this("Failed to load " + type + ": " + path);
    }
}