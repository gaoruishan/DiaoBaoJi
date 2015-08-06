package com.zjwy.tiaobaojinew.activity;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.bean.UserInfo;
import com.zjwy.tiaobaojinew.utils.AppConstant;
import com.zjwy.tiaobaojinew.utils.StringUtils;

public class LoginAcitivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {

	protected static final String TAG = "LoginAcitivity";
	private Button btn_login;
	private EditText login_number, login_password;
	public TextView tv_back, tv_title, tv_login_forget, tv_regist;
	private CheckBox cb_remm, cb_login;
	private String password;

	@Override
	public void setView() {
		setContentView(R.layout.activity_login);
	}

	@Override
	public void initView() {
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		// 设置标题栏
		tv_back.setTextColor(getResources().getColor(R.color.title_text));
		tv_title.setText("登录");
		// 设置各个按钮
		btn_login = (Button) findViewById(R.id.btn_login);
		tv_regist = (TextView) findViewById(R.id.tv_regist);
		tv_login_forget = (TextView) findViewById(R.id.tv_login_forget);
		login_number = (EditText) findViewById(R.id.et_login_number);
		login_password = (EditText) findViewById(R.id.et_login_password);

		cb_remm = (CheckBox) findViewById(R.id.cb_remm);
		cb_login = (CheckBox) findViewById(R.id.cb_login);

	}

	@Override
	public void setListener() {
		tv_back.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		tv_regist.setOnClickListener(this);
		tv_login_forget.setOnClickListener(this);
		cb_remm.setOnCheckedChangeListener(this);
		cb_login.setOnCheckedChangeListener(this);

	}

