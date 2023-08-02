/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.llj.commondemo.sliding_conflict.pullrefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ListViewCompat;

/**
 * The SwipeRefreshLayout should be used whenever the user can refresh the
 * contents of a view via a vertical swipe gesture. The activity that
 * instantiates this view should add an OnRefreshListener to be notified
 * whenever the swipe to refresh gesture is completed. The SwipeRefreshLayout
 * will notify the listener each and every time the gesture is completed again;
 * the listener is responsible for correctly determining when to actually
 * initiate a refresh of its content. If the listener determines there should
 * not be a refresh, it must call setRefreshing(false) to cancel any visual
 * indication of a refresh. If an activity wishes to show just the progress
 * animation, it should call setRefreshing(true). To disable the gesture and
 * progress animation, call setEnabled(false) on the view.
 * <p>
 * This layout should be made the parent of the view that will be refreshed as a
 * result of the gesture and can only support one direct child. This view will
 * also be made the target of the gesture and will be forced to match both the
 * width and the height supplied in this layout. The SwipeRefreshLayout does not
 * provide accessibility events; instead, a menu item must be provided to allow
 * refresh of the content wherever this gesture is used.
 * </p>
 */
public class PullRefreshLayout extends ViewGroup implements NestedScrollingParent3,
        NestedScrollingParent2, NestedScrollingChild3, NestedScrollingChild2, NestedScrollingParent,
        NestedScrollingChild {
    private static final String TAG = "PullRefreshLayout";

    public static final int DEFAULT_SLINGSHOT_DISTANCE = -1;

    private static final String LOG_TAG = PullRefreshLayout.class.getSimpleName();

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final int INVALID_POINTER = -1;
    private static final float DRAG_RATE = .5f;

    private static final int ANIMATE_TO_TRIGGER_DURATION = 300;

    private static final int ANIMATE_TO_START_DURATION = 300;

    // Default offset in dips from the top of the view to where the progress spinner should stop
    private static final int DEFAULT_REFRESH_TARGET = 56;

    private View mTarget; // the target of the gesture
    OnRefreshListener mListener;
    boolean mRefreshing = false;
    boolean mIsAnimating = false;
    private int mTouchSlop;
    private float mTotalDragDistance = -1;

    private OnSecondRefreshListener mSecondRefreshListener;
    private float mSecondDragDistance = -1;
    private boolean mSecondRefreshEnabled = false;

    private OnOffsetChangedListener mOffsetChangedListener;

    // If nested scrolling is enabled, the total amount that needed to be
    // consumed by this as the nested scrolling parent is used in place of the
    // overscroll determined by MOVE events in the onTouch handler
    private float mTotalUnconsumed;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];

    // Used for calls from old versions of onNestedScroll to v3 version of onNestedScroll. This only
    // exists to prevent GC costs that are present before API 21.
    private final int[] mNestedScrollingV2ConsumedCompat = new int[2];
    private boolean mNestedScrollInProgress;

    private int mMediumAnimationDuration;
    int mCurrentTargetOffsetTop;

    private float mInitialMotionY;
    private float mInitialDownY;
    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;

    // Target is returning to its start offset because it was cancelled or a
    // refresh was triggered.
    private boolean mReturningToStart;
    private final DecelerateInterpolator mDecelerateInterpolator;
    private static final int[] LAYOUT_ATTRS = new int[]{
            android.R.attr.enabled
    };

    RefreshView mProgressView;

    private int mProgressViewIndex = -1;

    protected int mFrom;

    protected int mOriginalOffsetTop;

    int mSpinnerOffsetEnd;

    int mCustomSlingshotDistance;

    boolean mNotify;

    // Whether the client has set a custom starting position;
    boolean mUsingCustomStart;

    private OnChildScrollUpCallback mChildScrollUpCallback;

    private OnDragPercentCallback mDragPercentCallback;

    /** @see #setLegacyRequestDisallowInterceptTouchEventEnabled */
    private boolean mEnableLegacyRequestDisallowInterceptTouch;

    public void setDragPercentCallback(OnDragPercentCallback onDragPercentCallback) {
        this.mDragPercentCallback = onDragPercentCallback;
    }

    /**
     * 二楼刷新
     */
    public static final int SECOND_REFRESH = 0;
    /**
     * 正常刷新
     */
    public static final int REFRESH = 1;
    /**
     * 取消刷新
     */
    public static final int CANCEL_REFRESH = 2;

    @Nullable
    private OnSecondRefreshComputeInterface mSecondRefreshComputeInterface;

    public void setFinishedAction(OnSecondRefreshComputeInterface computeInterface) {
        this.mSecondRefreshComputeInterface = computeInterface;
    }

    public interface OnSecondRefreshComputeInterface {
        int getAction(float offset);
    }

    private AnimationListener mRefreshListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            doRefresh();
        }
    };

    private void doRefresh() {
        if (mRefreshing) {
            // Make sure the progress view is fully visible
            mProgressView.onRefresh();
            if (mNotify) {
                if (mListener != null) {
                    mListener.onRefresh();
                }
            }
            mCurrentTargetOffsetTop = mProgressView.getTop();
        } else {
            reset();
        }
    }

    void reset() {
        mProgressView.reset();
        mProgressView.clearAnimation();

        mProgressView.setVisibility(View.GONE);
        // Return the circle to its start position
        setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCurrentTargetOffsetTop);
        mCurrentTargetOffsetTop = mProgressView.getTop();
        mIsAnimating = false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            reset();
        }
    }

