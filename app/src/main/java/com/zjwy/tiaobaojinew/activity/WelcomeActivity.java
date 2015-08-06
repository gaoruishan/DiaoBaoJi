package com.zjwy.tiaobaojinew.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import cn.jpush.android.api.JPushInterface;

import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.utils.ActivitySplitAnimationUtil;
import com.zjwy.tiaobaojinew.utils.Utils;

/**
 * 引导页--设置图片
 * 
 */
public class WelcomeActivity extends BaseActivity {
	private static final int GO_MAIN = 0;
	private static final int GO_GUIDE = 1;

	@Override
	public void setView() {
		setContentView(R.layout.activity_welcome);
	}

	@Override
	public void initView() {
		// 判断是否第一次运行
		final boolean isFirst = Utils.isFirstRun(this);
		if (isFirst) {
			handler.sendEmptyMessageDelayed(GO_GUIDE, 400);
		} else {
			handler.sendEmptyMessageDelayed(GO_MAIN, 400);
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GO_MAIN:
				ActivitySplitAnimationUtil.startActivity(WelcomeActivity.this,
						new Intent(WelcomeActivity.this, MainActivity.class));
				break;
			case GO_GUIDE:
				ActivitySplitAnimationUtil.startActivity(WelcomeActivity.this,
						new Intent(WelcomeActivity.this, Guide1Activity.class));
			}
			onBackPressed();// ==finish()
		};
	};

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.init(getApplicationContext());
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}
}
