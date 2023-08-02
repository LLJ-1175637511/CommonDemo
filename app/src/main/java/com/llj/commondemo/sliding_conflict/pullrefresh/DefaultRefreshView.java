package com.llj.commondemo.sliding_conflict.pullrefresh;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.llj.commondemo.R;

/**
 * @author yangjinyang @ Zhihu Inc.
 * @since 20-11-17
 */
public class DefaultRefreshView extends RefreshView {
    private static final String TAG = "DefaultRefreshView";

    private final IAnimationView mAnimationView;
    private final TextView mRefreshHintTextView;
    private View mProgressArea;
    private View mRefreshTip;

    private boolean mSecondRefreshEnabled = false;

    //下拉刷新
    private CharSequence mTextPullRefresh = "下拉刷新";

    //松开刷新
    private CharSequence mTextReleaseRefresh = "松开刷新";

    //刷新中
    private CharSequence mTextRefreshing = "刷新中";

    //继续下拉进入二楼
    private CharSequence mTextSecondPullRefresh;

    //松开进入二楼
    private CharSequence mTextSecondReleaseRefresh;

    //正在进入二楼
    private CharSequence mTextSecondRefreshing;

    /**
     * 二楼触发百分比 可以外部设置 默认0.42f
     */
    private float mTriggerSecondSyncPercent = 0.42f;

    //刷新提示展示时长
    private long mRefreshTipsShowingTime = 1500;

    public void setTriggerSecondSyncPercent(float triggerSecondSyncPercent) {
        if (triggerSecondSyncPercent > 0f) {
            this.mTriggerSecondSyncPercent = triggerSecondSyncPercent;
        }
    }

    public DefaultRefreshView(@NonNull Context context) {
        this(context, null);
    }

