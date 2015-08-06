package com.zjwy.tiaobaojinew.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.widget.BadgeView;

/**
 * 添加到购物车的动画
 * 
 */
public class ShopCartAnimationUtil {
	private static ViewGroup anim_mask_layout;// 动画层
	private static ImageView buyImg;// 这是在界面上跑的小图片
	private static BadgeView buyNumView;// 显示购买数量的控件
	private static int buyNum = 0;// 购买数量
	private static ImageView shoppingcart;
	private static Activity context;

	/**
	 * 执行动画
	 */
	public static void startAnimation(final View v) {
		int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
		v.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
		anim_mask_layout = null;
		anim_mask_layout = createAnimLayout();

		anim_mask_layout.addView(buyImg);// 把动画小球添加到动画层
		final View view = addViewToAnimLayout(anim_mask_layout, buyImg,
				start_location);
		int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
		shoppingcart.getLocationInWindow(end_location);// shopCart是那个购物车
		// 计算位移
		int endX = end_location[0] - start_location[0];// 动画位移的X坐标
		int endY = end_location[1] - start_location[1];// 动画位移的y坐标
		TranslateAnimation translateAnimationX = new TranslateAnimation(0,
				endX, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
				0, endY);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(false);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(800);// 动画的执行时间
		view.startAnimation(set);
		// 动画监听事件
		set.setAnimationListener(new AnimationListener() {
			// 动画的开始
			@Override
			public void onAnimationStart(Animation animation) {
				buyImg.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			// 动画的结束
			@Override
			public void onAnimationEnd(Animation animation) {
				buyImg.setVisibility(View.GONE);
				buyNum++;// 让购买数量加1
				buyNumView.setText(buyNum + "");
				buyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT - 15);
				buyNumView.show();
			}
		});

	}

	public static void prepareAnimation(Activity currActivity, ImageView iv) {
		context = currActivity;
		shoppingcart = iv;
		buyNumView = new BadgeView(currActivity, shoppingcart);
		buyImg = new ImageView(currActivity);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
		buyImg.setImageResource(R.drawable.addshopcart_sign);// 设置buyImg的图片

	}

	/**
	 * @Description: 创建动画层
	 */
	private static ViewGroup createAnimLayout() {
		// 创建视图组，线性布局，参数设置，添加
		ViewGroup rootView = (ViewGroup) context.getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	/**
	 * 添加左上角提示(购物车按钮)
	 */
	private static View addViewToAnimLayout(final ViewGroup vg,
			final View view, int[] location) {
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}
}
