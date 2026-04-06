package game.scenes.text;

import game.exceptions.AssetNotFoundException;
import game.window.Alignment;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;

public class Text {
    private static final int BITMAP_SIZE = 1 << 10;
    private static final Map<String, Font> fontCache = new HashMap<>();

    public static void render(String text, String fontName,
                              float fontSize, float borderSize,
                              float x, float y,
                              float r, float g, float b,
                              float rb, float gb, float bb,
                              float a, String align) {
        var coords = Alignment.get(align).getCoords();
        float nx = x + coords.x(), ny = y + coords.y();

        Font font = getFont(fontName, fontSize);
        if (borderSize != 0) {
            glColor4f(rb, gb, bb, a);
            drawBorder(text, font, nx, ny, borderSize);
        }

        glColor4f(r, g, b, a);
        drawText(text, font, nx, ny);
        glColor4f(1f, 1f, 1f, 1f);
    }

    private static Font getFont(String name, float size) {
        String key = name + '&' + size;

        Font entry = fontCache.get(key);
        if (entry != null) return entry;

        String path = "fonts/" + name + ".ttf";
        try {
            ByteBuffer buffer = loadInternalResource(path);
            entry = bakeFont(buffer, size);
            fontCache.put(key, entry);
            return entry;
        } catch (Exception e) {
            throw new AssetNotFoundException("font", path);
        }
    }

    private static void drawText(String text, Font font, float x, float y) {
        glBindTexture(GL_TEXTURE_2D, font.texture());

        FloatBuffer xPos = BufferUtils.createFloatBuffer(1).put(0, x);
        FloatBuffer yPos = BufferUtils.createFloatBuffer(1).put(0, y);

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\n') {
                xPos.put(0, x);
                yPos.put(0, yPos.get(0) + font.lineHeight());
                continue;
            }
            if (isInvalidChar(c)) continue;

            STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
            stbtt_GetBakedQuad(font.chars(), BITMAP_SIZE, BITMAP_SIZE, mapChar(c), xPos, yPos, q, true);

            glBegin(GL_QUADS);

            glTexCoord2f(q.s0(), q.t0());
            glVertex2f(q.x0(), q.y0());

            glTexCoord2f(q.s1(), q.t0());
            glVertex2f(q.x1(), q.y0());

            glTexCoord2f(q.s1(), q.t1());
            glVertex2f(q.x1(), q.y1());

            glTexCoord2f(q.s0(), q.t1());
            glVertex2f(q.x0(), q.y1());

            glEnd();
            q.free();
        }
    }

    private static void drawBorder(String text, Font font, float x, float y, float borderSize) {
        float[][] offsets = {
                {borderSize, borderSize},
                {borderSize, -borderSize},
                {-borderSize, -borderSize},
                {-borderSize, borderSize},
                {borderSize, 0},
                {-borderSize, 0},
                {0, borderSize},
                {0, -borderSize}
        };

        for (float[] o : offsets) drawText(text, font, x + o[0], y + o[1]);
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
