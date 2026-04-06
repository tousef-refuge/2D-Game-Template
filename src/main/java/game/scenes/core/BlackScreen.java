package game.scenes.core;

public class BlackScreen extends Scene {
    double timePassed, resetTime;

    public BlackScreen(double resetTime) {
        this.resetTime = resetTime;
    }

    public BlackScreen() {
        this(0);
    }

    @Override
    public void update(double dt) {
        timePassed += dt;
        if (timePassed >= resetTime) SceneManager.pop(true);
    }

    @Override
    public void render() {}
}
