package gwt.material.design.client.js;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType
public class SmartScrollBlock {

    @JsProperty String[] dirs;
    @JsProperty boolean up;
    @JsProperty boolean down;
    @JsProperty boolean left;
    @JsProperty boolean right;
    @JsProperty boolean x;

    public SmartScrollBlock(boolean x) {
        this.x = x;
    }

    public String[] getDirs() {
        return dirs;
    }

    public void setDirs(String[] dirs) {
        this.dirs = dirs;
    }

    @JsMethod
    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    @JsMethod
    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    @JsMethod
    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    @JsMethod
    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    @JsMethod
    public boolean isX() {
        return x;
    }

    public void setX(boolean x) {
        this.x = x;
    }

    @JsMethod
    public boolean isY() {
        return !x;
    }

    public void setY(boolean y) {
        this.x = !y;
    }
}