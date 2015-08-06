package com.zjwy.tiaobaojinew.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.adapter.CenterCouponAdapter;
import com.zjwy.tiaobaojinew.bean.CouponBean;
import com.zjwy.tiaobaojinew.utils.AppConstant;

public class CouponAcitivity extends BaseActivity {
	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.tv_other)
	private TextView tv_other;
	@ViewInject(R.id.lv_center_coupon)
	private ListView lv_center_coupon;

	// 返回
	@OnClick(R.id.tv_back)
	public void backButtonClick(View v) {
		onBackPressed();
	}

	// 登录
	@OnClick(R.id.tv_other)
	public void otherButtonClick(View v) {
		startActivity(new Intent(activity, LoginAcitivity.class));
	}

	@Override
	public void setView() {
		setContentView(R.layout.activity_coupon);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		tv_title.setText("优惠券");
		tv_other.setVisibility(View.VISIBLE);

	}

	/**
	 * 初始化数据--获得优惠券信息(登录后)
	 */
	@Override
	public void initData() {
		SharedPreferences sp = BaseApplication.getSharedPreferences();
		if (sp.getBoolean("isLogin", false)) {
			String userId = sp.getString("id", null);
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.send(HttpMethod.GET, AppConstant.GETCOUPON + userId,
					new RequestCallBack<String>() {

						private CenterCouponAdapter adapter;

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							BaseApplication.getApplication().showToast(
									"网络出了问题!");
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							try {
								JSONObject object = new JSONObject(arg0.result);
								if (object.getString("message").equals("OK")) {
									JSONObject object2 = object
											.getJSONObject("data");
									List<CouponBean> list = JSON.parseArray(
											object2.getString("infos"),
											CouponBean.class);
									adapter = new CenterCouponAdapter(list,
											CouponAcitivity.this);
									lv_center_coupon.setAdapter(adapter);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});

		} else {
			BaseApplication.getApplication().showToast("请登录!");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		initData();// 重新加载
	}

	@Override
	public void setListener() {
	}

}
