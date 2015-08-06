package com.zjwy.tiaobaojinew.activity;

import java.util.ArrayList;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.adapter.MainPagerAdapter;
import com.zjwy.tiaobaojinew.fragment.CenterFragment;
import com.zjwy.tiaobaojinew.fragment.HomesFragment;
import com.zjwy.tiaobaojinew.fragment.ShopingFragment;
import com.zjwy.tiaobaojinew.fragment.SortFragment;
import com.zjwy.tiaobaojinew.interf.AcitivityCallBack;
import com.zjwy.tiaobaojinew.utils.ActivitySplitAnimationUtil;
import com.zjwy.tiaobaojinew.utils.AppConstant;
import com.zjwy.tiaobaojinew.utils.AppManager;

public class MainActivity extends BaseActivity implements OnPageChangeListener,
		AcitivityCallBack {
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private boolean isFirst = true;
	private static ViewPager viewPager;
	public static final int COLOR = 0xFF8409FF;
	private long exitTime;
	private ImageView iv_home, iv_sort, iv_center, iv_package, iv_shopping;
	private TextView tv_home, tv_sort, tv_center, tv_shopping, tv_package;

	@Override
	public void setView() {
		// 准备图像分割
		ActivitySplitAnimationUtil.prepareAnimation(this);
		// getSupportActionBar().hide();// 隐藏掉整个ActionBar，包括下面的Tabs
		setContentView(R.layout.activity_main);
		// 开启动画，展示Activity
		ActivitySplitAnimationUtil.animate(this, 900);
	}

	@Override
	public void initView() {
		// 初始化viewpager
		initViewpager();
		// 检测app更新
		// UpdateManager.getUpdateManager().checkAppUpdate(this, true);

		iv_home = (ImageView) findViewById(R.id.iv_home);
		iv_sort = (ImageView) findViewById(R.id.iv_sort);
		iv_center = (ImageView) findViewById(R.id.iv_center);
		iv_shopping = (ImageView) findViewById(R.id.iv_shopping);
		iv_package = (ImageView) findViewById(R.id.iv_package);

		tv_home = (TextView) findViewById(R.id.tv_home);
		tv_sort = (TextView) findViewById(R.id.tv_sort);
		tv_center = (TextView) findViewById(R.id.tv_center);
		tv_shopping = (TextView) findViewById(R.id.tv_shopping);
		tv_package = (TextView) findViewById(R.id.tv_package);

	}

	/**
	 * @Title: defaultView
	 * @说 明:设置底部导航的默认属性
	 * @参 数:
	 * @return void 返回类型
	 * @throws
	 */
	private void defaultView() {

		iv_home.setImageResource(R.drawable.ic_home_default);
		iv_center.setImageResource(R.drawable.ic_center_default);
		iv_sort.setImageResource(R.drawable.ic_sort_default);
		iv_shopping.setImageResource(R.drawable.ic_shopping_default);
		iv_package.setImageResource(R.drawable.ic_package_default);
		tv_home.setTextColor(Color.BLACK);
		tv_center.setTextColor(Color.BLACK);
		tv_sort.setTextColor(Color.BLACK);
		tv_sort.setTextColor(Color.BLACK);
		tv_shopping.setTextColor(Color.BLACK);
		tv_package.setTextColor(Color.BLACK);
	}

	/**
	 * 设置底部导航的点击事件的切换图片
	 * 
	 * @param view
	 */
	public void navigation(View view) {
		int id = view.getId();
		defaultView();
		switch (id) {
		case R.id.iv_home:
			iv_home.setImageResource(R.drawable.ic_home);
			tv_home.setTextColor(COLOR);
			viewPager.setCurrentItem(0);
			break;
		case R.id.iv_sort:
			iv_sort.setImageResource(R.drawable.ic_sort);
			tv_sort.setTextColor(COLOR);
			viewPager.setCurrentItem(1);
			break;
		case R.id.iv_package:
			iv_package.setImageResource(R.drawable.ic_package);
			tv_package.setTextColor(COLOR);
			// viewPager.setCurrentItem(2);
			startActivity(PackageActivity.class);
			break;
		case R.id.iv_shopping:
			iv_shopping.setImageResource(R.drawable.ic_shopping);
			tv_shopping.setTextColor(COLOR);
			viewPager.setCurrentItem(2);
			break;
		case R.id.iv_center:
			iv_center.setImageResource(R.drawable.ic_center);
			tv_center.setTextColor(COLOR);
			viewPager.setCurrentItem(3);
			break;
		}
	}

	/**
	 * 初始化viewpager
	 */
	public void initViewpager() {
		viewPager = (ViewPager) this.findViewById(R.id.main_viewpager);
		fragments.add(HomesFragment.getInstance());
		fragments.add(SortFragment.getInstance(AppConstant.SORT_BRAND));
		// fragments.add(PackagesFragment.getInstance());
		fragments.add(ShopingFragment.getInstance(AppConstant.SHOPPING));
		fragments.add(CenterFragment.getInstance());
		viewPager.setOnPageChangeListener(this);
		viewPager.setAdapter(new MainPagerAdapter(fragments,
				getSupportFragmentManager()));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 页面滚动监听
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	/**
	 * 当viewpager改变时，对应的底部导航的切换
	 */
	@Override
	public void onPageSelected(int position) {
		defaultView();
		switch (position) {
		case 0:
			iv_home.setImageResource(R.drawable.ic_home);
			tv_home.setTextColor(COLOR);
			break;
		case 1:
			iv_sort.setImageResource(R.drawable.ic_sort);
			tv_sort.setTextColor(COLOR);
			break;
		// case 2:
		// iv_package.setImageResource(R.drawable.ic_package);
		// tv_package.setTextColor(COLOR);
		// break;
		case 2:
			iv_shopping.setImageResource(R.drawable.ic_shopping);
			tv_shopping.setTextColor(COLOR);
			if (!BaseApplication.getSharedPreferences().getBoolean("isLogin",
					false)) {
				if (isFirst) {
					// Toast.makeText(this, "请登录！", Toast.LENGTH_SHORT).show();
					startActivity(LoginAcitivity.class);
					isFirst = false;
				}
			}
			break;
		case 3:
			iv_center.setImageResource(R.drawable.ic_center);
			tv_center.setTextColor(COLOR);
			break;
		}
	}

	/**
	 * 按返回键
	 */
	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - exitTime > 2000) {
			BaseApplication.getApplication().showToast(
					getString(R.string.exit_str));// 可直接调用的吐司
			exitTime = System.currentTimeMillis();
		} else {
			if (BaseApplication.getApplication() != null) {
				BaseApplication.getApplication().unRegisterLogin();// 注销登录
				BaseApplication.getApplication()
						.unRegisterNetworkStateReceiver();// 注销网络监听
				AppManager.getAppManager().AppExit(this);// 退出应用程序

			}
		}

	}

	@Override
	protected void onStop() {
		// 取消动画
		ActivitySplitAnimationUtil.cancel();
		super.onStop();
	}

	/**
	 * 点击购物车回调跳转--购物车
	 */
	@Override
	public void goToShopping() {
		viewPager.setCurrentItem(2);
	}

	@Override
	public void setListener() {

	}

	@Override
	public void initData() {

	}

}
