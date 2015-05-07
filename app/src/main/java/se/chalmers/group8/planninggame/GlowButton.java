package se.chalmers.group8.planninggame;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by patrik on 2015-05-04.
 */
public class GlowButton {

    private boolean isGlowing = false;

    private View button;
    private boolean isImageView;
    private int pressedResID;
    private int unPressedResID;

    public GlowButton(ImageView button, int pressedResID, int unPressedResID) {
        isImageView = true;
        this.button = button;
        this.pressedResID = pressedResID;
        this.unPressedResID = unPressedResID;

    }

    public GlowButton(Button button, int pressedResID, int unPressedResID) {
        isImageView = false;
        this.button = button;
        this.pressedResID = pressedResID;
        this.unPressedResID = unPressedResID;
    }

    public boolean isGlowing() {
        return isGlowing;
    }

    public void setGlowing(boolean isGlowing) {
        this.isGlowing = isGlowing;

        if(isGlowing) {
            if (isImageView)
                ((ImageView) button).setImageResource(pressedResID);
            else
                ((Button) button).setBackgroundResource(pressedResID);
        }
        else {
            if (isImageView)
                ((ImageView) button).setImageResource(unPressedResID);
            else
                ((Button) button).setBackgroundResource(unPressedResID);
        }

    }

    public void setOnClickListener(View.OnClickListener l) {
        button.setOnClickListener(l);
    }

    public View getView() {
        return button;
    }

    /*@Override
    public boolean equals(Object o) {

        GlowButton that = (GlowButton) o;

        return button.getId() == that.getView().getId();
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GlowButton that = (GlowButton) o;

        if (isGlowing != that.isGlowing) return false;
        if (isImageView != that.isImageView) return false;
        if (pressedResID != that.pressedResID) return false;
        if (unPressedResID != that.unPressedResID) return false;
        if (!button.equals(that.button)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (isGlowing ? 1 : 0);
        result = 31 * result + button.hashCode();
        result = 31 * result + (isImageView ? 1 : 0);
        result = 31 * result + pressedResID;
        result = 31 * result + unPressedResID;
        return result;
    }
}
