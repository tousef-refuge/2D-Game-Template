package game.input;

import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static game.core.GlobalConsts.WIDTH;
import static game.core.GlobalConsts.HEIGHT;
import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    private final static boolean[] buttons = new boolean[GLFW_MOUSE_BUTTON_LAST];
    private final static boolean[] prevButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];

    private static double x, y;
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

            double mouseX = xPos.get(), mouseY = yPos.get();
            IntBuffer fbWidth  = stack.mallocInt(1);
            IntBuffer fbHeight = stack.mallocInt(1);
            glfwGetFramebufferSize(window, fbWidth, fbHeight);

            int fbW = fbWidth.get(0);
            int fbH = fbHeight.get(0);

            float scaleX = fbW / WIDTH;
            float scaleY = fbH / HEIGHT;
            float scale = Math.min(scaleX, scaleY);

            int vpWidth  = (int)(WIDTH * scale);
            int vpHeight = (int)(HEIGHT * scale);

            int vpX = (fbW - vpWidth) / 2;
            int vpY = (fbH - vpHeight) / 2;

            double localX = mouseX - vpX;
            double localY = mouseY - vpY;

            double normX = localX / vpWidth;
            double normY = localY / vpHeight;

            //no HARAM out of bounds
            x = Math.max(0, Math.min(WIDTH, normX * WIDTH));
            y = Math.max(0, Math.min(HEIGHT, normY * HEIGHT));
        }
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
}
