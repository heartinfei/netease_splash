package com.wangqiang.libs.splashviewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

/**
 * Created by heartinfei on 2015/9/15.
 */
public class SplashViewSwicher extends FrameLayout implements Animation.AnimationListener {
    protected int anim_in;
    protected int anim_out;
    protected AnimoutFinishListener listener;

    public SplashViewSwicher(Context context) {
        super(context);
    }

    public SplashViewSwicher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SplashViewSwicher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    protected void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SplashViewSwicher, defStyle, 0);
        anim_out = a.getResourceId(R.styleable.SplashViewSwicher_svs_anim_in, R.anim.default_out_anim);
        anim_in = a.getResourceId(R.styleable.SplashViewSwicher_svs_anim_out, R.anim.base_slide_right_in);
        a.recycle();
    }

    public void setListener(AnimoutFinishListener amListener) {
        this.listener = amListener;
    }

    public void showView(View view) {
        addView(view, 0);
        view.setAnimation(AnimationUtils.loadAnimation(getContext(), anim_in));
        int removeIndex = getChildCount() - 1;
        if (removeIndex < 0) {
            return;
        }
        View removeView = getChildAt(removeIndex);
        removeView.setAnimation(AnimationUtils.loadAnimation(getContext(), anim_out));
        removeView.animate();
        view.animate();
        removeView.getAnimation().setAnimationListener(this);
    }//end showView

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (listener != null) {
            listener.onAnimoutFinish();
        }
        removeViewAt(getChildCount() - 1);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public interface AnimoutFinishListener {
        public void onAnimoutFinish();
    }
}
