package com.zjwy.tiaobaojinew.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.utils.ActivitySplitAnimationUtil;

public class Guide1Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 准备图像分割
		ActivitySplitAnimationUtil.prepareAnimation(this);

		setContentView(R.layout.activity_splash_split_one);

		findViewById(R.id.two).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivitySplitAnimationUtil.startActivity(Guide1Activity.this,
						new Intent(Guide1Activity.this, Guide2Activity.class));
				finish();
			}
		});

		// 开启动画，展示Activity
		ActivitySplitAnimationUtil.animate(this, 1000);
	}

}
