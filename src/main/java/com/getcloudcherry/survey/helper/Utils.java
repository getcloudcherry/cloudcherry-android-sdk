package com.getcloudcherry.survey.helper;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by riteshdubey on 8/20/16.
 */
public class Utils {
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp) {
        Resources resources = SurveyCC.getInstance().getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
}
