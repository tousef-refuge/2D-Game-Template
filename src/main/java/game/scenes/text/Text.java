package game.scenes.text;

import game.exceptions.AssetNotFoundException;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;

public class Text {
    private static final int BITMAP_SIZE = 1 << 10;
    private static final Map<String, Font> fontCache = new HashMap<>();

    private static Font getFont(String fontPath, float size) {
        String key = fontPath + '&' + size;

        Font entry = fontCache.get(key);
        if (entry != null) return entry;

        try {
            ByteBuffer buffer = loadInternalResource(fontPath);
            entry = bakeFont(buffer, size);
            fontCache.put(key, entry);
            return entry;
        } catch (Exception e) {
            throw new AssetNotFoundException("font", fontPath);
        }
    }

    private static ByteBuffer loadInternalResource(String path) throws IOException {
        try (InputStream in = Text.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) throw new IOException("Resource not found: " + path);

            byte[] bytes = in.readAllBytes();
            ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
            buffer.put(bytes).flip();
            return buffer;
        }
    }

    private static Font bakeFont(ByteBuffer fontBuffer, float fontSize) {
        STBTTBakedChar.Buffer cdata = STBTTBakedChar.malloc(0x60);
        ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_SIZE * BITMAP_SIZE);

        stbtt_BakeFontBitmap(fontBuffer, fontSize, bitmap, BITMAP_SIZE, BITMAP_SIZE, 0x20, cdata);

        STBTTFontinfo font = STBTTFontinfo.create();
        stbtt_InitFont(font, fontBuffer);

        IntBuffer a = BufferUtils.createIntBuffer(1);
        IntBuffer d = BufferUtils.createIntBuffer(1);
        IntBuffer g = BufferUtils.createIntBuffer(1);
        stbtt_GetFontVMetrics(font, a, d, g);

        float scale = stbtt_ScaleForPixelHeight(font, fontSize);

        float ascent  = a.get(0) * scale;
        float descent = d.get(0) * scale;
        float lineGap = g.get(0) * scale;
        float lineHeight = ascent - descent + lineGap;

        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA,
                BITMAP_SIZE, BITMAP_SIZE, 0,
                GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        return new Font(texture, cdata, ascent, descent, lineGap, lineHeight);
    }

    private static boolean isInvalidChar(char c) {
        return c < 32 || c > 126;
    }

    private static int mapChar(char c) {
        return c - 32;
    }
}
