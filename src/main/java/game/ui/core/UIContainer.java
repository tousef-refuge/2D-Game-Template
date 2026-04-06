package game.ui.core;

import game.input.Keyboard;
import game.input.Mouse;
import game.render.core.Texture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class UIContainer extends UIElement {
    private final List<UIElement> children = new ArrayList<>();
    protected final Runnable onClick;
    protected final int[] keys;

    public UIContainer(float x, float y,
                       float width, float height,
                       Texture texture, String align,
                       Runnable onClick, int[] keys) {
        super(x, y, width, height, texture, align);
        this.onClick = onClick;
        this.keys = keys;
    }

    public UIContainer() {
        this(0f, 0f, 0f, 0f, null, null, null, null);
    }

    public void add(UIElement child) {
        children.add(child);
    }

    public void remove(UIElement child) {
        children.remove(child);
    }

    public void clear() {
        children.clear();
    }

    @Override
    public void update(double dt) {
        for (UIElement child : children) {
            child.setActive(isActive);
            child.update(dt);
            if (child.isClicked) return;
        }

        if (onClick == null) return;
        if (Keyboard.isTapped(keys)) {
            onClick.run();
            return;
        }
        if (Mouse.isTapped(GLFW_MOUSE_BUTTON_LEFT)) {
            double mouseX = Mouse.getX(), mouseY = Mouse.getY();
            if (contains(mouseX, mouseY)) onClick.run();
        }
    }

    @Override
    public void render() {
        if (texture != null) super.render();
        for (UIElement child : children) child.render();
    }
}
