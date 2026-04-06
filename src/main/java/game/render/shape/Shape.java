package game.render.shape;

import game.window.Alignment;

import static org.lwjgl.opengl.GL11.*;

public class Shape {
    public static void rectangle(float cx, float cy,
                                 float width, float height,
                                 float r, float g, float b,
                                 float a, String align) {
        var coords = Alignment.get(align).getCoords();
        float x = cx + coords.x(), y = cy + coords.y();
        float halfWidth = width * 0.5f, halfHeight = height * 0.5f;

        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glDisable(GL_TEXTURE_2D);
        glColor4f(r, g, b, a);

        glBegin(GL_QUADS);
        glVertex2f(x - halfWidth, y - halfHeight);
        glVertex2f(x + halfWidth, y - halfHeight);
        glVertex2f(x + halfWidth, y + halfHeight);
        glVertex2f(x - halfWidth, y + halfHeight);

        glEnd();
        glPopAttrib();
    }

    public static void circleSegment(float cx, float cy,
                                     float radius, float startDeg, float endDeg,
                                     float r, float g, float b,
                                     float a, String align) {
        var coords = Alignment.get(align).getCoords();
        float x = cx + coords.x(), y = cy + coords.y();

        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glDisable(GL_TEXTURE_2D);
        glColor4f(r, g, b, a);

        int segments = 48;
        double startRad = Math.toRadians(startDeg);
        double endRad   = Math.toRadians(endDeg);

        glBegin(GL_TRIANGLE_FAN);
        glVertex2f(cx, cy);

        for (int i = 0; i <= segments; i++) {
            double t = (double) i / segments;
            double angle = startRad + t * (endRad - startRad);
            glVertex2f(
                    (float) (x + Math.cos(angle) * radius),
                    (float) (y + Math.sin(angle) * radius)
            );
        }

        glEnd();
        glPopAttrib();
    }

    public static void fullCircle(float cx, float cy,
                                  float radius,
                                  float r, float g, float b,
                                  float a, String align) {
        circleSegment(cx, cy, radius, 0f, 360f, r, g, b, a, align);
    }
}
