package game.window;

import game.input.Keyboard;
import game.input.Mouse;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private static long window;
    public static int WIDTH, HEIGHT;

    @SuppressWarnings("resource")
    public static void init(String title) {
        if (!glfwInit()) throw new IllegalStateException("Failed to initialize GLFW");

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);

        assert mode != null;
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
        WIDTH = mode.width();
        HEIGHT = mode.height();
        window = glfwCreateWindow(WIDTH, HEIGHT, title, monitor, 0);

        glfwSetKeyCallback(window, Keyboard::keyCallback);
        Mouse.setWindow(window);

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
        glViewport(0, 0, WIDTH, HEIGHT);
    }

    public static void update() {
        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public static void requestClose() {
        glfwSetWindowShouldClose(window, true);
    }

    public static void destroy() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}
