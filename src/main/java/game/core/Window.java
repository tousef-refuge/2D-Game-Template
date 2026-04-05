package game.core;

import game.input.Keyboard;
import game.input.Mouse;
import org.lwjgl.glfw.GLFWVidMode;

import static game.core.GlobalConsts.HEIGHT;
import static game.core.GlobalConsts.WIDTH;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    long window;
    int width = (int) WIDTH, height = (int) HEIGHT;
    int xPos, yPos;

    //the battle people curse
    boolean fullscreen = false;

    @SuppressWarnings("resource")
    public Window(String title) {
        if (!glfwInit()) throw new IllegalStateException("Failed to initialize GLFW");

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);

        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        window = glfwCreateWindow(width, height, title, 0, 0);

        glfwSetKeyCallback(window, Keyboard::keyCallback);
        Mouse.setWindow(window);

        assert mode != null;
        xPos = (mode.width() - width) / 2;
        yPos = (mode.height() - height) / 2;
        glfwSetWindowPos(window, xPos, yPos);

        glfwMakeContextCurrent(window);
        createCapabilities();
        glfwSwapInterval(1);

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glClearColor(0f, 0f, 0f, 1f);
        updateViewport();
    }

    @SuppressWarnings("DataFlowIssue")
    public void toggleFullscreen() {
        fullscreen = !fullscreen;

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);

        if (fullscreen) {
            int[] x = new int[1], y = new int[1];
            int[] w =  new int[1], h = new int[1];
            glfwGetWindowPos(window, x, y);
            glfwGetWindowSize(window, w, h);

            xPos = x[0]; yPos = y[0];
            width = w[0]; height = h[0];

            glfwSetWindowMonitor(window, monitor,
                    0, 0,
                    mode.width(), mode.height(),
                    mode.refreshRate()
            );
        } else {
            width = (int) WIDTH; height = (int) HEIGHT;
            glfwSetWindowMonitor(window, 0,
                    xPos, yPos,
                    width, height,
                    0);
        }

        updateViewport();
    }

    private void updateViewport() {
        int[] w = new int[1], h = new int[1];
        glfwGetFramebufferSize(window, w, h);
        width = w[0]; height = h[0];

        float scaleX = width / WIDTH;
        float scaleY = height / HEIGHT;
        float scale = Math.min(scaleX, scaleY);

        int vpWidth = (int)(WIDTH * scale);
        int vpHeight = (int)(HEIGHT * scale);

        int vpX = (width - vpWidth) / 2;
        int vpY = (height - vpHeight) / 2;

        glViewport(vpX, vpY, vpWidth, vpHeight);
    }

    public void update() {
        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void requestClose() {
        glfwSetWindowShouldClose(window, true);
    }

    public void destroy() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}
