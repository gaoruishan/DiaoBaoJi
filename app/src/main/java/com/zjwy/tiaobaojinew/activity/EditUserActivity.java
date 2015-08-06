package com.zjwy.tiaobaojinew.activity;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.utils.AppConstant;
import com.zjwy.tiaobaojinew.utils.StringUtils;

public class EditUserActivity extends BaseActivity {

	@ViewInject(R.id.ed_nickname)
	private EditText ed_nickname;
	@ViewInject(R.id.tv_back_edit)
	private TextView tv_back_edit;
	@ViewInject(R.id.tv_title_edit)
	private TextView tv_title_edit;
	@ViewInject(R.id.btn_ok02)
	private Button btn_ok02;
	@ViewInject(R.id.rg_sex)
	private RadioGroup rg_sex;

	@OnClick(R.id.tv_back_edit)
	public void backButtonClick(View v) { // 点击返回键
		onBackPressed();
	}

	@Override
	public void setView() {
		setContentView(R.layout.activity_edituser);
		ViewUtils.inject(this);

	}

	@Override
	public void initView() {
		tv_title_edit.setText("修改用户信息");

	}

	/**
	 * 监听验证码输入是否完成，完成按钮才可以点击
	 */
	@Override
	public void setListener() {
		ed_nickname.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (start > 0) {
					btn_ok02.setEnabled(true);
				} else {
					btn_ok02.setEnabled(false);
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

		rg_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (group.getCheckedRadioButtonId()) {
				case R.id.rd_nan:
					sex = "1";
					break;
				case R.id.rd_nv:
					sex = "2";
					break;
				case R.id.rd_null:
					sex = "0";
					break;
				}
			}
		});
	}

	@OnClick(R.id.btn_ok02)
	public void okButtonClick(View v) { // 点击确定
		final String urlStr = initDatas();
		if (urlStr != null) {
			Editor editor = BaseApplication.getSharedPreferences().edit();
			editor.putString("nickname", nickname);// 保存
			editor.putString("gender", sex);// 保存
			editor.commit();
			new Thread(new Runnable() {

				@Override
				public void run() {
					HttpGet httpGet = new HttpGet(AppConstant.USER_EDIT
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
							if ("10020".equals(status)) {
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

	private String sex = "1";
	private String nickname;

	private String initDatas() {
		nickname = ed_nickname.getText().toString();

		if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(sex)) {
			BaseApplication.getApplication().showToast("昵称或性别不能为空!");
			return null;
		}
		return "&nickname=" + nickname + "&gender=" + sex;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