	@Override
	public void initData() {
		SharedPreferences sp = BaseApplication.getSharedPreferences();
		if (sp.getBoolean("isSave", false)) {
			cb_remm.setChecked(true);
			login_number.setText(sp.getString("mobile", null));
			login_password.setText(sp.getString("password", null));
		}
		if (sp.getBoolean("autoLogin", false)) {
			cb_login.setChecked(true);
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		SharedPreferences sp = BaseApplication.getSharedPreferences();
		Editor editor = sp.edit();
		switch (buttonView.getId()) {
		case R.id.cb_remm:
			if (isChecked) {
				editor.putBoolean("isSave", true);// 保存密码
			} else {
				editor.putBoolean("isSave", false);
			}
			editor.commit();
			break;
		case R.id.cb_login:
			if (isChecked) {
				editor.putBoolean("autoLogin", true);// 保存密码
			} else {
				editor.putBoolean("autoLogin", false);
			}
			editor.commit();
			break;

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			this.finish();
			break;

		case R.id.btn_login:
			// 登陆
			Login();
			// 实例化输入法控制对象，通过hideSoftInputFromWindow来控制，其中第一个参数绑定的为需要隐藏输入法的EditText对象
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(btn_login.getWindowToken(), 0);
			break;

		case R.id.tv_regist:
			startActivity(RegistActivity.class);
			break;

		case R.id.tv_login_forget:
			startActivity(FindPwdActivity.class);
			break;

		}
	}

	/**
	 * get登录
	 */

	private void Login() {
		final String cellphone = login_number.getText().toString().trim();
		password = login_password.getText().toString().trim();

		if (StringUtils.isEmpty(cellphone) || StringUtils.isEmpty(password)) {
			BaseApplication.getApplication().showToast("账号或密码不能为空!");
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpGet httpGet = new HttpGet(AppConstant.LOGIN + "&name="
						+ cellphone + "&password=" + password);
				try {
					HttpResponse response = BaseApplication.getHttpClient()
							.execute(httpGet);
					if (response.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(
								response.getEntity(), "UTF-8");
						JSONObject jsObj = new JSONObject(result);
						String status = jsObj.getString("message");
						// 判断执行相应操作
						if ("登录成功".equals(status)) {
							runOnUiThread(new Runnable() {
								public void run() {
									BaseApplication.getApplication().showToast(
											"登录成功");
								}
							});
							// 保存数据
							UserInfo userInfo = JSON.parseObject(
									jsObj.getString("data"), UserInfo.class);
							SharedPreferences sp = BaseApplication
									.getSharedPreferences();
							Editor editor = sp.edit();
							editor.putBoolean("isLogin", true);// 用于设置是否自动登录---换头像
							editor.putString("password", password);// 添加密码
							editor.putString("id", userInfo.getId());
							editor.putString("mobile", userInfo.getMobile());
							editor.putString("username", userInfo.getName());
							editor.putString("gender", userInfo.getGender());
							editor.putString("nickname", userInfo.getNickname());
							editor.putString("avatar", userInfo.getAvatar());

							editor.putString("email", userInfo.getEmail());
							editor.putString("qq", userInfo.getQq());
							editor.putString("ucserver_id",
									userInfo.getUcserver_id());
							editor.putString("telephone",
									userInfo.getTelephone());
							editor.putString("truename_last",
									userInfo.getTruename_last());
							editor.putString("mobile_last",
									userInfo.getMobile_last());
							editor.putString("truename",
									userInfo.getTruename_last());
							editor.putString("nickname", userInfo.getNickname());
							editor.putString("idcard", userInfo.getIdcard());
							editor.putString("channel_code",
									userInfo.getChannel_code());
							editor.putString("birthday", userInfo.getBirthday());
							editor.putString("postcode", userInfo.getPostcode());
							editor.putString("education",
									userInfo.getEducation());
							editor.putString("graduate", userInfo.getGraduate());
							editor.putString("organization_scal",
									userInfo.getOrganization_scal());
							editor.putString("career", userInfo.getCareer());
							editor.putString("description",
									userInfo.getDescription());
							editor.putString("login_num",
									userInfo.getLogin_num());
							editor.putString("fan_num", userInfo.getFan_num());
							editor.putString("follow_num",
									userInfo.getFollow_num());
							editor.putString("lastlogin_ip",
									userInfo.getLastlogin_ip());
							editor.putString("lastlogin_time",
									userInfo.getLastlogin_time());
							editor.putString("is_verify",
									userInfo.getIs_verify());
							editor.putString("islock", userInfo.getIslock());
							editor.putString("only_bind",
									userInfo.getOnly_bind());
							editor.putString("industry_1_id",
									userInfo.getIndustry_1_id());
							editor.putString("organization",
									userInfo.getOrganization());
							editor.putString("register_ip",
									userInfo.getRegister_ip());
							editor.putString("region_id",
									userInfo.getRegion_id());
							editor.putString("address_last",
									userInfo.getAddress_last());
							editor.putString("register_date",
									userInfo.getRegister_date());

							editor.commit();
							// 保持登录状态
							BaseApplication appCookie = ((BaseApplication) getApplication());
							// 读cookie
							CookieStore cookies = appCookie.getCookie();
							// 设置cookie
							appCookie.setCookie(cookies);
							// 销毁登录界面
							finish();
						} else {
							runOnUiThread(new Runnable() {
								public void run() {
									BaseApplication.getApplication().showToast(
											"账户不存在，请注册！");
								}
							});
						}
					} else {
						runOnUiThread(new Runnable() {
							public void run() {
								BaseApplication.getApplication().showToast(
										"网络出了问题!");
							}
						});
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
	}

	/**
	 * 进行post登陆设置--传值给服务器
	 * 
	 * public void Login() { String cellphone =
	 * login_number.getText().toString().trim(); String password =
	 * login_password.getText().toString().trim();
	 * 
	 * if (StringUtils.isEmpty(cellphone) || StringUtils.isEmpty(password)) {
	 * Toast.makeText(getApplicationContext(), "账号或密码不能为空!",
	 * Toast.LENGTH_SHORT).show(); return; } HttpUtils conn = new
	 * HttpUtils(4000); // url请求地址 RequestParams params = new RequestParams();
	 * // 设置参数 List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	 * parameters.add(new BasicNameValuePair("name", cellphone));
	 * parameters.add(new BasicNameValuePair("password", password));
	 * params.addBodyParameter(parameters); conn.send(HttpMethod.POST,
	 * AppConstant.LOGIN, params, new RequestCallBack<String>() {
	 * 
	 * @Override public void onSuccess(ResponseInfo<String> arg0) { String
	 *           result = arg0.result; String status = "";// 返回json格式的状态码 try {
	 *           JSONObject jsObj = new JSONObject(result); status =
	 *           jsObj.getString("message"); // 判断执行相应操作 if
	 *           ("登录成功".equals(status)) {
	 *           Toast.makeText(getApplicationContext(), "登录成功",
	 *           Toast.LENGTH_SHORT).show(); // 保存数据 UserInfo userInfo =
	 *           JSON.parseObject( jsObj.getString("data"), UserInfo.class);
	 *           SharedPreferences sp = BaseApplication .getSharedPreferences();
	 *           Editor editor = sp.edit(); editor.putBoolean("isLogin",
	 *           true);// 用于设置是否自动登录---换头像 editor.putString("userId",
	 *           userInfo.getId()); editor.putString("userMobile",
	 *           userInfo.getMobile()); editor.putString("userName",
	 *           userInfo.getName()); editor.putString("nickname ",
	 *           userInfo.getNickname()); editor.putString("gender",
	 *           userInfo.getGender()); editor.putString("qq",
	 *           userInfo.getQq()); editor.putString("follow_num ",
	 *           userInfo.getFollow_num()); editor.putString("fan_num ",
	 *           userInfo.getFan_num()); editor.putString("truename",
	 *           userInfo.getTruename_last()); editor.putString("address_last",
	 *           userInfo.getAddress_last());
	 *           editor.putString("userRegister_date",
	 *           userInfo.getRegister_date()); editor.commit(); // 保持登录状态
	 *           BaseApplication appCookie = ((BaseApplication)
	 *           getApplication()); // 读cookie CookieStore cookies =
	 *           appCookie.getCookie(); // 设置cookie
	 *           appCookie.setCookie(cookies); // 销毁登录界面 finish(); } else {
	 *           Toast.makeText(getApplicationContext(), "用户名或密码错误",
	 *           Toast.LENGTH_SHORT).show(); } } catch (JSONException e) {
	 *           e.printStackTrace(); } }
	 * @Override public void onFailure(
	 *           com.lidroid.xutils.exception.HttpException arg0, String arg1) {
	 *           Toast.makeText(getApplicationContext(), "网络出现问题了..正在紧急处理中...",
	 *           Toast.LENGTH_LONG) .show();
	 * 
	 *           } }); }
	 */
}
