package com.llj.commondemo.sliding_conflict.pullrefresh;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RefreshTipFloatView {
    private View contentView;
    private long duration;

    private RefreshTipFloatView(Builder builder) {
        contentView = builder.contentView;
        duration = builder.duration;
    }

    long getDuration(){
        return duration;
    }

    public View getContentView(){
        return contentView;
    }

    public static final class Builder {
        private String textColor;
        private float textSize;
        private int textSizeType = TypedValue.COMPLEX_UNIT_SP;
        private View contentView;
        private String backgroundColor;
        private long duration;
        private String text;
        private int width;
        private int height;
        private int maxLines;
        private boolean useDefaultTextView;
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder withDefaultTextView() {
            useDefaultTextView = true;
            textColor = "GBL01A";
            textSize = 14;
            backgroundColor = "GBK99A";
            width = ViewGroup.LayoutParams.MATCH_PARENT;
            height = (int) (28 * context.getResources().getDisplayMetrics().density + 0.5f);
            maxLines = 1;
            return this;
        }

        public Builder textColor(String textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder textSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder textSize(int unit, float textSize) {
            this.textSize = textSize;
            textSizeType = unit;
            return this;
        }

        public Builder contentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder backgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder duration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder maxLines(int maxLines) {
            this.maxLines = maxLines;
            return this;
        }

        public RefreshTipFloatView build() {
            if (useDefaultTextView) {
                TextView tempText = new TextView(context);
                tempText.setTextSize(textSizeType, textSize);
                tempText.setText(text);
                tempText.setTextColor(context.getResources().getColor(context.getResources().getIdentifier(textColor, "color", context.getPackageName())));
                tempText.setLayoutParams(new ViewGroup.LayoutParams(width, height));
                tempText.setBackgroundColor(context.getResources().getColor(context.getResources().getIdentifier(backgroundColor, "color", context.getPackageName())));
                tempText.setGravity(Gravity.CENTER);
                contentView = tempText;
            }
            return new RefreshTipFloatView(this);
        }
    }


}
