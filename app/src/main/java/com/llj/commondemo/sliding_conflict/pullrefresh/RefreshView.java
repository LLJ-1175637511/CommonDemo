package com.llj.commondemo.sliding_conflict.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @author yangjinyang @ Zhihu Inc.
 * @since 2021/11/3
 */
public abstract class RefreshView extends FrameLayout {
    private Animation.AnimationListener mListener;

    public RefreshView(@NonNull Context context) {
        super(context);
    }

    public RefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAnimationListener(Animation.AnimationListener listener) {
        mListener = listener;
    }

    @Override
    public void onAnimationStart() {
        super.onAnimationStart();
        if (mListener != null) {
            mListener.onAnimationStart(getAnimation());
        }
    }

    @Override
    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (mListener != null) {
            mListener.onAnimationEnd(getAnimation());
        }
    }

    protected abstract void onProgress(float adjustedPercent, float tensionPercent, int offsetTop);

    protected abstract void onRefresh();

    protected abstract void onSecondRefresh();

    public abstract void onCancel();

    public abstract void reset();

    /**
     * 注入更新提示的布局
     * 为了保持一定的灵活性，采取了注入布局的形式
     *
     * @param refreshTipView 更新提示的布局配置项
     *
     * 目前只在 {@link DefaultRefreshView}提供该能力
     * */
    public void addRefreshTipView(RefreshTipFloatView refreshTipView){};

    /**
     * 展示更新提示布局的动画，动画时间与下拉刷新组件里，下拉图案收起的时间同步 仅在{@link DefaultRefreshView}提供
     * */
    void showRefreshTips(Runnable runnable){}

    /**
     * 获取更新提示的布局，目前只在 {@link DefaultRefreshView}提供
     * */
    @Nullable
    View getRefreshTipsView(){
        return null;
    }
}
