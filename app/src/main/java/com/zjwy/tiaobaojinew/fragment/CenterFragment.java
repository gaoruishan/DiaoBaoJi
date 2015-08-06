package com.zjwy.tiaobaojinew.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.activity.CouponAcitivity;
import com.zjwy.tiaobaojinew.activity.LoginAcitivity;
import com.zjwy.tiaobaojinew.activity.MyOrderActivity;
import com.zjwy.tiaobaojinew.activity.UserInfoAcitivity;
import com.zjwy.tiaobaojinew.activity.WithMeAcitivity;
import com.zjwy.tiaobaojinew.utils.StringUtils;

public class CenterFragment extends BaseFragment implements OnClickListener {

	private ImageView iv_center_head;
	private TextView tv_accout_num;
	private View view;

	/**
	 * 提供一个静态的方法传值，返回实例
	 * 
	 */
	public static CenterFragment getInstance() {
		return new CenterFragment();
	}

	@Override
	public void onResume() {// 登录 ---销毁跳转---设置头像！
		super.onStart();
		SharedPreferences sp = activity.getSharedPreferences("config",
				activity.MODE_PRIVATE);
		if (sp.getBoolean("isLogin", false)) {
			initView();
			String fileUrl = sp.getString("avatar", null);
			String username = sp.getString("username", null);
			if (!StringUtils.isEmpty(fileUrl)) {
				BitmapUtils bitmapUtils = new BitmapUtils(activity);
				bitmapUtils.display(iv_center_head, fileUrl);
			}
			if (!StringUtils.isEmpty(username)) {
				tv_accout_num.setText("当前账号：" + username);
				tv_accout_num.setTextColor(Color.WHITE);
			}

		} else {
			iv_center_head.setVisibility(View.GONE);
			tv_accout_num.setVisibility(View.GONE);
		}
	}

	private void initView() {
		iv_center_head.setVisibility(View.VISIBLE);
		tv_accout_num.setVisibility(View.VISIBLE);
		iv_center_head.setOnClickListener(this);
	}

	/**
	 * 视图创建时fragment，将布局添加到viewpager中
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout_centers, container, false);
		initView(view);
		return view;
	}

	/**
	 * 初始化控件
	 * 
	 * @param view
	 * @param inflater
	 */
	private void initView(View view) {
		iv_center_head = (ImageView) view.findViewById(R.id.iv_center_head);
		tv_accout_num = (TextView) view.findViewById(R.id.tv_accout_num);
		view.findViewById(R.id.login).setOnClickListener(this);
		view.findViewById(R.id.center_orderlist).setOnClickListener(this);
		view.findViewById(R.id.center_coupon).setOnClickListener(this);
		view.findViewById(R.id.center_userinfo).setOnClickListener(this);
		view.findViewById(R.id.center_aboutus).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.login:// 登录
			startActivity(LoginAcitivity.class);
			break;
		case R.id.center_orderlist:// 我的订单
			startActivity(MyOrderActivity.class);
			break;
		case R.id.center_userinfo:// 个人信息
			startActivity(UserInfoAcitivity.class);
			break;
		case R.id.iv_center_head:// 个人信息
			startActivity(UserInfoAcitivity.class);
			break;
		case R.id.center_coupon:// 我的优惠券
			startActivity(CouponAcitivity.class);
			break;
		case R.id.center_aboutus:// 关于我们
			startActivity(WithMeAcitivity.class);
			break;
		}
	}
}
