package game.core;

import game.data.Env;
import game.scenes.base.Scene;
import game.scenes.other.EmptyScene;

public final class GlobalConsts {
    //initial game variables
    public static final String TITLE = "2D Game Template";
    public static final int FPS = 72;
    public static final boolean IS_DEBUG = Env.get("DEBUG", "false").equals("true");
    public static final Scene STARTING_SCENE = new EmptyScene();
}
