package game.render.core;

import game.exceptions.AssetNotFoundException;
import game.window.Alignment;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    final int id;
    final int width, height;
    private static final Map<String, Texture> textures = new HashMap<>();

    private Texture(String path) {
        try (ImageData data = new ImageData(path)) {
            var image = data.image;
            width = data.width;
            height = data.height;

            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                    GL_RGBA, GL_UNSIGNED_BYTE, image);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        ClassLoader loader = Texture.class.getClassLoader();
        try (InputStream in = loader.getResourceAsStream("textures.manifest")) {
            if (in == null) throw new AssetNotFoundException("textures.manifest not found");

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            for (String line : reader.lines().toList()) {
                String path = "textures/" + line.trim();
                textures.put(path, new Texture(path));
            }
        } catch (IOException ignored) {}
    }

    public static Texture get(String img) {
        String path = "textures/" + img;
        if (textures.get(path) == null) throw new AssetNotFoundException("texture", path);
        return textures.get(path);
    }

    public static boolean exists(String img) {
        String path = "textures/" + img;
        return textures.containsKey(path);
    }

    public void render(float x, float y,
                       float width, float height,
                       float alpha, String align) {
        glBindTexture(GL_TEXTURE_2D, id);
        glColor4f(1f, 1f, 1f, alpha);

        var coords = Alignment.get(align).getCoords();
        float nx = x + coords.x(), ny = y + coords.y();

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(nx, ny);
        glTexCoord2f(1, 0); glVertex2f(nx + width, ny);
        glTexCoord2f(1, 1); glVertex2f(nx + width, ny + height);
        glTexCoord2f(0, 1); glVertex2f(nx, ny + height);
        glEnd();
    }

    public void renderCentered(float x, float y,
                               float width, float height,
                               float alpha, String align) {
        float renderX = x - width / 2f;
        float renderY = y - height / 2f;
        render(renderX, renderY, width, height, alpha, align);
    }

    public static void cleanupAll() {
        textures.values().forEach(Texture::cleanup);
        textures.clear();
    }

    private void cleanup() {
        glDeleteTextures(id);
    }
}
