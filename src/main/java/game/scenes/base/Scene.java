package game.scenes.base;

@SuppressWarnings("SameParameterValue")
public abstract class Scene {
    protected boolean exitFlag;
    public void reset() {}
    public void cleanup() {}
    public abstract void update(double dt);
    public abstract void render();
}
