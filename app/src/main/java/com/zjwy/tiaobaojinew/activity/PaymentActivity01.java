package com.zjwy.tiaobaojinew.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zjwy.tiaobaojinew.R;

/**
 * 继续支付网页
 * 
 */

public class PaymentActivity01 extends BaseActivity {

	@ViewInject(R.id.pull_refresh_webview)
	private PullToRefreshWebView mPullRefreshWebView;
	private WebView mWebView;
	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.tv_order)
	private TextView tv_order;
	private String payUrl;

	@OnClick(R.id.tv_back)
	public void backButtonClick(View v) {
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
		settings.setSupportZoom(true); // 支持缩放
		settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mWebView.setWebViewClient(new SampleWebViewClient());
		mWebView.loadUrl(payUrl);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	private static class SampleWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

	}

}
