package com.dhht.sld.utlis;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class IconFontView extends androidx.appcompat.widget.AppCompatTextView {

    public IconFontView(Context context) {
        super(context);
        init(context);
    }

    public IconFontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IconFontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        setTypeface(iconfont);
    }
}