    public DefaultRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultRefreshView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.zxt_study_pag_refresh_view, this);
        mAnimationView = findViewById(R.id.animation_view);
        mAnimationView.setAnimation("pull_refresh_loading.pag");
        mRefreshHintTextView = findViewById(R.id.refresh_hint);
        mProgressArea = findViewById(R.id.progress_area);
    }

    /**
     * 注意：lottie 已替换成 pag，该接口废弃
     */
    @Deprecated
    public void setLottieAnimation(String assetName) {
        mAnimationView.setAnimation(assetName);
    }

    /**
     * 注意：lottie 已替换成 pag，该接口废弃
     */
    @Deprecated
    public void setLottieAnimation(@RawRes final int rawRes) {
        mAnimationView.setAnimation(rawRes);
    }

    public void setRefreshHint(CharSequence text) {
        mRefreshHintTextView.setText(text);
    }

    public void setRefreshHintEnabled(boolean enabled) {
        if (enabled) {
            mRefreshHintTextView.setVisibility(View.VISIBLE);
        } else {
            mRefreshHintTextView.setVisibility(View.GONE);
        }
    }

    public boolean isRefreshHintEnabled() {
        return mRefreshHintTextView.getVisibility() == View.VISIBLE;
    }

    public boolean isSecondRefreshEnabled() {
        return mSecondRefreshEnabled;
    }

    public void setSecondRefreshEnabled(boolean secondRefreshEnabled) {
        mSecondRefreshEnabled = secondRefreshEnabled;
    }

    public CharSequence getTextPullRefresh() {
        return mTextPullRefresh;
    }

    public void setTextPullRefresh(CharSequence textPullRefresh) {
        mTextPullRefresh = textPullRefresh;
    }

    public CharSequence getTextReleaseRefresh() {
        return mTextReleaseRefresh;
    }

    public void setTextReleaseRefresh(CharSequence textReleaseRefresh) {
        mTextReleaseRefresh = textReleaseRefresh;
    }

    public CharSequence getTextRefreshing() {
        return mTextRefreshing;
    }

    public void setTextRefreshing(CharSequence textRefreshing) {
        mTextRefreshing = textRefreshing;
    }

    public CharSequence getTextSecondPullRefresh() {
        return mTextSecondPullRefresh;
    }

    public void setTextSecondPullRefresh(CharSequence textSecondPullRefresh) {
        mTextSecondPullRefresh = textSecondPullRefresh;
    }

    public CharSequence getTextSecondReleaseRefresh() {
        return mTextSecondReleaseRefresh;
    }

    public void setTextSecondReleaseRefresh(CharSequence textSecondReleaseRefresh) {
        mTextSecondReleaseRefresh = textSecondReleaseRefresh;
    }

    public CharSequence getTextSecondRefreshing() {
        return mTextSecondRefreshing;
    }

    public void setTextSecondRefreshing(CharSequence textSecondRefreshing) {
        mTextSecondRefreshing = textSecondRefreshing;
    }

    /**
     * 注意：lottie 已替换成 pag，该接口废弃
     * 目前仅 video_entity 仓库的 VideoSecondFloorSupportImpl 在使用，已与仓库负责人崔树燕沟通，首页视频tab入口已经下线，该逻辑不会走到
     */
    @Deprecated
    public void setAnimationFromJson(String json, String cacheKey) {
        mAnimationView.setAnimationFromJson(json, cacheKey);
    }

    public void setTextColor(int colorRes) {
        mRefreshHintTextView.setTextColor(colorRes);
    }

    @Override
    protected void onProgress(float adjustedPercent, float tensionPercent, int offsetTop) {
        if (!mAnimationView.isAnimating()) {
            mAnimationView.playAnimation();
        }
        //更改点
        //原来的四段式文案改成两段式文案
        if (mSecondRefreshEnabled && tensionPercent > mTriggerSecondSyncPercent) {
            //继续下拉进入二楼
            setRefreshHint(mTextSecondPullRefresh);
        } else {
            //下拉刷新
            setRefreshHint(mTextPullRefresh);
        }
    }

    @Override
    public void onCancel() {
        mAnimationView.pauseAnimation();
    }

    @Override
    public void onRefresh() {
        //刷新中
        setRefreshHint(mTextRefreshing);
        mAnimationView.playAnimation();
    }

    @Override
    public void onSecondRefresh() {
        //正在进入二楼
        setRefreshHint(mTextSecondRefreshing);
    }

    @Override
    public void reset() {
        mAnimationView.pauseAnimation();
        hideRefreshTips();
    }

    @Override
    public void addRefreshTipView(RefreshTipFloatView refreshTipView) {
        if (mRefreshTip != null) {
            removeView(mRefreshTip);
            mRefreshTip = null;
            return;
        }
        if (refreshTipView == null || refreshTipView.getContentView() == null) {
            return;
        }
        if (refreshTipView.getDuration() >0){
            mRefreshTipsShowingTime = refreshTipView.getDuration();
        }
        LayoutParams layoutParams = new LayoutParams(refreshTipView.getContentView().getLayoutParams());
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        mRefreshTip = refreshTipView.getContentView();
        mRefreshTip.setAlpha(0);
        mRefreshTip.setTranslationY(0);
        addView(refreshTipView.getContentView(), layoutParams);
    }

    void showRefreshTips(Runnable runnable){
        if (null != mRefreshTip){
            mProgressArea.setAlpha(0);
            mRefreshTip.setTranslationY(0);
            mRefreshTip.animate().cancel();
            mRefreshTip.animate().alpha(1).translationY((getMeasuredHeight()-mRefreshTip.getMeasuredHeight())).setDuration(300).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    postDelayed(runnable,mRefreshTipsShowingTime);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }

    private void hideRefreshTips(){
        mProgressArea.setAlpha(1);
        if (null != mRefreshTip) {
            mRefreshTip.setTranslationY(0);
            mRefreshTip.setAlpha(0);
            removeView(mRefreshTip);
            mRefreshTip = null;
        }
    }

    @Override
    public View getRefreshTipsView() {
        return mRefreshTip;
    }

}