package gwt.material.design.client.data.component;

import gwt.material.design.client.ui.animate.Transition;

public class RowHighlightConfig {

    private int offsetTop;
    private int duration;
    private int delay;
    private Transition transition;

    public RowHighlightConfig() {
    }

    public RowHighlightConfig(int offsetTop, int duration, int delay, Transition transition) {
        this.offsetTop = offsetTop;
        this.duration = duration;
        this.delay = delay;
        this.transition = transition;
    }

    public int getOffsetTop() {
        return offsetTop;
    }

    public void setOffsetTop(int offsetTop) {
        this.offsetTop = offsetTop;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Transition getTransition() {
        return transition;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }
}
