package com.zjwy.tiaobaojinew.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.adapter.CenterOrderListViewAdapter;
import com.zjwy.tiaobaojinew.adapter.CenterOrderListViewAdapter1;
import com.zjwy.tiaobaojinew.bean.OrderBean;
import com.zjwy.tiaobaojinew.utils.AppConstant;

public class MyOrderActivity extends BaseActivity {
	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.tv_other)
	private TextView tv_other;
	@ViewInject(R.id.tv_myorder_all)
	private TextView tv_myorder_all;
	@ViewInject(R.id.tv_myorder_other)
	private TextView tv_myorder_other;
	@ViewInject(R.id.lv_center_order)
	private ListView lv_center_order;
	private boolean isLogin = true;
	public static final int COLOR = 0xFF9E3EFF;
	private List<OrderBean> listAll;
	private ArrayList<OrderBean> listOther = new ArrayList<OrderBean>();

	@OnClick(R.id.tv_myorder_other)
	public void otherButtonClick(View v) { // 未完成
		setColors(tv_myorder_other, tv_myorder_all);
		if (listOther != null && listOther.size() > 0) {
			lv_center_order.setAdapter(adapter01);
		}
	}

	@OnClick(R.id.tv_myorder_all)
	public void allButtonClick(View v) { // 全部
		setColors(tv_myorder_all, tv_myorder_other);
		if (listAll != null && listAll.size() > 0) {
			lv_center_order.setAdapter(adapter);
		}
	}

	/**
	 * 设置标题颜色变化
	 */
	private void setColors(TextView changevView, TextView defaultView) {
		changevView.setBackgroundColor(COLOR);
		changevView.setTextColor(Color.WHITE);
		defaultView.setBackgroundColor(Color.WHITE);
		defaultView.setTextColor(Color.BLACK);
	}

	@OnClick(R.id.tv_back)
	public void backButtonClick(View v) { // 方法签名必须和接口中的要求一致
		onBackPressed();
	}

	/**
	 * 跳转登录界面
	 */
	@OnClick(R.id.tv_other)
	public void otherClick(View v) {
		if (!BaseApplication.getSharedPreferences()
				.getBoolean("isLogin", false)) {
			startActivity(new Intent(this, LoginAcitivity.class));
		} else {
			if (isLogin) {
				BaseApplication.getApplication().showToast("已登录");
				isLogin = false;
			}
		}
	}

	@Override
	public void setView() {
		setContentView(R.layout.activity_myorder);
		ViewUtils.inject(this);// 申明的时候用注解，要在此注入布局视图
	}

	@Override
	public void initView() {
		tv_title.setText("我的订单");
		tv_other.setVisibility(View.VISIBLE);
		tv_other.setText("登录");

	}

	private CenterOrderListViewAdapter1 adapter01;
	private CenterOrderListViewAdapter adapter;
	private boolean isture = true;

	/**
	 * 点击加载数据
	 */
	@SuppressLint("ShowToast")
	@Override
	public void initData() {
		if (BaseApplication.getSharedPreferences().getBoolean("isLogin", false)) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					HttpGet httpGet = new HttpGet(AppConstant.GETPAYINFOS);
					try {
						HttpResponse response = BaseApplication.getHttpClient()
								.execute(httpGet);
						if (response.getStatusLine().getStatusCode() == 200) {
							String result = EntityUtils.toString(
									response.getEntity(), "UTF-8");
							JSONObject object = new JSONObject(result);
							if ("OK".equals(object.getString("message"))) {

								JSONObject object2 = object
										.getJSONObject("data");
								listAll = JSON.parseArray(
										object2.getString("infos"),
										OrderBean.class);
								for (OrderBean list : listAll) {
									String status = list.getStatus();
									if (status.equals("0")) {
										listOther.add(list);
									}
								}
								runOnUiThread(new Runnable() {

									public void run() {
										adapter = new CenterOrderListViewAdapter(
												listAll, MyOrderActivity.this);
										adapter01 = new CenterOrderListViewAdapter1(
												listOther, MyOrderActivity.this);
										lv_center_order.setAdapter(adapter);
									}
								});

							}
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			if (isture) {
				BaseApplication.getApplication().showToast("请登录");
				isture = false;
			}
		}

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

}
