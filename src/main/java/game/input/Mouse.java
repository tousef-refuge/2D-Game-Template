package game.input;

import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    private final static boolean[] buttons = new boolean[GLFW_MOUSE_BUTTON_LAST];
    private final static boolean[] prevButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];

    private static double x = 0, y = 0;
    private static double dx, dy;
    public static long window;

    @SuppressWarnings({"unused", "resource"})
    public static void setWindow(long win) {
        window = win;
        glfwSetMouseButtonCallback(window, (winHandle, button, action, mods) -> {
            if (button < buttons.length) buttons[button] = action != GLFW_RELEASE;
        });
    }

    public static void update() {
        System.arraycopy(buttons, 0, prevButtons, 0, buttons.length);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer xPos = stack.mallocDouble(1);
            DoubleBuffer yPos = stack.mallocDouble(1);

            glfwGetCursorPos(window, xPos, yPos);
            double newX = xPos.get(0);
            double newY = yPos.get(0);

            dx = newX - x;
            dy = newY - y;
            x = newX;
            y = newY;
        }
    }

    public static void setCursorVisibility(boolean visible) {
        if (window == 0) return;
        glfwSetInputMode(
                window,
                GLFW_CURSOR,
                visible ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED
        );
    }

    public static boolean isPressed(int button) {
        return buttons[button];
    }

    public static boolean isTapped(int button) {
        return buttons[button] && !prevButtons[button];
    }

    public static boolean isReleased(int button) {
        return !buttons[button] && prevButtons[button];
    }

    public static double getX() {
        return x;
    }

    public static double getY() {
        return y;
    }

    public static double getDx() {
        return dx;
    }

    public static double getDy() {
        return dy;
    }
}