package com.llj.commondemo.sliding_conflict.ui.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.llj.commondemo.sliding_conflict.other.FlingHelper;

public class NestedScrollLayout extends NestedScrollView {

  private static final String TAG = "NestedScrollLayout";

  public NestedScrollLayout(Context context) {
      this(context, null);
      init();
  }

  public NestedScrollLayout(Context context, @Nullable AttributeSet attrs) {
      this(context, attrs, 0);
      init();
  }

  public NestedScrollLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
      this(context, attrs, defStyleAttr, 0);
      init();
  }

  public NestedScrollLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr);
      init();
  }

  private FlingHelper mFlingHelper;

  int totalDy = 0;
  /**
   * 用于判断RecyclerView是否在fling
   */
  boolean isStartFling = false;
  /**
   * 记录当前滑动的y轴加速度
   */
  private int velocityY = 0;

  void init() {
      mFlingHelper = new FlingHelper(getContext());
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          setOnScrollChangeListener(new View.OnScrollChangeListener() {
              @Override
              public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                  if (isStartFling) {
                      totalDy = 0;
                      isStartFling = false;
                  }
                  if (scrollY == 0) {
                      Log.i(TAG, "TOP SCROLL");
                      // refreshLayout.setEnabled(true);
                  }
                  if (scrollY == (getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                      Log.i(TAG, "BOTTOM SCROLL");
                      dispatchChildFling();
                  }
                  //在RecyclerView fling情况下，记录当前RecyclerView在y轴的偏移
                  totalDy += scrollY - oldScrollY;
              }
          });
      }
  }


  private void dispatchChildFling() {
      if (velocityY != 0) {
          double splineFlingDistance = mFlingHelper.getSplineFlingDistance(velocityY);
          if (splineFlingDistance > totalDy) {
              childFling(mFlingHelper.getVelocityByDistance(splineFlingDistance - (double) totalDy));
          }
      }
      totalDy = 0;
      velocityY = 0;
  }

  private void childFling(int velY) {
      RecyclerView childRecyclerView = getChildRecyclerView((ViewGroup) getRootView());
      if (childRecyclerView != null) {
          childRecyclerView.fling(0, velY);
      }
  }

  @Override
  public void fling(int velocityY) {
      super.fling(velocityY);
      //记录速度
      if (velocityY <= 0) {
          this.velocityY = 0;
      } else {
          isStartFling = true;
          this.velocityY = velocityY;
      }
  }

  private RecyclerView getChildRecyclerView(ViewGroup viewGroup) {
      for (int i = 0; i < viewGroup.getChildCount(); i++) {
          View view = viewGroup.getChildAt(i);
          if (view instanceof RecyclerView) {
              return (RecyclerView) viewGroup.getChildAt(i);
          } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
              RecyclerView childRecyclerView = getChildRecyclerView((ViewGroup) viewGroup.getChildAt(i));
              if (childRecyclerView != null) {
                  return childRecyclerView;
              }
          }
          continue;
      }
      return null;
  }
}