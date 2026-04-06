package game.ui.core;

import game.render.core.Texture;
import game.window.Alignment;

public abstract class UIElement {
    protected float x, y;
    protected final float width, height;
    protected final Texture texture;

    protected boolean isActive = true, isClicked = false;
    protected String align;

    public UIElement(float x, float y,
                     float width, float height,
                     Texture texture, String align) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.align = align;
    }

    public abstract void update(double dt);

    public void render() {
        float rx = getRenderX();
        float ry = getRenderY();
        texture.render(rx, ry, width, height, 1f, align);
    }

    public boolean contains(double mx, double my) {
        float rx = getRenderX();
        float ry = getRenderY();
        return mx >= rx && mx <= rx + width &&
                my >= ry && my <= ry + height;
    }

    public float getRenderX() {
        return x + Alignment.get(align).getCoords().x();
    }

    public float getRenderY() {
        return y + Alignment.get(align).getCoords().y();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPos(UIElement other) {
        this.x = other.x;
        this.y = other.y;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void setXCentered(float x) {
        this.x = x - 0.5f * width;
    }

    public void setYCentered(float y) {
        this.y = y - 0.5f * height;
    }

    public void setPosCentered(UIElement other) {
        this.x = other.x + 0.5f * other.width;
        this.y = other.y + 0.5f * other.height;
    }
}