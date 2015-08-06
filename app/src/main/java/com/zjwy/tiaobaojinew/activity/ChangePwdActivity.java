package com.zjwy.tiaobaojinew.activity;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.zjwy.tiaobaojinew.utils.StringUtils;

public class ChangePwdActivity extends BaseActivity {

	@ViewInject(R.id.ed_old_password)
	private EditText ed_old_password;
	@ViewInject(R.id.ed_new_repassword)
	private EditText ed_new_repassword;
	@ViewInject(R.id.tv_back01)
	private TextView tv_back;
	@ViewInject(R.id.tv_title01)
	private TextView tv_title;
	@ViewInject(R.id.btn_ok01)
	private Button btn_ok01;

	@OnClick(R.id.tv_back01)
	public void backButtonClick(View v) { // 点击返回键
		onBackPressed();
	}

	@Override
	public void setView() {
		setContentView(R.layout.activity_changepwd);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		tv_title.setText("修改密码");

	}

	/**
	 * 监听验证码输入是否完成，完成按钮才可以点击
	 */

	@Override
	public void setListener() {
		ed_new_repassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (start > 0) {
					btn_ok01.setEnabled(true);
				} else {
					btn_ok01.setEnabled(false);
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

	/**
	 * 点击确定--开启线程修改密码
	 * 
	 * @param v
	 */
	@OnClick(R.id.btn_ok01)
	public void okButtonClick(View v) {
		final String urlStr = initDatas();
		if (urlStr != null) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					HttpGet httpGet = new HttpGet(AppConstant.EDIT_OLD_PWD
							+ urlStr);
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
										BaseApplication.getApplication()
												.showToast("修改成功");
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

	}

	/**
	 * 初始化数据--获得填写信息
	 * 
	 * @return
	 */
	private String initDatas() {
		String oldpwd = ed_old_password.getText().toString();
		String newpwd = ed_new_repassword.getText().toString();
		if (StringUtils.isEmpty(oldpwd) || StringUtils.isEmpty(newpwd)) {
			BaseApplication.getApplication().showToast("账号或密码不能为空!");
			return null;
		}
		return "&password_old=" + oldpwd + "&password=" + newpwd + "&sign=";
	}

	@Override
	public void initData() {

	}

}