//    @Parcelize
//    static class SavedState extends BaseSavedState {
//        final boolean mRefreshing;
//
//        /**
//         * Constructor called from {@link PullRefreshLayout#onSaveInstanceState()}
//         */
//        SavedState(Parcelable superState, boolean refreshing) {
//            super(superState);
//            this.mRefreshing = refreshing;
//        }
//
//        /**
//         * Constructor called from {@link #CREATOR}
//         */
//        SavedState(Parcel in) {
//            super(in);
//            mRefreshing = in.readByte() != 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel out, int flags) {
//            super.writeToParcel(out, flags);
//            out.writeByte(mRefreshing ? (byte) 1 : (byte) 0);
//        }
//
//        public static final Creator<SavedState> CREATOR =
//                new Creator<SavedState>() {
//                    @Override
//                    public SavedState createFromParcel(Parcel in) {
//                        return new SavedState(in);
//                    }
//
//                    @Override
//                    public SavedState[] newArray(int size) {
//                        return new SavedState[size];
//                    }
//                };
//    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        return new SavedState(superState, mRefreshing);
//    }

//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        SavedState savedState = (SavedState) state;
//        super.onRestoreInstanceState(savedState.getSuperState());
//        setRefreshing(savedState.mRefreshing);
//    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    /**
     * The refresh indicator starting and resting position is always positioned
     * near the top of the refreshing content. This position is a consistent
     * location, but can be adjusted in either direction based on whether or not
     * there is a toolbar or actionbar present.
     * <p>
     * <strong>Note:</strong> Calling this will reset the position of the refresh indicator to
     * <code>start</code>.
     * </p>
     *
     * @param start The offset in pixels from the top of this view at which the
     *              progress spinner should appear.
     * @param end   The offset in pixels from the top of this view at which the
     *              progress spinner should come to rest after a successful swipe
     *              gesture.
     */
    public void setProgressViewOffset(int start, int end) {
        ensureTarget();
        mOriginalOffsetTop = start;
        mSpinnerOffsetEnd = end;
        mUsingCustomStart = true;
        reset();
        mRefreshing = false;
    }

    /**
     * @return The offset in pixels from the top of this view at which the progress spinner should
     * appear.
     */
    public int getProgressViewStartOffset() {
        return mOriginalOffsetTop;
    }

    /**
     * @return The offset in pixels from the top of this view at which the progress spinner should
     * come to rest after a successful swipe gesture.
     */
    public int getProgressViewEndOffset() {
        return mSpinnerOffsetEnd;
    }

    /**
     * The refresh indicator resting position is always positioned near the top
     * of the refreshing content. This position is a consistent location, but
     * can be adjusted in either direction based on whether or not there is a
     * toolbar or actionbar present.
     *
     * @param end The offset in pixels from the top of this view at which the
     *            progress spinner should come to rest after a successful swipe
     *            gesture.
     */
    public void setProgressViewEndTarget(int end) {
        ensureTarget();
        mSpinnerOffsetEnd = end;
        mProgressView.invalidate();
    }

    /**
     * Sets the distance that the refresh indicator can be pulled beyond its resting position during
     * a swipe gesture. The default is {@link #DEFAULT_SLINGSHOT_DISTANCE}.
     *
     * @param slingshotDistance The distance in pixels that the refresh indicator can be pulled
     *                          beyond its resting position.
     */
    public void setSlingshotDistance(@Px int slingshotDistance) {
        mCustomSlingshotDistance = slingshotDistance;
    }

    /**
     * Simple constructor to use when creating a SwipeRefreshLayout from code.
     */
    public PullRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    /**
     * Constructor that is called when inflating SwipeRefreshLayout from XML.
     */
    public PullRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mMediumAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);

        setWillNotDraw(false);
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

        final DisplayMetrics metrics = getResources().getDisplayMetrics();

        createDefaultProgressView();
        setChildrenDrawingOrderEnabled(true);
        // the absolute offset has to take into account that the circle starts at an offset
        mSpinnerOffsetEnd = (int) (DEFAULT_REFRESH_TARGET * metrics.density);
        mTotalDragDistance = mSpinnerOffsetEnd;
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);

        mOriginalOffsetTop = mCurrentTargetOffsetTop = -mSpinnerOffsetEnd;
        moveToStart(1.0f,mOriginalOffsetTop);

        final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (mProgressViewIndex < 0) {
            return i;
        } else if (i == childCount - 1) {
            // Draw the selected child last
            return mProgressViewIndex;
        } else if (i >= mProgressViewIndex) {
            // Move the children after the selected child earlier one
            return i + 1;
        } else {
            // Keep the children before the selected child the same
            return i;
        }
    }

    private void createDefaultProgressView() {
        mProgressView = new DefaultRefreshView(getContext());
        mProgressView.setVisibility(View.GONE);
        addView(mProgressView);
    }

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe
     * gesture.
     */
    public void setOnRefreshListener(@Nullable OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * Set the listener to be notified when a second refresh is triggered via the swipe
     * gesture.
     */
    public void setOnSecondRefreshListener(@Nullable OnSecondRefreshListener listener) {
        mSecondRefreshListener = listener;
    }

    public void setOnOffsetChangedListener(OnOffsetChangedListener offsetChangedListener) {
        mOffsetChangedListener = offsetChangedListener;
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     *
     * @param refreshing Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && mRefreshing != refreshing) {
            mRefreshing = refreshing;
            mNotify = false;
            int endTarget;
//            if (!this.mUsingCustomStart) {
//                endTarget = this.mSpinnerOffsetEnd + this.mOriginalOffsetTop;
//            } else {
//                endTarget = this.mSpinnerOffsetEnd;
//            }
//
//            this.setTargetOffsetTopAndBottom(endTarget - this.mCurrentTargetOffsetTop);
//            animateOffsetToCorrectPosition(endTarget - this.mCurrentTargetOffsetTop, null);

            animateOffsetToCorrectPosition(this.mCurrentTargetOffsetTop, null);
            doRefresh();
        } else {
            setRefreshing(refreshing, false /* notify */);
        }
    }


    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {
                animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, null);
                doRefresh();
            } else {
                if (null != mProgressView.getRefreshTipsView()){
                    animateOffsetToRefreshTipsPosition(mCurrentTargetOffsetTop);
                    mProgressView.showRefreshTips(()-> animateOffsetToStartPosition(mCurrentTargetOffsetTop,mRefreshListener));
                }else {
                    animateOffsetToStartPosition(mCurrentTargetOffsetTop,mRefreshListener);
                }
            }
        }
    }

    private void setSecondRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {
                mProgressView.onSecondRefresh();
                if (notify) {
                    if (mSecondRefreshListener != null) {
                        mSecondRefreshListener.onRefresh();
                    }
                }
            } else {
                setRefreshing(false);
            }
        }
    }

    public void setSecondRefreshEnabled(boolean enabled) {
        mSecondRefreshEnabled = enabled;
    }

    public boolean isSecondRefreshEnabled() {
        return mSecondRefreshEnabled;
    }

    /**
     * set custom RefreshView
     */
    public void setRefreshView(RefreshView refreshView) {
        removeView(mProgressView);

        mProgressView = refreshView;
        mProgressView.setVisibility(View.GONE);

        addView(mProgressView);
    }

    /**
     * get current RefreshView
     */
    public RefreshView getRefreshView() {
        return mProgressView;
    }

    /**
     * @return Whether the SwipeRefreshWidget is actively showing refresh
     * progress.
     */
    public boolean isRefreshing() {
        return mRefreshing;
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mProgressView)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    /**
     * Set the distance to trigger a refresh in pixel
     */
    public void setDistanceToTriggerSync(int distance) {
        mTotalDragDistance = distance;
    }

    /**
     * Set the distance to trigger second refresh in pixel
     */
    public void setDistanceToTriggerSecondSync(int distance) {
        mSecondDragDistance = distance;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        final View child = mTarget;
        final int childLeft = getPaddingLeft();
//        final int childTop = getPaddingTop() + mCurrentTargetOffsetTop + mProgressView.getMeasuredHeight();
        final int childTop = getPaddingTop() - mCurrentTargetOffsetTop + mOriginalOffsetTop;
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        int circleWidth = mProgressView.getMeasuredWidth();
        int circleHeight = mProgressView.getMeasuredHeight();
        mProgressView.layout((width / 2 - circleWidth / 2), mCurrentTargetOffsetTop,
                (width / 2 + circleWidth / 2), mCurrentTargetOffsetTop + circleHeight);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        mTarget.measure(MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
        mProgressView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.AT_MOST));
        mProgressViewIndex = -1;
        // Get the index of the circleview.
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == mProgressView) {
                mProgressViewIndex = index;
                break;
            }
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (mChildScrollUpCallback != null) {
            return mChildScrollUpCallback.canChildScrollUp(this, mTarget);
        }
        if (mTarget instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mTarget, -1);
        }
        return mTarget.canScrollVertically(-1);
    }

    /**
     * Set a callback to override {@link PullRefreshLayout#canChildScrollUp()} method. Non-null
     * callback will return the value provided by the callback and ignore all internal logic.
     *
     * @param callback Callback that should be called when canChildScrollUp() is called.
     */
    public void setOnChildScrollUpCallback(@Nullable OnChildScrollUpCallback callback) {
        mChildScrollUpCallback = callback;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        final int action = ev.getActionMasked();
        int pointerIndex;

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled() || mReturningToStart || canChildScrollUp()
                || mRefreshing || mNestedScrollInProgress || mIsAnimating) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setTargetOffsetTopAndBottom(mOriginalOffsetTop - mProgressView.getTop());
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                mInitialDownY = ev.getY(pointerIndex);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float y = ev.getY(pointerIndex);
                startDragging(y);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsBeingDragged;
    }

    /**
     * Enables the legacy behavior of {@link #requestDisallowInterceptTouchEvent} from before
     * 1.1.0-alpha03, where the request is not propagated up to its parents in either of the
     * following two cases:
     * <ul>
     *     <li>The child as an {@link AbsListView} and the runtime is API < 21</li>
     *     <li>The child has nested scrolling disabled</li>
     * </ul>
     * Use this method <em>only</em> if your application:
     * <ul>
     *     <li>is upgrading SwipeRefreshLayout from &lt; 1.1.0-alpha03 to &gt;= 1.1.0-alpha03</li>
     *     <li>relies on a parent of SwipeRefreshLayout to intercept touch events and that
     *     parent no longer responds to touch events</li>
     *     <li>setting this method to {@code true} fixes that issue</li>
     * </ul>
     *
     * @param enabled {@code true} to enable the legacy behavior, {@code false} for default behavior
     * @deprecated Only use this method if the changes introduced in
     * {@link #requestDisallowInterceptTouchEvent} in version 1.1.0-alpha03 are breaking
     * your application.
     */
    @Deprecated
    public void setLegacyRequestDisallowInterceptTouchEventEnabled(boolean enabled) {
        mEnableLegacyRequestDisallowInterceptTouch = enabled;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // if this is a List < L or another view that doesn't support nested
        // scrolling, ignore this request so that the vertical scroll event
        // isn't stolen
        if ((android.os.Build.VERSION.SDK_INT < 21 && mTarget instanceof AbsListView)
                || (mTarget != null && !ViewCompat.isNestedScrollingEnabled(mTarget))) {
            if (mEnableLegacyRequestDisallowInterceptTouch) {
                // Nope.
            } else {
                // Ignore here, but pass it up to our parent
                ViewParent parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(b);
                }
            }
        } else {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }

    //<editor-fold desc="nested scroll impl">
    // NestedScrollingParent 3

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, @ViewCompat.NestedScrollType int type,
                               @NonNull int[] consumed) {
        if (type != ViewCompat.TYPE_TOUCH) {
            return;
        }

        // This is a bit of a hack. onNestedScroll is typically called up the hierarchy of nested
        // scrolling parents/children, where each consumes distances before passing the remainder
        // to parents.  In our case, we want to try to run after children, and after parents, so we
        // first pass scroll distances to parents and consume after everything else has.
        int consumedBeforeParents = consumed[1];
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow, type, consumed);
        int consumedByParents = consumed[1] - consumedBeforeParents;
        int unconsumedAfterParents = dyUnconsumed - consumedByParents;

        // There are two reasons why scroll distance may be totally consumed.  1) All of the nested
        // scrolling parents up the hierarchy implement NestedScrolling3 and consumed all of the
        // distance or 2) at least 1 nested scrolling parent doesn't implement NestedScrolling3 and
        // for comparability reasons, we are supposed to act like they have.
        //
        // We must assume 2) is the case because we have no way of determining that it isn't, and
        // therefore must fallback to a previous hack that was done before nested scrolling 3
        // existed.
        int remainingDistanceToScroll;
        if (unconsumedAfterParents == 0) {
            // The previously implemented hack is to see how far we were offset and assume that that
            // distance is equal to how much all of our parents consumed.
            remainingDistanceToScroll = dyUnconsumed + mParentOffsetInWindow[1];
        } else {
            remainingDistanceToScroll = unconsumedAfterParents;
        }

        // Not sure why we have to make sure the child can't scroll up... but seems dangerous to
        // remove.
        if (remainingDistanceToScroll < 0 && !canChildScrollUp()) {
            mTotalUnconsumed += Math.abs(remainingDistanceToScroll);
            moveSpinner(mTotalUnconsumed);

            // If we've gotten here, we need to consume whatever is left to consume, which at this
            // point is either equal to 0, or remainingDistanceToScroll.
            consumed[1] += unconsumedAfterParents;
        }
    }

    // NestedScrollingParent 2

    @Override
    public boolean onStartNestedScroll(View child, View target, int axes, int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            return onStartNestedScroll(child, target, axes);
        } else {
            return false;
        }
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes, int type) {
        // Should always be true because onStartNestedScroll returns false for all type !=
        // ViewCompat.TYPE_TOUCH, but check just in case.
        if (type == ViewCompat.TYPE_TOUCH) {
            onNestedScrollAccepted(child, target, axes);
        }
    }

    @Override
    public void onStopNestedScroll(View target, int type) {
        // Should always be true because onStartNestedScroll returns false for all type !=
        // ViewCompat.TYPE_TOUCH, but check just in case.
        if (type == ViewCompat.TYPE_TOUCH) {
            onStopNestedScroll(target);
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed, int type) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type,
                mNestedScrollingV2ConsumedCompat);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
        // Should always be true because onStartNestedScroll returns false for all type !=
        // ViewCompat.TYPE_TOUCH, but check just in case.
        if (type == ViewCompat.TYPE_TOUCH) {
            onNestedPreScroll(target, dx, dy, consumed);
        }
    }

    // NestedScrollingParent 1

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && !mReturningToStart && !mRefreshing && !mIsAnimating
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
        mNestedScrollInProgress = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            moveSpinner(mTotalUnconsumed);
        }

        // If a client layout is using a custom start position for the circle
        // view, they mean to hide it again before scrolling the child view
        // If we get back to mTotalUnconsumed == 0 and there is more to go, hide
        // the circle so it isn't exposed if its blocking content is moved
        if (mUsingCustomStart && dy > 0 && mTotalUnconsumed == 0
                && Math.abs(dy - consumed[1]) > 0) {
            mProgressView.setVisibility(View.GONE);
        }

        // Now let our nested parent consume the leftovers
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0) {
            finishSpinner(mTotalUnconsumed);
            mTotalUnconsumed = 0;
        }
        // Dispatch up our nested parent
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                ViewCompat.TYPE_TOUCH, mNestedScrollingV2ConsumedCompat);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX,
                                    float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY,
                                 boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    // NestedScrollingChild 3

    @Override
    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                     int dyUnconsumed, @Nullable int[] offsetInWindow,
                                     @ViewCompat.NestedScrollType int type,
                                     @NonNull int[] consumed) {
        if (type == ViewCompat.TYPE_TOUCH) {
            mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,
                    dyUnconsumed, offsetInWindow, type, consumed);
        }
    }

    // NestedScrollingChild 2

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return type == ViewCompat.TYPE_TOUCH && startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll(int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            stopNestedScroll();
        }
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return type == ViewCompat.TYPE_TOUCH && hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow, int type) {
        return type == ViewCompat.TYPE_TOUCH && mNestedScrollingChildHelper.dispatchNestedScroll(
                dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow,
                                           int type) {
        return type == ViewCompat.TYPE_TOUCH && dispatchNestedPreScroll(dx, dy, consumed,
                offsetInWindow);
    }

    // NestedScrollingChild 1

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
    //</editor-fold>

    private boolean isAnimationRunning(Animation animation) {
        return animation != null && animation.hasStarted() && !animation.hasEnded();
    }

    /**
     * 根据手指滑动的距离，设置 progressView 位置
     *
     * @param overscrollTop 距离顶部距离
     */
    public void moveSpinner(float overscrollTop) {
        //实际滑动百分比
        float originalDragPercent = overscrollTop / mTotalDragDistance;
        //指示器滑动百分比
        float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
        float extraOS = Math.abs(overscrollTop) - mTotalDragDistance;
        //额外可下拉的距离
        float slingshotDist = mCustomSlingshotDistance > mTotalDragDistance
                ? mCustomSlingshotDistance - mTotalDragDistance
                : (mUsingCustomStart
                ? mSpinnerOffsetEnd - mOriginalOffsetTop
                : mSpinnerOffsetEnd);
        float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, slingshotDist * 2) / slingshotDist);
        float tensionPercent = (float) ((tensionSlingshotPercent / 4)
                - Math.pow((tensionSlingshotPercent / 4), 2)) * 2f;
        float extraMove = slingshotDist * tensionPercent * 2;
        int offsetTop = (int) ((mTotalDragDistance * dragPercent) + extraMove);

        int targetY = mOriginalOffsetTop + offsetTop;

        // where 1.0f is a full circle
        if (mProgressView.getVisibility() != View.VISIBLE) {
            mProgressView.setVisibility(View.VISIBLE);
        }
        if (mProgressView.getRefreshTipsView()!=null){
            mProgressView.reset();
        }
        //progress
        if (mDragPercentCallback != null) {
            mDragPercentCallback.onDragPercent(dragPercent, extraMove / slingshotDist, offsetTop);
        }
        mProgressView.onProgress(dragPercent, extraMove / slingshotDist, offsetTop);
        setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop);
    }

    /**
     * 下拉结束，手指抬起或是嵌套滑动结束时触发
     */
    private void finishSpinner(float overscrollTop) {
        //overscrollTop 是手指下拉的距离，这里需要 view 实际滑动的距离
        float offsetTop = mCurrentTargetOffsetTop - mOriginalOffsetTop;
        //此处的更改点 在下拉二楼二期 为了适配不同屏幕的机型 在命中二楼的情况下
        //由外部计算是否应该触发二楼（计算方式为滑动的距离是否达到某个百分比）
        if (mSecondRefreshEnabled) {
            if (mSecondRefreshComputeInterface != null) {
                //如果设置了这个计算接口 则外部计算 否则内部计算 用一期逻辑
                int action = mSecondRefreshComputeInterface.getAction(offsetTop);
                if (action == SECOND_REFRESH) {
                    setSecondRefreshing(true, true);
                } else if (action == REFRESH) {
                    setRefreshing(true, true /* notify */);
                } else {
                    // cancel refresh
                    mRefreshing = false;
                    animateOffsetToStartPosition(mCurrentTargetOffsetTop, null);
                    mProgressView.onCancel();
                }
            } else if (mSecondDragDistance > 0 && offsetTop > mSecondDragDistance) {
                setSecondRefreshing(true, true);
            } else if (offsetTop > mTotalDragDistance) {
                setRefreshing(true, true /* notify */);
            } else {
                mRefreshing = false;
                animateOffsetToStartPosition(mCurrentTargetOffsetTop, null);
                mProgressView.onCancel();
            }
        } else if (offsetTop > mTotalDragDistance) {
            setRefreshing(true, true /* notify */);
        } else {
            // cancel refresh
            mRefreshing = false;
            animateOffsetToStartPosition(mCurrentTargetOffsetTop, null);
            mProgressView.onCancel();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        int pointerIndex = -1;

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled() || mReturningToStart || canChildScrollUp()
                || mRefreshing || mNestedScrollInProgress || mIsAnimating) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = ev.getY(pointerIndex);
                startDragging(y);

                if (mIsBeingDragged) {
                    final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
                    if (overscrollTop > 0) {
                        // While the spinner is being dragged down, our parent shouldn't try
                        // to intercept touch events. It will stop the drag gesture abruptly.
                        getParent().requestDisallowInterceptTouchEvent(true);
                        moveSpinner(overscrollTop);
                    } else {
                        return false;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                pointerIndex = ev.getActionIndex();
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG,
                            "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                mActivePointerId = ev.getPointerId(pointerIndex);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    return false;
                }

                if (mIsBeingDragged) {
                    final float y = ev.getY(pointerIndex);
                    final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
                    mIsBeingDragged = false;
                    finishSpinner(overscrollTop);
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
            case MotionEvent.ACTION_CANCEL:
                return false;
        }

        return true;
    }

    private void startDragging(float y) {
        final float yDiff = y - mInitialDownY;
        if (yDiff > mTouchSlop && !mIsBeingDragged) {
            mInitialMotionY = mInitialDownY + mTouchSlop;
            mIsBeingDragged = true;
        }
    }

    /**
     * 下拉结束触发刷新时，将 progressView 移到刷新位置
     */
    private void animateOffsetToCorrectPosition(int from, AnimationListener listener) {
        mFrom = from;
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(ANIMATE_TO_TRIGGER_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        mProgressView.setAnimationListener(listener);
        mProgressView.clearAnimation();
        mProgressView.setVisibility(View.VISIBLE);
        mProgressView.startAnimation(mAnimateToCorrectPosition);
    }

    /**
     * 下拉结束，但是未触发刷新，将 progressView 置回初始位置
     */
    private void animateOffsetToStartPosition(int from, AnimationListener listener) {
        mFrom = from;
        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(ANIMATE_TO_START_DURATION);
        //mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        if (listener != null) {
            mProgressView.setAnimationListener(listener);
        }
        mProgressView.clearAnimation();
        mProgressView.startAnimation(mAnimateToStartPosition);
    }


    /**
     * 下拉刷新结束后，将布局向上收回，到恰好展示更新提示的布局的位置.
     */
    private void animateOffsetToRefreshTipsPosition(int from) {
        mFrom = from;
        mIsAnimating = true;
        mAnimateToRefreshTipsPosition.reset();
        mAnimateToRefreshTipsPosition.setDuration(ANIMATE_TO_START_DURATION);
        mProgressView.clearAnimation();
        mProgressView.startAnimation(mAnimateToRefreshTipsPosition);
    }

    /**
     * 手动调用 setRefreshing(true) 触发刷新时，将 progressView 移到刷新位置
     *
     * @return
     */
    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int endTarget = 0;
            if (!mUsingCustomStart) {
                endTarget = mSpinnerOffsetEnd - Math.abs(mOriginalOffsetTop);
            } else {
                endTarget = mSpinnerOffsetEnd;
            }
            int targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mProgressView.getTop();
            setTargetOffsetTopAndBottom(offset);
        }
    };

    /**
     * 将 progressView 移到起始位置
     *
     * @param interpolatedTime 0 ~ 1, 1 为起始位置
     */
    void moveToStart(float interpolatedTime,int targetOffset) {
        int targetTop = (mFrom + (int) ((targetOffset - mFrom) * interpolatedTime));
        int offset = targetTop - mProgressView.getTop();
        setTargetOffsetTopAndBottom(offset);
    }

    /**
     * 下拉结束，但是未触发刷新，将 progressView 置回初始位置
     */
    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime,mOriginalOffsetTop);
        }
    };

    /**
     * 下拉刷新结束后，，将 progressView 向上移动，到刚好展示更新提示的视图
     */
    private final Animation mAnimateToRefreshTipsPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            if (null != mProgressView.getRefreshTipsView()){
                moveToStart(interpolatedTime,-mProgressView.getRefreshTipsView().getMeasuredHeight());
            }
        }
    };

    /**
     * 设置 progressView 距离顶部的偏移
     */
    void setTargetOffsetTopAndBottom(int offset) {
        mProgressView.bringToFront();
        ViewCompat.offsetTopAndBottom(mProgressView, offset);
        mCurrentTargetOffsetTop = mProgressView.getTop();
        if (mTarget != null) {
            ViewCompat.offsetTopAndBottom(mTarget, offset);
            //mTarget.setTranslationY(mProgressView.getBottom());
        }

        if (mOffsetChangedListener != null && offset != 0) {
            int offsetTop = mCurrentTargetOffsetTop - mOriginalOffsetTop;
            mOffsetChangedListener.onOffsetChanged(this, offsetTop);
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a refresh should implement this interface.
     */
    public interface OnRefreshListener {
        /**
         * Called when a swipe gesture triggers a refresh.
         */
        void onRefresh();
    }

    public interface OnSecondRefreshListener {
        void onRefresh();
    }

    public interface OnOffsetChangedListener {
        /**
         * Called when the vertical offset has been changed. This allows
         * child views to implement custom behavior based on the offset
         *
         * @param refreshLayout  the {@link PullRefreshLayout} which offset has changed
         * @param verticalOffset the vertical offset for the parent {@link PullRefreshLayout}, in px
         */
        void onOffsetChanged(PullRefreshLayout refreshLayout, int verticalOffset);
    }

    /**
     * Classes that wish to override {@link PullRefreshLayout#canChildScrollUp()} method
     * behavior should implement this interface.
     */
    public interface OnChildScrollUpCallback {
        /**
         * Callback that will be called when {@link PullRefreshLayout#canChildScrollUp()} method
         * is called to allow the implementer to override its behavior.
         *
         * @param parent SwipeRefreshLayout that this callback is overriding.
         * @param child  The child view of SwipeRefreshLayout.
         * @return Whether it is possible for the child view of parent layout to scroll up.
         */
        boolean canChildScrollUp(@NonNull PullRefreshLayout parent, @Nullable View child);
    }

    public interface OnDragPercentCallback {
        void onDragPercent(float adjustedPercent, float tensionPercent, int offsetTop);
    }

}
