package com.zjwy.tiaobaojinew.widget;

import com.zjwy.tiaobaojinew.interf.setMyScrollViewListener;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyScrollView(Context context) {
		super(context);
	}

	setMyScrollViewListener _t = null;

	public void onScroll(setMyScrollViewListener t) {
		_t = t;
	}

	// 是否正在移动
	public boolean istouch = false;

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		if (istouch) {
			if (this.getScrollY() + this.getHeight() >= computeVerticalScrollRange()) {
				istouch = false;
				_t.down();
			}
			if (t == 0) {
				istouch = false;
				_t.up();
			}
		}

	}

}
