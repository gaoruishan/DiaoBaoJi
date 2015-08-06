package com.zjwy.tiaobaojinew.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.utils.AppConstant;
import com.zjwy.tiaobaojinew.utils.StringUtils;

public class RegistActivity extends BaseActivity implements OnClickListener {

	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = "4933c2c1b49e";

	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "5564e9a326a0646778248693030a9643";
	private MyEventHandler myEventHandlernew;

	private EditText regist_number;
	private Button btn_regist;
	private EditText regist_password;
	private EditText regist_repassword;
	private EditText regist_check;
	private Button check_btn;

	public TextView tv_back;
	public TextView tv_title;
	public TextView tv_right_option;

	@Override
	public void setView() {
		setContentView(R.layout.activity_regist);
		// 初始化短信验证码sdk
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		myEventHandlernew = new MyEventHandler();
		SMSSDK.registerEventHandler(myEventHandlernew);
	}

	@Override
	public void initView() {
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		// 设置标题栏
		tv_back.setTextColor(getResources().getColor(R.color.title_text));
		tv_title.setText("注册");

		// 设置各个按钮
		regist_number = (EditText) findViewById(R.id.et_regist_number);
		regist_password = (EditText) findViewById(R.id.et_regist_password);
		regist_repassword = (EditText) findViewById(R.id.et_regist_repassword);
		regist_check = (EditText) findViewById(R.id.et_regist_check);
		check_btn = (Button) findViewById(R.id.btn_check_btn);
		btn_regist = (Button) findViewById(R.id.btn_regist);
		btn_regist.setEnabled(false);

	}

	@Override
	public void setListener() {
		// 按钮设置监听
		tv_back.setOnClickListener(this);
		btn_regist.setOnClickListener(this);
		check_btn.setOnClickListener(this);
		regist_repassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {// 是否有焦点---即确认输入内容
					if (!regist_password
							.getText()
							.toString()
							.trim()
							.equals(regist_repassword.getText().toString()
									.trim())) {
						BaseApplication.getApplication().showToast("两次密码不一致!");
						return;
					}
				}
			}
		});

		// 监听验证码输入是否完成，完成按钮才可以点击
		regist_check.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (start > 0) {
					btn_regist.setEnabled(true);
				} else {
					btn_regist.setEnabled(false);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			this.finish();
			break;
		case R.id.btn_check_btn:
			checkSecurityCode();
			break;
		case R.id.btn_regist:
			Regist();
			break;

		}
	}

	/**
	 * 短信验证码获取
	 */
	private void checkSecurityCode() {
		if (StringUtils.isEmpty(regist_number.getText().toString().trim())) {
			BaseApplication.getApplication().showToast("手机号不能为空!");
			return;
		}
		// 发送请求
		SMSSDK.getVerificationCode("86", regist_number.getText().toString()
				.trim());
		check_btn.setEnabled(false);
	}

	/**
	 * 短信验证的回调监听
	 * 
	 * @author Mr_q
	 * 
	 */
	public class MyEventHandler extends EventHandler {

		@Override
		public void afterEvent(int event, int result, Object data) {
			super.afterEvent(event, result, data);
			// 解析注册结果
			if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
				LogUtils.i("验证码校验成功");
			}

		}

		@Override
		public void beforeEvent(int arg0, Object arg1) {
			super.beforeEvent(arg0, arg1);
		}

		@Override
		public void onRegister() {
			super.onRegister();
		}

		@Override
		public void onUnregister() {
			super.onUnregister();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterEventHandler(myEventHandlernew);
	}

	/**
	 * 注册用户--使用HttpUtils的get方法注册
	 */
	private void Regist() {

		String cellphone = regist_number.getText().toString().trim();
		String password = regist_password.getText().toString().trim();

		if (StringUtils.isEmpty(cellphone) || StringUtils.isEmpty(password)) {
			BaseApplication.getApplication().showToast("账号或密码不能为空!");
			return;
		}
		HttpUtils conn = new HttpUtils(4000);
		// url请求地址
		conn.send(HttpMethod.GET, AppConstant.REGISTER + "&mobile=" + cellphone
				+ "&password=" + password, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				BaseApplication.getApplication().showToast("网络出了问题!");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				LogUtils.i(result);
				try {
					JSONObject jsObj = new JSONObject(result);
					String message = jsObj.getString("message");
					if (message.equals("注册成功")) {
						BaseApplication.getApplication().showToast("注册成功");
					}
					onBackPressed();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	/**
	 * 注册用户--post方法
	 * 
	 * @SuppressWarnings("unused") private void Regist() {
	 * 
	 *                             String cellphone =
	 *                             regist_number.getText().toString().trim();
	 *                             String password =
	 *                             regist_password.getText().toString().trim();
	 * 
	 *                             if (StringUtils.isEmpty(cellphone) ||
	 *                             StringUtils.isEmpty(password)) {
	 *                             Toast.makeText(getApplicationContext(),
	 *                             "账号或密码不能为空!", Toast.LENGTH_SHORT).show();
	 *                             return; }
	 * 
	 *                             HttpUtils conn = new HttpUtils(4000); //
	 *                             url请求地址 String url =
	 *                             "http://passport.tiaobaoji.com/client?isTest=no&action=register"
	 *                             ; RequestParams params = new RequestParams();
	 *                             // 设置参数 List<NameValuePair> parameters = new
	 *                             ArrayList<NameValuePair>();
	 *                             parameters.add(new
	 *                             BasicNameValuePair("mobile", cellphone));
	 *                             parameters.add(new
	 *                             BasicNameValuePair("password", password));
	 * 
	 *                             params.addBodyParameter(parameters);
	 * 
	 *                             conn.send(HttpMethod.POST, url, params, new
	 *                             RequestCallBack<String>() {
	 * @Override public void onFailure(HttpException arg0, String arg1) {
	 *           Toast.makeText(getApplicationContext(), "网络出现问题了..正在紧急处理中...",
	 *           Toast.LENGTH_LONG).show(); }
	 * @Override public void onSuccess(ResponseInfo<String> arg0) {
	 *           Toast.makeText(getApplicationContext(), "请求网络成功",
	 *           Toast.LENGTH_LONG).show(); String result = arg0.result;
	 *           LogUtils.i(result); try { JSONObject jsObj = new
	 *           JSONObject(result); String code = jsObj.getString("code");
	 *           String message = jsObj.getString("message"); if
	 *           (code.equals(10026)) {
	 * 
	 *           }
	 * 
	 *           Toast.makeText(getApplicationContext(), message,
	 *           Toast.LENGTH_LONG).show();
	 * 
	 *           } catch (JSONException e) { e.printStackTrace(); } }
	 * 
	 *           }); }
	 */
}
