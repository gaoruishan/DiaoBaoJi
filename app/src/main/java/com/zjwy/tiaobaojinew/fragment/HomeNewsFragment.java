package com.zjwy.tiaobaojinew.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.adapter.HomeNewsListViewAdapter;
import com.zjwy.tiaobaojinew.bean.HomeBestNewsBean;
import com.zjwy.tiaobaojinew.bean.HomeBestNewsBean.InfoBean;

public class HomeNewsFragment extends BaseFragment {

	private String url;
	@ViewInject(R.id.news_lv)
	private PullToRefreshListView prl;
	private List<InfoBean> list = new ArrayList<InfoBean>();
	private HttpUtils httpUtils = new HttpUtils();
	@ViewInject(R.id.pb_home_news)
	private ProgressBar pb_home_news;
	@ViewInject(R.id.tv_refresh_news)
	private TextView tv_refresh_news;
	@ViewInject(R.id.tv_load02)
	private TextView tv_load02;

	public HomeNewsFragment(String url) {
		super();
		this.url = url;
	}

	/**
	 * 提供一个静态的方法传值，返回实例
	 * 
	 */
	public static HomeNewsFragment getInstance(String url) {
		HomeNewsFragment fragment = new HomeNewsFragment();
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		fragment.setArguments(bundle);
		return fragment;
	}

	public HomeNewsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		url = getArguments().getString("url");// 创建时获得url
	}

	/**
	 * 视图创建时fragment，将布局添加到viewpager中
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home_news, container,
				false);
		ViewUtils.inject(this, view);
		initView(view, inflater);
		initDatas();
		return view;
	}

	/**
	 * 加载网络数据--进行适配
	 */
	private void initDatas() {
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			private HomeNewsListViewAdapter homeBestListViewAdapter;

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						pb_home_news.setVisibility(View.GONE);
						tv_load02.setVisibility(View.GONE);
						tv_refresh_news.setVisibility(View.VISIBLE);
					}
				}, 3000);
				tv_refresh_news.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						initDatas();// 重新加载数据
						pb_home_news.setVisibility(View.VISIBLE);
						tv_load02.setVisibility(View.VISIBLE);
						tv_refresh_news.setVisibility(View.GONE);
					}
				});
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				JSONObject object;
				try {
					object = new JSONObject(result);
					if (object.getString("message").equals("OK")) {
						HomeBestNewsBean bean = JSON.parseObject(
								object.getString("data"),
								HomeBestNewsBean.class);
						list = bean.getInfos();
						homeBestListViewAdapter = new HomeNewsListViewAdapter(
								list, activity, pb_home_news, tv_load02);
						prl.setAdapter(homeBestListViewAdapter);
						tv_refresh_news.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 初始化控件
	 * 
	 * @param view
	 * @param inflater
	 */
	private void initView(View view, LayoutInflater inflater) {
		prl.setMode(Mode.BOTH);// 设置 刷新模式
		// 上拉刷新
		ILoadingLayout ill = prl.getLoadingLayoutProxy(true, false);
		ill.setPullLabel("~(^ˍ^)~ 嘻嘻");
		ill.setReleaseLabel("亲，松开你的手哦！");
		ill.setRefreshingLabel("拼命刷新。。。。");
		ill.setLoadingDrawable(getResources().getDrawable(// 刷新图标
				R.drawable.progress_rotate));
		String data2 = new SimpleDateFormat("dd日 HH时mm分ss秒").format(new Date());// 设置刷新时间
		ill.setLastUpdatedLabel(data2);
		// 下拉加载
		ILoadingLayout ilp = prl.getLoadingLayoutProxy(false, true);
		ilp.setPullLabel("已滑动到底部！");
		ilp.setRefreshingLabel("已滑动到底部！");
		ilp.setReleaseLabel("已滑动到底部！");
		ilp.setLoadingDrawable(null);
		prl.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {

				initDatas();// 加载数据
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						prl.onRefreshComplete();// 推迟500完成刷新---一定要提交
					}
				}, 2000);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						prl.onRefreshComplete();//
					}
				}, 500);
			}
		});
	}
}
