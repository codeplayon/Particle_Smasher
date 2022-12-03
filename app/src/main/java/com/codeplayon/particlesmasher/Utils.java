package com.codeplayon.particlesmasher;
import android.content.res.Resources;

public class Utils {


    private static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;
    public static int dp2Px(int dp) {
        return Math.round(dp * DENSITY);
    }

}
