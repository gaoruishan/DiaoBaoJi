package com.zjwy.tiaobaojinew.activity;

import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zjwy.tiaobaojinew.R;

public class WithMeAcitivity extends BaseActivity {
	@ViewInject(R.id.tv_title_aboutus)
	private TextView tv_title_aboutus;
	@ViewInject(R.id.tv_back_us)
	private TextView tv_back_us;

	@OnClick(R.id.tv_back_us)
	public void backButtonClick(View v) { // 点击返回键
		onBackPressed();
	}

	@Override
	public void setView() {
		setContentView(R.layout.activity_aboutus);
		ViewUtils.inject(this);// 申明的时候用注解，要在此注入布局视图
	}

	@Override
	public void initView() {
		tv_title_aboutus.setText("公司简介");

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
