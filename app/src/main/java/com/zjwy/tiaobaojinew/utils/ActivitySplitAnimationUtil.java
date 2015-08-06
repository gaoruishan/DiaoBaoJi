package com.zjwy.tiaobaojinew.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * 分割Activity的动画
 * 
 * @author Udi Cohen (@udinic)
 */
public class ActivitySplitAnimationUtil {

	public static Bitmap mBitmap = null;
	private static int[] mLoc1;
	private static int[] mLoc2;
	private static ImageView mTopImage;
	private static ImageView mBottomImage;
	private static AnimatorSet mSetAnim;

	/**
	 * 准备位图和intent
	 */
	public static void startActivity(Activity currActivity, Intent intent,
			int splitYCoord) {

		// 准备位图
		prepare(currActivity, splitYCoord);

		currActivity.startActivity(intent);
		currActivity.overridePendingTransition(0, 0);// 边距过渡
	}

	/**
	 * 开启一个Activity进行分割动画
	 */
	public static void startActivity(Activity currActivity, Intent intent) {
		startActivity(currActivity, intent, -1);
	}

	/**
	 * 准备两个图形，展示当前Activity (在Activity#onCreate()中调用)
	 */
	public static void prepareAnimation(final Activity destActivity) {
		mTopImage = createImageView(destActivity, mBitmap, mLoc1);
		mBottomImage = createImageView(destActivity, mBitmap, mLoc2);
	}

	/**
	 * 设置展示分割的动画
	 */
	public static void animate(final Activity destActivity, final int duration,
			final TimeInterpolator interpolator) {

		// 向主线程消息队列推送信息
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				// 设置动画
				mSetAnim = new AnimatorSet();
				mTopImage.setLayerType(View.LAYER_TYPE_HARDWARE, null);
				mBottomImage.setLayerType(View.LAYER_TYPE_HARDWARE, null);
				mSetAnim.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						clean(destActivity);// 清理
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						clean(destActivity);
					}

					@Override
					public void onAnimationRepeat(Animator animation) {

					}
				});
				// ObjectAnimator：　继承自ValueAnimator，要指定一个对象及该对象的一个属性，当属性值计算完成时自动设置为该对象的相应属性
				// 两个动画--分割
				Animator anim1 = ObjectAnimator.ofFloat(mTopImage,
						"translationY", mTopImage.getHeight() * -1);
				Animator anim2 = ObjectAnimator.ofFloat(mBottomImage,
						"translationY", mBottomImage.getHeight());

				if (interpolator != null) {
					anim1.setInterpolator(interpolator);
					anim2.setInterpolator(interpolator);
				}
				// 执行时间，一起，开始
				mSetAnim.setDuration(duration);
				mSetAnim.playTogether(anim1, anim2);
				mSetAnim.start();
			}
		});
	}

	/**
	 * 开启动画，展示Activity(在Activity#onCreate()中调用)
	 */
	public static void animate(final Activity destActivity, final int duration) {
		animate(destActivity, duration, new DecelerateInterpolator());
	}

	/**
	 * 取消动画进程
	 */
	public static void cancel() {
		if (mSetAnim != null)
			mSetAnim.cancel();
	}

	/**
	 * 清理图形
	 */
	private static void clean(Activity activity) {
		if (mTopImage != null) {
			mTopImage.setLayerType(View.LAYER_TYPE_NONE, null);
			try {
				// If we use the regular removeView() we'll get a small UI
				// glitch
				activity.getWindowManager().removeViewImmediate(mBottomImage);
			} catch (Exception ignored) {
			}
		}
		if (mBottomImage != null) {
			mBottomImage.setLayerType(View.LAYER_TYPE_NONE, null);
			try {
				activity.getWindowManager().removeViewImmediate(mTopImage);
			} catch (Exception ignored) {
			}
		}

		mBitmap = null;
	}

	/**
	 * 准备图形for动画
	 */
	private static void prepare(Activity currActivity, int splitYCoord) {

		// Get the content of the activity and put in a bitmap
		View root = currActivity.getWindow().getDecorView()
				.findViewById(android.R.id.content);
		root.setDrawingCacheEnabled(true);
		mBitmap = root.getDrawingCache();

		// -1进行Y轴的等分割
		splitYCoord = (splitYCoord != -1 ? splitYCoord
				: mBitmap.getHeight() / 2);

		if (splitYCoord > mBitmap.getHeight())
			throw new IllegalArgumentException("Split Y coordinate ["
					+ splitYCoord + "] exceeds the activity's height ["
					+ mBitmap.getHeight() + "]");

		// Set the location to put the 2 bitmaps on the destination activity
		mLoc1 = new int[] { 0, splitYCoord, root.getTop() };
		mLoc2 = new int[] { splitYCoord, mBitmap.getHeight(), root.getTop() };
	}

	/**
	 * 创建ImageView
	 */
	private static ImageView createImageView(Activity destActivity, Bitmap bmp,
			int loc[]) {
		MyImageView imageView = new MyImageView(destActivity);
		imageView.setImageBitmap(bmp);
		imageView.setImageOffsets(bmp.getWidth(), loc[0], loc[1]);

		WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
		windowParams.gravity = Gravity.TOP;
		windowParams.x = 0;
		windowParams.y = loc[2] + loc[0];
		windowParams.height = loc[1] - loc[0];
		windowParams.width = bmp.getWidth();
		windowParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		windowParams.format = PixelFormat.TRANSLUCENT;
		windowParams.windowAnimations = 0;
		destActivity.getWindowManager().addView(imageView, windowParams);

		return imageView;
	}

	/**
	 * 自定义一个ImageView
	 */
	private static class MyImageView extends ImageView {
		private Rect mSrcRect;
		private Rect mDstRect;
		private Paint mPaint;

		public MyImageView(Context context) {
			super(context);
			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		}

		/**
		 * 设置bitmap的可见区域
		 */
		public void setImageOffsets(int width, int startY, int endY) {
			mSrcRect = new Rect(0, startY, width, endY);
			mDstRect = new Rect(0, 0, width, endY - startY);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			Bitmap bm = null;
			Drawable drawable = getDrawable();
			if (null != drawable && drawable instanceof BitmapDrawable) {
				bm = ((BitmapDrawable) drawable).getBitmap();
			}

			if (null == bm) {
				super.onDraw(canvas);
			} else {
				canvas.drawBitmap(bm, mSrcRect, mDstRect, mPaint);
			}
		}
	}
}
