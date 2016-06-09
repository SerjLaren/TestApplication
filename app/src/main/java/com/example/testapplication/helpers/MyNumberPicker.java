package com.example.testapplication.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;

public class MyNumberPicker extends NumberPicker {

    public MyNumberPicker(Context ctx) {
        super(ctx);
    }

    public MyNumberPicker(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        setAttributes(attrs);
    }

    private void setAttributes(AttributeSet attrs) {
        this.setMinValue(attrs.getAttributeIntValue(null, "min_value", 0));
        this.setMaxValue(attrs.getAttributeIntValue(null, "max_value", 0));
    }
}
