package com.codeplayon.particlesmasher;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.Random;

public class SmashAnimator {
    public static final int STYLE_EXPLOSION=1,
            STYLE_DROP=2,
            STYLE_FLOAT_LEFT=3,
            STYLE_FLOAT_RIGHT=4,
            STYLE_FLOAT_TOP=5,
            STYLE_FLOAT_BOTTOM=6;

    private int mStyle=STYLE_EXPLOSION;

    private ValueAnimator mValueAnimator;

    private ParticleSmasher mContainer;
    private View mAnimatorView;

    private Bitmap mBitmap;
    private Rect mRect;

    private Paint mPaint;
    private Particle[][] mParticles;

    private float mEndValue = 1.5f;

    private long mDuration = 1000L;
    private long mStartDelay = 150L;
    private float mHorizontalMultiple = 3;
    private float mVerticalMultiple = 4;
    private int mRadius=Utils.dp2Px(2);

    private static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateInterpolator(0.6f);
    private OnAnimatorListener mOnAnimatorLIstener;

    public SmashAnimator(ParticleSmasher view, View animatorView) {
        this.mContainer = view;
        init(animatorView);
    }

    private void init(View animatorView) {
        this.mAnimatorView = animatorView;
        mBitmap = mContainer.createBitmapFromView(animatorView);
        mRect = mContainer.getViewRect(animatorView);
        initValueAnimator();
        initPaint();
    }

    private void initValueAnimator() {
        mValueAnimator = new ValueAnimator();
        mValueAnimator.setFloatValues(0F, mEndValue);
        mValueAnimator.setInterpolator(DEFAULT_INTERPOLATOR);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }



    public static abstract class OnAnimatorListener {

        public void onAnimatorStart() {
        }

        public void onAnimatorEnd() {
        }

    }


    public SmashAnimator setStyle(int style){
        this.mStyle=style;
        return this;
    }


    public SmashAnimator setDuration(long duration) {
        this.mDuration = duration;
        return this;
    }


    public SmashAnimator setStartDelay(long startDelay) {
        mStartDelay = startDelay;
        return this;
    }


    public SmashAnimator setHorizontalMultiple(float horizontalMultiple) {
        this.mHorizontalMultiple = horizontalMultiple;
        return this;
    }


    public SmashAnimator setVerticalMultiple(float verticalMultiple) {
        this.mVerticalMultiple = verticalMultiple;
        return this;
    }


    public SmashAnimator setParticleRadius(int radius){
        this.mRadius=radius;
        return this;
    }


    public SmashAnimator addAnimatorListener(final OnAnimatorListener listener) {
        this.mOnAnimatorLIstener = listener;
        return this;
    }


    public void start() {
        setValueAnimator();
        calculateParticles(mBitmap);
        hideView(mAnimatorView, mStartDelay);
        mValueAnimator.start();
        mContainer.invalidate();
    }


    private void setValueAnimator() {
        mValueAnimator.setDuration(mDuration);
        mValueAnimator.setStartDelay(mStartDelay);
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnAnimatorLIstener != null) {
                    mOnAnimatorLIstener.onAnimatorEnd();
                }
                mContainer.removeAnimator(SmashAnimator.this);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                if (mOnAnimatorLIstener != null) {
                    mOnAnimatorLIstener.onAnimatorStart();
                }

            }
        });
    }


    private void calculateParticles(Bitmap bitmap) {

        int col = bitmap.getWidth() /(mRadius*2);
        int row = bitmap.getHeight() / (mRadius*2);

        Random random = new Random(System.currentTimeMillis());
        mParticles = new Particle[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int x=j * mRadius*2 + mRadius;
                int y=i * mRadius*2 + mRadius;
                int color = bitmap.getPixel(x, y);
                Point point=new Point(mRect.left+x,mRect.top+y);

                switch (mStyle){
                    case STYLE_EXPLOSION:
                        mParticles[i][j] = new ExplosionParticle(color, mRadius, mRect, mEndValue, random, mHorizontalMultiple, mVerticalMultiple);
                        break;
                    case STYLE_DROP:
                        mParticles[i][j] = new DropParticle(point,color, mRadius, mRect, mEndValue, random, mHorizontalMultiple, mVerticalMultiple);
                        break;
                    case STYLE_FLOAT_LEFT:
                        mParticles[i][j] = new FloatParticle(FloatParticle.ORIENTATION_LEFT,point,color, mRadius, mRect, mEndValue, random, mHorizontalMultiple, mVerticalMultiple);
                        break;
                    case STYLE_FLOAT_RIGHT:
                        mParticles[i][j] = new FloatParticle(FloatParticle.ORIENTATION_RIGHT,point,color, mRadius, mRect, mEndValue, random, mHorizontalMultiple, mVerticalMultiple);
                        break;
                    case STYLE_FLOAT_TOP:
                        mParticles[i][j] = new FloatParticle(FloatParticle.ORIENTATION_TOP,point,color, mRadius, mRect, mEndValue, random, mHorizontalMultiple, mVerticalMultiple);
                        break;
                    case STYLE_FLOAT_BOTTOM:
                        mParticles[i][j] = new FloatParticle(FloatParticle.ORIENTATION_BOTTOM,point,color, mRadius, mRect, mEndValue, random, mHorizontalMultiple, mVerticalMultiple);
                        break;
                }

            }
        }
        mBitmap.recycle();
        mBitmap = null;
    }



    public void hideView(final View view, long startDelay) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(startDelay + 50).setFloatValues(0f, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            Random random = new Random();

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setTranslationX((random.nextFloat() - 0.5F) * view.getWidth() * 0.05F);
                view.setTranslationY((random.nextFloat() - 0.5f) * view.getHeight() * 0.05f);
            }
        });
        valueAnimator.start();
        view.animate().setDuration(260).setStartDelay(startDelay).scaleX(0).scaleY(0).alpha(0).start();
    }



    public boolean draw(Canvas canvas) {
        if (!mValueAnimator.isStarted()) {
            return false;
        }
        for (Particle[] particle : mParticles) {
            for (Particle p : particle) {
                // 根据动画进程，修改粒子的参数
                p.advance((float) (mValueAnimator.getAnimatedValue()), mEndValue);
                if (p.alpha > 0) {
                    mPaint.setColor(p.color);
                    mPaint.setAlpha((int) (Color.alpha(p.color) * p.alpha));
                    canvas.drawCircle(p.cx, p.cy, p.radius, mPaint);
                }
            }
        }
        mContainer.invalidate();
        return true;
    }

}
