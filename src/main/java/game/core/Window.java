package game.core;

import org.lwjgl.glfw.GLFWVidMode;

import static game.core.GlobalConsts.HEIGHT;
import static game.core.GlobalConsts.WIDTH;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    long window;
    int width = (int) WIDTH, height = (int) HEIGHT;
    int xPos, yPos;

    public Window(String title) {
        if (!glfwInit()) throw new IllegalStateException("Failed to initialize GLFW");

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);

        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        window = glfwCreateWindow(width, height, title, 0, 0);

        assert mode != null;
        xPos = (mode.width() - width) / 2;
        yPos = (mode.height() - height) / 2;
        glfwSetWindowPos(window, xPos, yPos);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void destroy() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}
