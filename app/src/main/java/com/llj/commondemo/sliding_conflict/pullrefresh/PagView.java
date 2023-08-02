package com.llj.commondemo.sliding_conflict.pullrefresh;

import android.content.Context;
import android.opengl.EGLContext;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.zhihu.zhixuetang.android.study.calendar.pullrefresh.PagConfigKt;

import org.libpag.PAGFile;
import org.libpag.PAGView;

/**
 * zui仓库封装的ZUIAnimationView继承了widget仓库的ZHFrameLayout，而下拉刷新组件也在widget仓库，所以下拉刷新只能使用原始的PAGView
 *
 * @author liyue @ Zhihu Inc.
 * @since 05-08-2023
 */
public class PagView extends PAGView implements IAnimationView {
    public PagView(Context context) {
        super(context);
    }

    public PagView(Context context, EGLContext sharedContext) {
        super(context, sharedContext);
    }

    public PagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setAnimation(String assetName) {
        if (PagConfigKt.isPagResource(assetName)) {
            setComposition(PAGFile.Load(getContext().getAssets(), assetName));
            setRepeatCount(0);
        }
    }

    @Override
    @Deprecated
    public void setAnimation(int rawRes) {

    }

    @Override
    @Deprecated
    public void setAnimationFromJson(String jsonString, @Nullable String cacheKey) {

    }

    @Override
    public boolean isAnimating() {
        return isPlaying();
    }

    @Override
    public void playAnimation() {
        play();
    }

    @Override
    public void pauseAnimation() {
        stop();
    }
}
