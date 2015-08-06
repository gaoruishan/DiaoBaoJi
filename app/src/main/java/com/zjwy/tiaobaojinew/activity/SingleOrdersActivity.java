package com.zjwy.tiaobaojinew.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
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
import com.zjwy.tiaobaojinew.bean.CouponBean;
import com.zjwy.tiaobaojinew.bean.ProductContentBean;
import com.zjwy.tiaobaojinew.utils.AppConstant;

public class SingleOrdersActivity extends BaseActivity {
	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.tv_other)
	private TextView tv_other;
	@ViewInject(R.id.tv_order_styles)
	private TextView tv_order_styles;
	@ViewInject(R.id.tv_order_brands)
	private TextView tv_order_brands;
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
	@ViewInject(R.id.radio0)
	private RadioButton radio0;
	@ViewInject(R.id.radio1)
	private RadioButton radio1;
	private String rentDate;
	private int rentPrice;
	private String name;
	private String moblie;
	private String address;
	@ViewInject(R.id.order_jia)
	private ImageView order_jia;
	@ViewInject(R.id.icon_xing)
	private ImageView icon_xing;
	@ViewInject(R.id.list_iv_ordersingle)
	private ImageView list_iv_ordersingle;
	@ViewInject(R.id.tv_coupon)
	private TextView tv_coupon;
	@ViewInject(R.id.order_notes)
	private EditText order_notes;
	@ViewInject(R.id.cb_use)
	private CheckBox cb_use;
	@ViewInject(R.id.sp_pay)
	private Spinner sp_pay;
	@ViewInject(R.id.ll_coupon)
	private LinearLayout ll_coupon;
	private boolean isLogin = true;

	private boolean isFrist02 = true;
	private String strUrl;
	private SharedPreferences sp;
	private ProductContentBean beans;
	protected String pay = "aliwap";

	@Override
	public void setView() {
		setContentView(R.layout.activity_orders);
		ViewUtils.inject(this);// 申明的时候用注解，要在此注入布局视图

	}

	/**
	 * 初始化界面,订单信息
	 */
	@Override
	public void initView() {
		tv_title.setText("订单");
		tv_other.setVisibility(View.VISIBLE);
		tv_other.setText("登录");
		beans = getIntent().getParcelableExtra("bean");
		tv_order_styles.setText("款式：" + beans.getSearch_field().split(" ")[0]);
		tv_order_brands.setText("品牌：" + beans.getBrand_name()
				+ beans.getBrand_name_chinese());
		new BitmapUtils(this).display(list_iv_ordersingle, beans.getThumb());
		// 建立数据源
		String[] mItems = getResources().getStringArray(R.array.spinnername);
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(
				SingleOrdersActivity.this,
				android.R.layout.simple_spinner_item, mItems);
		// 绑定 Adapter到控件
		sp_pay.setAdapter(_Adapter);
		radio0.setText(beans.getPrice_rent_7().split("\\.")[0] + "元/7天");
		radio1.setText(beans.getPrice_rent_10().split("\\.")[0] + "元/10天");

	}

	@Override
	public void setListener() {
		// 点击确定提交
		findViewById(R.id.commit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击事件 前获取数据!
				getDatas();

				// 判断不能为null
				if (sp != null && sp.getBoolean("isLogin", false)) {
					if (!moblie.equals("") && !address.equals("")
							&& !rentDate.equals("") && rentPrice != 0
							&& !name.equals("") && strUrl != null) {
						if (pay.equals("offline")) {
							BaseApplication.getApplication().showToast(
									"你的订单已提交，立刻为你发货！");
						} else {
							// 开启跳转支付界面
							System.out.println("---" + strUrl + "&mobile="
									+ moblie + "&good_id[" + beans.getId()
									+ "]=" + rentDate + "&money=" + rentPrice
									+ "&truename=" + name + "&address="
									+ address + "&payment_code=" + pay);
							startActivity(new Intent(SingleOrdersActivity.this,
									PaymentActivity.class).putExtra("payUrl",
									strUrl + "&mobile=" + moblie + "&good_id["
											+ beans.getId() + "]=" + rentDate
											+ "&money=" + rentPrice
											+ "&truename=" + name + "&address="
											+ address + "&payment_code=" + pay));
						}
					} else {
						BaseApplication.getApplication().showToast("请填写完整信息");
					}
				} else {
					BaseApplication.getApplication().showToast("请登录");
				}
				// 保存数据库
				// try {
				// BaseApplication.getDbUtils().saveOrUpdate(beans);// 保存到数据库
				// } catch (DbException e) {
				// e.printStackTrace();
				// }
			}

		});

	}

	@Override
	public void initData() {
		// 获取优惠券
		sp = BaseApplication.getSharedPreferences();
		if (sp.getBoolean("isLogin", false)) {
			String userId = sp.getString("id", null);
			getCoupon(userId);
		}

	}

	@OnClick(R.id.cb_use)
	public void cbuseClick(View v) { // 使用优惠券
		rentPrice = (rentPrice - Integer.parseInt(money));
	}

	@OnClick(R.id.tv_back)
	public void backButtonClick(View v) { // 返回
		onBackPressed();
	}

	@OnClick(R.id.tv_other)
	public void otherClick(View v) { // 登录
		if (!sp.getBoolean("isLogin", false)) {
			startActivity(new Intent(SingleOrdersActivity.this,
					LoginAcitivity.class));
		} else {
			if (isLogin) {
				BaseApplication.getApplication().showToast("已登录");
				isLogin = false;
			}
		}
	}

	@OnClick(R.id.radio0)
	public void radio0(View v) {
		rentPrice = Integer.parseInt(beans.getPrice_rent_7().split("\\.")[0]);
		rentDate = "7";
	}

	@OnClick(R.id.radio1)
	public void radio1(View v) {
		rentPrice = Integer.parseInt(beans.getPrice_rent_10().split("\\.")[0]);
		rentDate = "10";
	}

	private boolean isFrist = true;

	/**
	 * 备注
	 */
	@OnClick(R.id.icon_xing)
	public void xingNotes(View v) {
		Animation scale2 = AnimationUtils.loadAnimation(this, R.anim.scale);
		icon_xing.setAnimation(scale2);
		if (isFrist02) {
			order_notes.setVisibility(View.VISIBLE);
			isFrist02 = false;
		} else {
			order_notes.setVisibility(View.GONE);
			isFrist02 = true;
		}
	}

	/**
	 * 点击了优惠券
	 */
	@OnClick(R.id.order_jia)
	public void radioJia(View v) {
		Animation scale1 = AnimationUtils.loadAnimation(
				SingleOrdersActivity.this, R.anim.rotate);
		order_jia.setAnimation(scale1);
		if (isFrist && (sp.getBoolean("isLogin", false))) {
			if (money != null) {
				ll_coupon.setVisibility(View.VISIBLE);
				tv_coupon.setText("立省：" + money + "/元");
				isFrist = false;
			} else {
				BaseApplication.getApplication().showToast("没有优惠券！");
			}
		} else if (!sp.getBoolean("isLogin", false)) {
			BaseApplication.getApplication().showToast("请登录");
		} else {
			ll_coupon.setVisibility(View.GONE);
			isFrist = true;
		}
	}

	/**
	 * 获取优惠券
	 * 
	 * @param userId
	 */
	private String money;

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
									money = bean.getMoney().split("\\.")[0];
									strUrl = "&activity_id="
											+ bean.getActivity_id()
											+ "&coupon_id=" + bean.getId();
									int parseIntend = Integer.parseInt(bean
											.getEnd_time());
									long diff = System.currentTimeMillis()
											- (long) parseIntend * 1000;
									if (diff > 0) {
										cb_use.setEnabled(false);
										tv_coupon.setTextColor(Color.GRAY);
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 初始化数据--地址信息和支付类型
	 */
	private void getDatas() {
		name = order_name.getText().toString().trim();
		moblie = order_moblie.getText().toString().trim();
		address = order_city.getText().toString().trim()
				+ order_country.getText().toString().trim()
				+ order_address.getText().toString().trim();
		// 支付类型 --选择监听
		sp_pay.setOnItemSelectedListener(new OnItemSelectedListener() {
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

}
