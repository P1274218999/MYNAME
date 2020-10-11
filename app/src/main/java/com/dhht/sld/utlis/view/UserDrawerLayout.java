package com.dhht.sld.utlis.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

public class UserDrawerLayout extends DrawerLayout {
    public UserDrawerLayout(@NonNull Context context) {
        super(context);
    }

    public UserDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
