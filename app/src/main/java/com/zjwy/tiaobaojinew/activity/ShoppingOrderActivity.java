package com.zjwy.tiaobaojinew.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.zjwy.tiaobaojinew.adapter.ShoppingOrderAdapter;
import com.zjwy.tiaobaojinew.bean.CouponBean;
import com.zjwy.tiaobaojinew.bean.ProductContentBean;
import com.zjwy.tiaobaojinew.utils.AppConstant;
import com.zjwy.tiaobaojinew.widget.ListViewForScrollView;

public class ShoppingOrderActivity extends BaseActivity {
	@ViewInject(R.id.total)
	private TextView total;
	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.tv_other)
	private TextView tv_other;
	@ViewInject(R.id.tv_coupon01)
	private TextView tv_coupon01;
	@ViewInject(R.id.order_name)
	private EditText order_name;
	@ViewInject(R.id.order_moblie)
	private EditText order_moblie;
	@ViewInject(R.id.order_city)
	private EditText order_city;
	@ViewInject(R.id.order_country)
	private EditText order_country;
	@ViewInject(R.id.order_address)
	private EditText order_address;
	@ViewInject(R.id.commit01)
	private Button commit01;
	@ViewInject(R.id.order_jia01)
	private ImageView order_jia01;
	@ViewInject(R.id.icon_xing01)
	private ImageView icon_xing01;
	@ViewInject(R.id.ll_coupon01)
	private LinearLayout ll_coupon01;
	@ViewInject(R.id.order_notes01)
	private EditText order_notes01;
	@ViewInject(R.id.cb_use01)
	private CheckBox cb_use01;
	@ViewInject(R.id.sp_pays)
	private Spinner sp_pays;

	private String name;
	private String moblie;
	private String address;
	protected String pay = "aliwap";
	@ViewInject(R.id.lv_shopping_order)
	private ListViewForScrollView lv_shopping_order;
	@ViewInject(R.id.icon_xing01)
	private ImageView icon_xing;
	@ViewInject(R.id.order_notes01)
	private EditText order_notes;
	private boolean isFrist = true;
	private boolean isFrist01 = true;
	private String money;
	private long diff;
	private String strUrl;
	private List<ProductContentBean> beans;
	private ShoppingOrderAdapter adapter;

	@OnClick(R.id.cb_use)
	public void cbuseClick(View v) { // 使用优惠券

	}

	@OnClick(R.id.order_jia01)
	public void orderjiaClick(View v) { // 使用优惠券
		if (isFrist) {
			ll_coupon01.setVisibility(View.VISIBLE);
			tv_coupon01.setText("立省" + money + "元！");
			if (diff > 0) {
				tv_coupon01.setTextColor(Color.GRAY);
				cb_use01.setEnabled(false);
			}
			isFrist = false;
		} else {
			ll_coupon01.setVisibility(View.GONE);
			isFrist = true;
		}
	}

	@OnClick(R.id.icon_xing01)
	public void iconxingClick(View v) { // 备注
		if (isFrist01) {
			order_notes01.setVisibility(View.VISIBLE);
			isFrist01 = false;
		} else {
			order_notes01.setVisibility(View.GONE);
			isFrist01 = true;
		}
	}

	@OnClick(R.id.tv_back)
	public void backButtonClick(View v) { // 返回
		onBackPressed();
	}

	@OnClick(R.id.tv_other)
	public void otherClick(View v) { // 方法签名必须和接口中的要求一致
		activity.startActivity(new Intent(activity, LoginAcitivity.class));
	}

	@Override
	public void setView() {
		setContentView(R.layout.fragment_shopping_order);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		tv_title.setText("订单");
		tv_other.setVisibility(View.VISIBLE);
		tv_other.setText("登录");
		beans = getIntent().getParcelableArrayListExtra("beans");
		// 建立数据源
		String[] mItems = getResources().getStringArray(R.array.spinnername);
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(
				ShoppingOrderActivity.this,
				android.R.layout.simple_spinner_item, mItems);
		// 绑定 Adapter到控件
		sp_pays.setAdapter(_Adapter);

		if (beans.size() > 0) {
			adapter = new ShoppingOrderAdapter(beans, activity, total);
			lv_shopping_order.setAdapter(adapter);
			// prl.setSelection(beans.size());//置底
		}
	}

	@Override
	public void setListener() {
		// 点击确定提交
		findViewById(R.id.commit01).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击事件 前获取数据!
				getDatas();

				if (adapter != null) {
					String urlString = adapter.getUrlString();// 获取适配器的url
					// System.out.println(urlString);
					if (BaseApplication.getSharedPreferences().getBoolean(
							"isLogin", false)) {
						if (!name.equals("") && !moblie.equals("")
								&& !address.equals("") && strUrl != null) {
							if (pay.equals("offline")) {
								BaseApplication.getApplication().showToast(
										"你的订单已提交，立刻为你发货！");
							} else {
								System.out.println("---" + strUrl + "&mobile="
										+ moblie + urlString + "&truename="
										+ name + "&address=" + address
										+ "&payment_code=" + pay);
								startActivity(new Intent(activity,
										PaymentActivity.class).putExtra(
										"payUrl", strUrl + "&mobile=" + moblie
												+ urlString + "&truename="
												+ name + "&address=" + address
												+ "&payment_code=" + pay));
							}

						} else {
							BaseApplication.getApplication().showToast(
									"请填写完整信息");
						}
					} else {
						BaseApplication.getApplication().showToast("请登录");
					}
				}
			}
		});

	}

	@Override
	public void initData() {
		// 判断获取优惠券
		SharedPreferences sp = BaseApplication.getSharedPreferences();
		if (sp.getBoolean("isLogin", false)) {
			String userId = sp.getString("id", null);
			getCoupon(userId);
		}

	}

	/**
	 * 获取用户输入数据
	 */
	private void getDatas() {
		name = order_name.getText().toString().trim();
		moblie = order_moblie.getText().toString().trim();
		address = order_city.getText().toString().trim()
				+ order_country.getText().toString().trim()
				+ order_address.getText().toString().trim();
		sp_pays.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String str = parent.getItemAtPosition(position).toString();
				switch (str) {
				case "支付宝wap":
					pay = "aliwap";
					break;
				case "货到付款":
					pay = "offline";
					break;
				case "银联支付":
					pay = "yeepay";
					break;
				case "支付宝":
					pay = "alipay";
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	/**
	 * 获取优惠券
	 */
	private void getCoupon(String userId) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, AppConstant.GETCOUPON + userId,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						BaseApplication.getApplication().showToast("网络出了问题！");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							// System.out.println(arg0.result);
							JSONObject object = new JSONObject(arg0.result);
							if (object.getString("message").equals("OK")) {
								JSONObject object2 = object
										.getJSONObject("data");
								JSONArray jsonArray = object2
										.getJSONArray("infos");
								for (int i = 0; i < jsonArray.length(); i++) {
									CouponBean bean = JSON.parseObject(
											jsonArray.getString(i),
											CouponBean.class);
									// coupon = bean.getCoupon();
									money = bean.getMoney().split("\\.")[0];
									strUrl = "&activity_id="
											+ bean.getActivity_id()
											+ "&coupon_id=" + bean.getId();
									int parseIntend = Integer.parseInt(bean
											.getEnd_time());
									diff = System.currentTimeMillis()
											- (long) parseIntend * 1000;

								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

}
