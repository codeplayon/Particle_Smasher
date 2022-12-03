package com.codeplayon.particlesmasher;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;
public class ParticleSmasher extends View {

    private List<SmashAnimator> mAnimators = new ArrayList<>();
    private Canvas mCanvas;
    private Activity mActivity;

    public ParticleSmasher(Activity activity) {
        super((Context) activity);
        this.mActivity = activity;
        addView2Window(activity);
        init();
    }


    private void addView2Window(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.addView(this, layoutParams);
    }

    private void init() {
        mCanvas = new Canvas();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (SmashAnimator animator : mAnimators) {
            animator.draw(canvas);
        }
    }

    public SmashAnimator with(View view) {
        SmashAnimator animator = new SmashAnimator(this, view);
        mAnimators.add(animator);
        return animator;
    }


    public Rect getViewRect(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);

        int[] location = new int[2];
        getLocationOnScreen(location);

        rect.offset(-location[0], -location[1]);
        return rect;
    }


    public Bitmap createBitmapFromView(View view) {

        view.clearFocus();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        if (bitmap != null) {
            synchronized (mCanvas) {
                Canvas canvas = mCanvas;
                canvas.setBitmap(bitmap);
                view.draw(canvas);
                canvas.setBitmap(null);
            }
        }
        return bitmap;
    }


    public void removeAnimator(SmashAnimator animator) {
        if (mAnimators.contains(animator)) {
            mAnimators.remove(animator);
        }
    }


    public void clear() {
        mAnimators.clear();
        invalidate();
    }


    public void reShowView(View view) {
        view.animate().setDuration(100).setStartDelay(0).scaleX(1).scaleY(1).translationX(0).translationY(0).alpha(1).start();
    }

}
