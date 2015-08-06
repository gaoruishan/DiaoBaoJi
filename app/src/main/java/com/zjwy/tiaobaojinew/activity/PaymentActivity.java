package com.zjwy.tiaobaojinew.activity;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.utils.AppConstant;

/**
 * webView 打开一个订单支付网页
 * 
 */

public class PaymentActivity extends BaseActivity {

	@ViewInject(R.id.pull_refresh_webview)
	private PullToRefreshWebView mPullRefreshWebView;
	private WebView mWebView;
	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	private String string;
	@ViewInject(R.id.tv_order)
	private TextView tv_order;
	private String payUrl;

	@OnClick(R.id.tv_back)
	public void backButtonClick(View v) { // 方法签名必须和接口中的要求一致
		onBackPressed();
	}

	@Override
	public void setView() {
		setContentView(R.layout.activity_payment);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		tv_title.setText("支付");
		payUrl = getIntent().getStringExtra("payUrl");
		mWebView = mPullRefreshWebView.getRefreshableView();
	}

	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public void setListener() {
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);// 设置支持javascript脚本
		settings.setAllowFileAccess(true); // 允许访问文件
		// 设置加载进来的页面自适应手机屏幕(设置webview加载的页面的模式)
		// settings.setUseWideViewPort(true);
		// settings.setLoadWithOverviewMode(true);
		settings.setSupportZoom(true); // 支持缩放
		settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
		// 清除浏览器缓存
		// mWebView.clearCache(true);
		// 设置网页布局类型：
		// 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
		// 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放(设置webview推荐使用的窗口)
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mWebView.setWebViewClient(new SampleWebViewClient());

	}

	@Override
	public void initData() {
		// 获取唯一序列号
		getserial();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (string != null) {
					mWebView.loadUrl(AppConstant.ORDERPAY + payUrl + "&serial="
							+ string);// 开启加载网页
					System.out.println("---url:" + AppConstant.ORDERPAY
							+ payUrl + "&serial=" + string);
				}
			} else {
				mPullRefreshWebView.setVisibility(View.GONE);
				tv_order.setVisibility(View.VISIBLE);
				Toast.makeText(PaymentActivity.this, "商品在使用中或已预订!",
						Toast.LENGTH_SHORT).show();
			}
		};
	};

	/**
	 * 获取序列号
	 */
	private void getserial() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpPost httpGet = new HttpPost(AppConstant.GETSERIAL);
				try {
					HttpResponse response = BaseApplication.getHttpClient()
							.execute(httpGet);
					if (response.getStatusLine().getStatusCode() == 200) {
						String str = EntityUtils.toString(response.getEntity(),
								"UTF-8");
						JSONObject object = new JSONObject(str);
						if (object.getString("code").equals("10000")) {
							JSONObject object2 = object.getJSONObject("data");
							string = object2.getString("serial");
							handler.sendEmptyMessage(1);
						} else {
							handler.sendEmptyMessage(0);
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
	}

	private static class SampleWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

	}
}
