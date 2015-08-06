package com.zjwy.tiaobaojinew.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 自定义持续保持滚动
 */
public class CustomDurationScroller extends Scroller {

	private double scrollFactor = 1;

	public CustomDurationScroller(Context context) {
		super(context);
	}

	public CustomDurationScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}

	/**
	 * 设置时间变化的因素
	 */
	public void setScrollDurationFactor(double scrollFactor) {
		this.scrollFactor = scrollFactor;
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy,
				(int) (duration * scrollFactor));
	}
}
