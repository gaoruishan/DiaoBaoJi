package com.zjwy.tiaobaojinew.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.utils.ActivitySplitAnimationUtil;

public class Guide2Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 准备图像分割
		ActivitySplitAnimationUtil.prepareAnimation(this);

		setContentView(R.layout.activity_splash_split_two);

		findViewById(R.id.three).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivitySplitAnimationUtil.startActivity(Guide2Activity.this,
						new Intent(Guide2Activity.this, MainActivity.class));
				finish();
			}
		});

		// 开启动画，展示Activity
		ActivitySplitAnimationUtil.animate(this, 1000);
	}

}
