package com.zjwy.tiaobaojinew.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
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
import com.zjwy.tiaobaojinew.adapter.SearchAdapter;
import com.zjwy.tiaobaojinew.bean.HomeBestNewsBean;
import com.zjwy.tiaobaojinew.bean.HomeBestNewsBean.InfoBean;
import com.zjwy.tiaobaojinew.utils.AppConstant;

public class SearchActivity extends BaseActivity {

	@ViewInject(R.id.search_grid)
	private GridView search_grid;
	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;

	@Override
	public void setView() {
		setContentView(R.layout.activity_search);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		keyword = getIntent().getStringExtra("keyword");
		tv_title.setText("搜索内容");

	}

	@Override
	public void setListener() {
		search_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (infos != null && infos.size() > 0) {
					InfoBean bean = infos.get(position);
					startActivity(new Intent(SearchActivity.this,
							ProductDetailsActivity.class).putExtra("url",
							AppConstant.HOME_HEAD + bean.getId()));
				}
			}
		});
	}

	private SearchAdapter adapter;
	private List<InfoBean> infos;
	private String keyword;

	@OnClick(R.id.tv_back)
	public void backButtonClick(View v) { // 方法签名必须和接口中的要求一致
		onBackPressed();
	}

	@Override
	public void initData() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, AppConstant.SEARCH + keyword,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						BaseApplication.getApplication().showToast("网络出了问题!");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String result = arg0.result;
						try {
							JSONObject object = new JSONObject(result);
							String string = object.getString("message");
							if (string.equals("OK")) {
								JSONObject object1 = new JSONObject(object
										.getString("data"));
								HomeBestNewsBean bean = JSON.parseObject(
										object1.getString("infos"),
										HomeBestNewsBean.class);
								infos = bean.getInfos();
								adapter = new SearchAdapter(infos,
										SearchActivity.this);
								search_grid.setAdapter(adapter);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

}
