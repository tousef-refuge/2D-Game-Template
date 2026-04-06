package game.ui.buttons;

import game.render.core.Texture;
import game.render.text.Text;

public class TextButton extends Button {
    String text, font;
    float fontSize, borderSize,
            r, g, b,
            rb, gb, bb;

    //smallest java constructor
    public TextButton(float x, float y,
                      float width, float height,
                      String text, String font,
                      float fontSize, float borderSize,
                      float r, float g, float b,
                      float rb, float gb, float bb,
                      Texture texture, String align,
                      Runnable onClick, int[] keys) {
        super(x, y, width, height, texture, align, onClick, keys);
        this.text = text;
        this.font = font;
        this.fontSize = fontSize;
        this.borderSize = borderSize;
        this.r = r;
        this.g = g;
        this.b = b;
        this.rb = rb;
        this.gb = gb;
        this.bb = bb;
    }

    @Override
    public void render() {
        float rx = getRenderX();
        float ry = getRenderY();
        texture.render(rx, ry, width, height, 1f, align);
        Text.renderCentered(text, font,
                fontSize, borderSize,
                rx + width * 0.5f, ry + height * 0.5f,
                r, g, b,
                rb, gb, bb,
                1f, align);
    }
}
