package com.zjwy.tiaobaojinew.activity;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.utils.AppConstant;
import com.zjwy.tiaobaojinew.utils.Md5Utils;
import com.zjwy.tiaobaojinew.utils.StringUtils;

@SuppressLint("DefaultLocale")
public class FindPwdActivity extends BaseActivity {

	@ViewInject(R.id.ed_mobile_num)
	private EditText ed_mobile_num;
	@ViewInject(R.id.ed_new_password)
	private EditText ed_new_password;
	@ViewInject(R.id.et_find_check)
	private EditText et_find_check;
	@ViewInject(R.id.tv_back1)
	private TextView tv_back;
	@ViewInject(R.id.tv_title1)
	private TextView tv_title;
	@ViewInject(R.id.btn_check_find)
	private Button btn_check_find;
	@ViewInject(R.id.btn_ok)
	private Button btn_ok;

	@OnClick(R.id.tv_back1)
	public void backButtonClick(View v) { // 点击返回键
		onBackPressed();
	}

	@Override
	public void setView() {
		setContentView(R.layout.activity_findpwd);
		ViewUtils.inject(this);

	}

	@Override
	public void initView() {
		tv_title.setText("找回密码");
	}

	@Override
	public void setListener() {
		// 监听验证码输入是否完成，完成按钮才可以点击
		et_find_check.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (start > 0) {
					btn_ok.setEnabled(true);
				} else {
					btn_ok.setEnabled(false);
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

	@OnClick(R.id.btn_ok)
	public void okButtonClick(View v) { // 点击确定
		final String urlStr = initDatas();
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpGet httpGet = new HttpGet(AppConstant.EDIT_PWD + urlStr);
				try {
					HttpResponse response = BaseApplication.getHttpClient()
							.execute(httpGet);
					if (response.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(
								response.getEntity(), "UTF-8");
						JSONObject jsObj = new JSONObject(result);
						String status = jsObj.getString("code");
						// 判断执行相应操作
						if ("10000".equals(status)) {
							runOnUiThread(new Runnable() {
								public void run() {
									BaseApplication.getApplication().showToast(
											"修改成功");
									onBackPressed();
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
	 * 获得 拼接的Url 字符串
	 */
	private String initDatas() {
		String mobile = ed_mobile_num.getText().toString();
		String newpwd = ed_new_password.getText().toString();
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(newpwd)) {
			BaseApplication.getApplication().showToast("账号或密码不能为空!");
			return null;
		}
		String md5Str = Md5Utils.encode(mobile + newpwd + "fep_workshop");
		return "&mobile=" + mobile + "&password=" + newpwd + "&sign=" + md5Str;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}
}
