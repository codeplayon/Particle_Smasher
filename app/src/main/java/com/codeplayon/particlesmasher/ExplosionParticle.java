package com.codeplayon.particlesmasher;
import android.graphics.Rect;

import java.util.Random;

public class ExplosionParticle extends Particle{

    public ExplosionParticle( int color, int radius, Rect rect, float endValue, Random random, float horizontalMultiple, float verticalMultiple){

        this.color = color;
        alpha = 1;
        float nextFloat = random.nextFloat();

        baseRadius = getBaseRadius(radius, random, nextFloat);
        this.radius =  baseRadius;

        horizontalElement = getHorizontalElement(rect, random, nextFloat, horizontalMultiple);
        verticalElement = getVerticalElement(rect, random, nextFloat, verticalMultiple);

        int offsetX = rect.width() / 4;
        int offsetY = rect.height() / 4;


        baseCx = rect.centerX() + offsetX * (random.nextFloat() - 0.5f);
        baseCy = rect.centerY() + offsetY * (random.nextFloat() - 0.5f);
        cx = baseCx;
        cy = baseCy;


        font = endValue / 10 * random.nextFloat();
        later = 0.4f * random.nextFloat();
    }

    private static float getBaseRadius(float radius, Random random, float nextFloat) {
        float r = radius + radius * (random.nextFloat() - 0.5f) * 0.5f;
        r = nextFloat < 0.6f ? r :
                nextFloat < 0.8f ? r * 1.4f : r * 0.8f;
        return r;
    }

    private static float getHorizontalElement(Rect rect, Random random, float nextFloat,float horizontalMultiple) {


        float horizontal = rect.width() * (random.nextFloat() - 0.5f);
        horizontal = nextFloat < 0.2f ? horizontal :
                nextFloat < 0.8f ? horizontal * 0.6f : horizontal * 0.3f;
        return horizontal * horizontalMultiple;
    }

    private static float getVerticalElement(Rect rect, Random random, float nextFloat,float verticalMultiple) {
        float vertical = rect.height() * (random.nextFloat() * 0.5f + 0.5f);
        vertical = nextFloat < 0.2f ? vertical :
                nextFloat < 0.8f ? vertical * 1.2f : vertical * 1.4f;

        // 上面的计算是为了让变化参数有随机性，下面的计算是变化的幅度。
        return vertical * verticalMultiple;
    }


    public void advance(float factor, float endValue) {

        // 动画进行到了几分之几
        float normalization = factor / endValue;

        if (normalization < font || normalization > 1f - later) {
            alpha = 0;
            return;
        }
        alpha = 1;

        // 粒子可显示的状态中，动画实际进行到了几分之几
        normalization = (normalization - font) / (1f - font - later);
        // 动画超过7/10，则开始逐渐变透明
        if (normalization >= 0.7f) {
            alpha = 1f - (normalization - 0.7f) / 0.3f;
        }

        float realValue = normalization * endValue;

        // y=j+k*x，j、k都是常数，x为 0~1.4
        cx = baseCx + horizontalElement * realValue;

        // y=j+k*(x*(x-1)，j、k都是常数，x为 0~1.4
        cy = baseCy + verticalElement * (realValue * (realValue - 1));

        radius = baseRadius + baseRadius / 4 * realValue;

    }

}
