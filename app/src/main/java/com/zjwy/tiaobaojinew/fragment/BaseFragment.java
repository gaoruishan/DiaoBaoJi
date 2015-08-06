package com.zjwy.tiaobaojinew.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.activity.SearchActivity;

/**
 * @类 说 明:
 * @version 1.0
 * @创建时间：2014-8-5 下午2:57:23
 * 
 */
@SuppressLint("ClickableViewAccessibility")
public abstract class BaseFragment extends Fragment {
	public Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	/**
	 * 将获取的附属的activity，变为成员变量---供继承的fragment使用
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 提供intent跳转
	 */
	public void startActivity(Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		startActivity(intent);
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(
				activity.getCurrentFocus().getWindowToken(), 0);
	}

	/**
	 * Android EditText 设置键盘 搜索，回车
	 */
	public void editorSendAction(final EditText editText) {
		// Android EditText 设置键盘 搜索，回车
		editText.setImeOptions(EditorInfo.IME_ACTION_SEND);
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND
						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					startActivity(new Intent(activity, SearchActivity.class)
							.putExtra("keyword", editText.getText().toString()));
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 点击屏幕空白处隐藏软键盘
	 */
	public void clickBlankHideSoftInput(View view) {
		// 点击屏幕空白处隐藏软键盘
		// 实例化输入法控制对象，通过hideSoftInputFromWindow来控制，其中第一个参数绑定的为需要隐藏输入法的EditText对象，比
		RelativeLayout activity_main = (RelativeLayout) view
				.findViewById(R.id.activity_sort);
		activity_main.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				hideSoftInput();// 隐藏键盘
				return true;
			}
		});

	}

}
