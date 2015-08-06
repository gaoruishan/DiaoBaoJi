package com.zjwy.tiaobaojinew.fragment;

import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.activity.QRCodeActivity;
import com.zjwy.tiaobaojinew.activity.SearchActivity;
import com.zjwy.tiaobaojinew.adapter.HomePagerAdapter;
import com.zjwy.tiaobaojinew.adapter.SearchAutoCompAdapter;
import com.zjwy.tiaobaojinew.bean.SearchAutoDataBean;
import com.zjwy.tiaobaojinew.utils.AppConstant;
import com.zjwy.tiaobaojinew.utils.StringUtils;

@SuppressLint("ClickableViewAccessibility")
public class HomesFragment extends BaseFragment implements
		OnPageChangeListener, OnClickListener {

	public static final String SEARCH_HISTORY = "search_history";
	public static final int COLOR = 0xFF9A35FF;
	private ViewPager viewPager;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private View view;
	private TextView activitys, recommended, newStyle, tv_search, tv_qrcode;
	private TextView editText;
	private ListView mAutoListView;
	private SearchAutoCompAdapter mSearchAutoAdapter;
	private final static int SCANNIN_GREQUEST_CODE = 1;

	/**
	 * 提供一个静态的方法传值，返回实例
	 * 
	 */
	public static HomesFragment getInstance() {
		return new HomesFragment();
	}

	/**
	 * 视图创建时fragment，将布局添加到viewpager中
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout_home, container, false);
		viewPager = (ViewPager) view.findViewById(R.id.home_viewpager);
		initView();// 初始化视图
		setListener();// 设置监听
		return view;
	}

	/**
	 * 设置监听事件
	 */
	private void setListener() {
		activitys = (TextView) view.findViewById(R.id.activity);
		activitys.setOnClickListener(this);
		recommended = (TextView) view.findViewById(R.id.recommended);
		recommended.setOnClickListener(this);
		newStyle = (TextView) view.findViewById(R.id.newStyle);
		newStyle.setOnClickListener(this);
		tv_search = (TextView) view.findViewById(R.id.tv_search);
		tv_search.setOnClickListener(this);
		tv_qrcode = (TextView) view.findViewById(R.id.tv_qrcode);
		tv_qrcode.setOnClickListener(this);
		mAutoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				SearchAutoDataBean data = (SearchAutoDataBean) mSearchAutoAdapter
						.getItem(position);
				editText.setText(data.getContent());
				tv_search.performClick();
			}
		});
		/**
		 * 点击事件和文本内容变化监听
		 */
		tv_search.setOnClickListener(this);
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mSearchAutoAdapter = new SearchAutoCompAdapter(activity, -1,
						HomesFragment.this);
				mAutoListView.setAdapter(mSearchAutoAdapter);
				mSearchAutoAdapter.performFiltering(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		// Android EditText 设置键盘 搜索，回车
		editText.setImeOptions(EditorInfo.IME_ACTION_SEND);
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND
						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					String input = editText.getText().toString().trim();
					if (!StringUtils.isEmpty(input)) {
						startActivity(new Intent(activity, SearchActivity.class)
								.putExtra("keyword", input));
					} else {
						BaseApplication.getApplication().showToast("请输入搜索内容！");
					}
					return true;
				}
				return false;
			}
		});
		editText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAutoListView.setAdapter(new SearchAutoCompAdapter());
			}
		});
		// 点击屏幕空白处隐藏软键盘
		// 实例化输入法控制对象，通过hideSoftInputFromWindow来控制，其中第一个参数绑定的为需要隐藏输入法的EditText对象，比
		RelativeLayout activity_main = (RelativeLayout) view
				.findViewById(R.id.activity_home);
		activity_main.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				InputMethodManager imm = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(activity.getCurrentFocus()
						.getWindowToken(), 0);
			}
		});
	}

	/**
	 * 初始化控件
	 * 
	 * @param view
	 * @param inflater
	 */
	private void initView() {
		editText = (TextView) view.findViewById(R.id.et_search);
		tv_search = (TextView) view.findViewById(R.id.tv_search);
		mAutoListView = (ListView) view.findViewById(R.id.auto_listviews);
		fragments.add(HomeActivityFragment
				.getInstance(AppConstant.HOME_ACTIVITY));
		fragments.add(HomeBestFragment
				.getInstance(AppConstant.HOME_BEST_RECOMMEND));
		fragments.add(HomeNewsFragment.getInstance(AppConstant.HOME_NEWS_POP));
		viewPager.setAdapter(new HomePagerAdapter(fragments,
				getFragmentManager()));
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	/**
	 * 当viewpager改变时，对应的标题的切换
	 */
	@Override
	public void onPageSelected(int position) {
		defaultView();
		switch (position) {
		case 0:
			activitys.setTextColor(Color.RED);
			break;
		case 1:
			recommended.setTextColor(Color.RED);
			break;
		case 2:
			newStyle.setTextColor(Color.RED);
			break;

		default:
			break;
		}
	}

	/**
	 * @Title: defaultView
	 * @说 明:设置的标题默认属性
	 * @参 数:
	 * @return void 返回类型
	 * @throws
	 */
	private void defaultView() {
		activitys = (TextView) view.findViewById(R.id.activity);
		recommended = (TextView) view.findViewById(R.id.recommended);
		newStyle = (TextView) view.findViewById(R.id.newStyle);
		activitys.setTextColor(COLOR);
		recommended.setTextColor(COLOR);
		newStyle.setTextColor(COLOR);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.activity:
			viewPager.setCurrentItem(0);
			activitys.setTextColor(Color.RED);
			break;
		case R.id.recommended:
			viewPager.setCurrentItem(1);
			recommended.setTextColor(Color.RED);
			break;
		case R.id.newStyle:
			viewPager.setCurrentItem(2);
			newStyle.setTextColor(Color.RED);
			break;
		case R.id.tv_qrcode:
			// 打开二维码
			Intent intent = new Intent(activity, QRCodeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			break;
		case R.id.tv_search:
			String input = editText.getText().toString().trim();
			if (!StringUtils.isEmpty(input)) {
				startActivity(new Intent(activity, SearchActivity.class)
						.putExtra("keyword", input));
				saveSearchHistory();
				mSearchAutoAdapter.initSearchHistory();
				InputMethodManager imm = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(tv_search.getWindowToken(), 0);
				mAutoListView.setAdapter(new SearchAutoCompAdapter());
			} else {
				BaseApplication.getApplication().showToast("请输入搜索内容！");
			}
			break;
		}
	}

	/**
	 * 开启的intent 返回数据--接收
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == activity.RESULT_OK) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						getActivity());
				Bundle bundle = data.getExtras();
				final String url = bundle.getString("result");
				// 用默认浏览器打开扫描得到的地址
				// Intent intent = new Intent();
				// intent.setAction("android.intent.action.VIEW");
				// Uri content_url = Uri.parse(url);
				// intent.setData(content_url);
				// activity.startActivity(intent);
				// activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
				// .parse("www.baidu.com")));

				// 显示扫描到的内容
				dialog.setTitle("提示");
				dialog.setMessage("是否跳转到浏览器？");
				dialog.setNegativeButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 用默认浏览器打开扫描得到的地址
								Intent intent = new Intent();
								intent.setAction("android.intent.action.VIEW");
								Uri content_url = Uri.parse(url);
								intent.setData(content_url);
								activity.startActivity(intent);
								dialog.dismiss();
							}
						});
				dialog.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				dialog.create().show();
				System.out.println("---" + url);
				// 显示
				// mImageView.setImageBitmap((Bitmap) data
				// .getParcelableExtra("bitmap"));
			}
			break;
		}
	}

	/**
	 * 保存搜索记录
	 */
	private void saveSearchHistory() {
		String text = editText.getText().toString().trim();
		if (text.length() < 1) {
			return;
		}
		SharedPreferences sp = activity.getSharedPreferences(SEARCH_HISTORY, 0);
		String longhistory = sp.getString(SEARCH_HISTORY, "");
		String[] tmpHistory = longhistory.split(",");
		ArrayList<String> history = new ArrayList<String>(
				Arrays.asList(tmpHistory));
		if (history.size() > 0) {

			int i;
			for (i = 0; i < history.size(); i++) {
				if (text.equals(history.get(i))) {
					history.remove(i);
					break;
				}
			}
			history.add(0, text);
		}
		if (history.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < history.size(); i++) {
				sb.append(history.get(i) + ",");
			}
			sp.edit().putString(SEARCH_HISTORY, sb.toString()).commit();
		} else {
			sp.edit().putString(SEARCH_HISTORY, text + ",").commit();
		}
	}

}
