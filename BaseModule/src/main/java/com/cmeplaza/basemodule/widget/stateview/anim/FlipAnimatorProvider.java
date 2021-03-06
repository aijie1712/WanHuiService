package com.cmeplaza.basemodule.widget.stateview.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.cmeplaza.basemodule.widget.stateview.AnimatorProvider;

/**
 * @author Nukc.
 */

public class FlipAnimatorProvider implements AnimatorProvider {

    private String mPropertyName;

    public FlipAnimatorProvider() {
        mPropertyName = "rotationX";
    }

    public FlipAnimatorProvider(boolean isX) {
        mPropertyName = isX ? "rotationX" : "rotationY";
    }

    @Override
    public Animator showAnimation(View view) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, mPropertyName, 90f, -15f, 15f, 0f),
                ObjectAnimator.ofFloat(view, "alpha", 0.25f, 0.5f, 0.75f, 1f)
        );
        set.setDuration(400);
        return set;
    }

    @Override
    public Animator hideAnimation(View view) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, mPropertyName, -90f, 15f, -15f, 0f),
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0.75f, 0.25f, 0f)
        );
        set.setDuration(400);
        return set;
    }
}
