package game.ui.buttons;

import game.input.Keyboard;
import game.input.Mouse;
import game.render.core.Texture;
import game.ui.core.UIElement;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Button extends UIElement {
    protected final Runnable onClick;
    protected final int[] keys;

    public Button(float x, float y,
                  float width, float height,
                  Texture texture, String align,
                  Runnable onClick, int[] keys) {
        super(x, y, width, height, texture, align);
        this.onClick = onClick;
        this.keys = keys;
    }

    @Override
    public void update(double dt) {
        isClicked = false;
        if (Keyboard.isTapped(keys) && isActive) {
            onClick.run();
            return;
        }
        if (Mouse.isTapped(GLFW_MOUSE_BUTTON_LEFT)) {
            double mouseX = Mouse.getX(), mouseY = Mouse.getY();
            if (contains(mouseX, mouseY)) {
                if (isActive) onClick.run();
                isClicked = true;
            }
        }
    }
}
