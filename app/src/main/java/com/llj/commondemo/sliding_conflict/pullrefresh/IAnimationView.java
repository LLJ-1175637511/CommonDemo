package com.llj.commondemo.sliding_conflict.pullrefresh;

import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

/**
 * @author liyue @ Zhihu Inc.
 * @since 05-08-2023
 */
interface IAnimationView {

    void setAnimation(final String assetName);

    void setAnimation(@RawRes final int rawRes);

    void setAnimationFromJson(String jsonString, @Nullable String cacheKey);

    boolean isAnimating();

    void playAnimation();

    void pauseAnimation();
}
