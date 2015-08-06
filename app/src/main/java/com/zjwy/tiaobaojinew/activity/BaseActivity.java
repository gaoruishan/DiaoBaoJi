package com.zjwy.tiaobaojinew.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.zjwy.tiaobaojinew.utils.AppManager;

/**
 * 创建一个基本的activity-----继承这个类，来管理所有的activity
 * 
 */
public abstract class BaseActivity extends ActionBarActivity {

	protected Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		AppManager.getAppManager().addActivity(this);
		setView();
		initView();
		setListener();
		initData();
	}

	/**
	 * 设置布局文件
	 */
	public abstract void setView();

	/**
	 * 初始化布局文件中的控件
	 */
	public abstract void initView();

	/**
	 * 设置控件的监听
	 */
	public abstract void setListener();

	/**
	 * 设置控件的监听
	 */
	public abstract void initData();

	/**
	 * 返回键---结束当前Activity（堆栈中最后一个压入的）
	 */

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		AppManager.getAppManager().finishActivity();
	}

	/**
	 * 封装方法--提供跳转
	 */
	public void startActivity(Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		startActivity(intent);
	}

}
