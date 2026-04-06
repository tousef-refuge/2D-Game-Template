package game.scenes.core;

import java.util.ArrayDeque;

public abstract class StateScene extends Scene {
    private final ArrayDeque<SceneState> stateStack = new ArrayDeque<>();

    protected SceneState currentState() {
        return stateStack.peek();
    }

    protected boolean isState(SceneState state) {
        return stateStack.peek() == state;
    }

    protected void nextState(SceneState state) {
        stateStack.push(state);
    }

    protected void removeState(SceneState state) {
        if (stateStack.peek() == state) stateStack.poll();
    }
}

