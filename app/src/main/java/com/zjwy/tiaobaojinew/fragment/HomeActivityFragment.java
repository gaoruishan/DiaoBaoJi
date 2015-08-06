package com.zjwy.tiaobaojinew.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.adapter.HomeActivityListViewAdapter;
import com.zjwy.tiaobaojinew.adapter.ImagePagerAdapter;
import com.zjwy.tiaobaojinew.bean.HomeActivityBean;
import com.zjwy.tiaobaojinew.bean.HomeActivityBean.BeanChild;
import com.zjwy.tiaobaojinew.interf.setMyScrollViewListener;
import com.zjwy.tiaobaojinew.widget.AutoScrollViewPager;
import com.zjwy.tiaobaojinew.widget.ListViewForScrollView;
import com.zjwy.tiaobaojinew.widget.MyScrollView;

@SuppressLint("ClickableViewAccessibility")
public class HomeActivityFragment extends BaseFragment {

	private String url;
	private BitmapUtils bitmapUtils;
	// 利用注解是方式来findViewById
	@ViewInject(R.id.activity_viewpager)
	private AutoScrollViewPager viewPager;
	private ImagePagerAdapter imagePagerAdapter;// 对自动轮播的图片适配
	private ArrayList<View> imageViews = new ArrayList<View>();
	private ArrayList<View> ivList = new ArrayList<View>();
	@ViewInject(R.id.home_lv)
	private ListViewForScrollView prl;
	private ArrayList<BeanChild> list = new ArrayList<BeanChild>();
	@ViewInject(R.id.home_iv01)
	private ImageView iv01;
	@ViewInject(R.id.home_iv02)
	private ImageView iv02;
	@ViewInject(R.id.tv_refresh)
	private TextView tv_refresh;
	@ViewInject(R.id.point01)
	private ImageView point01;
	@ViewInject(R.id.point02)
	private ImageView point02;
	@ViewInject(R.id.point03)
	private ImageView point03;
	@ViewInject(R.id.point04)
	private ImageView point04;
	@ViewInject(R.id.tv_buttom)
	private TextView tv_buttom;
	@ViewInject(R.id.tv_load)
	private TextView tv_load;
	@ViewInject(R.id.tv_bg)
	private TextView tv_bg;
	@ViewInject(R.id.pb_home)
	private ProgressBar pb_home;
	@ViewInject(R.id.sv_home)
	private MyScrollView sv_home;

	private HomeActivityListViewAdapter homeListViewAdapter;

	private HttpUtils httpUtils = new HttpUtils();
	private View view;

	public HomeActivityFragment(String url) {
		super();
		this.url = url;
	}

	/**
	 * 提供一个静态的方法传值，返回实例
	 * 
	 */
	public static HomeActivityFragment getInstance(String url) {
		HomeActivityFragment fragment = new HomeActivityFragment();
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		fragment.setArguments(bundle);
		return fragment;
	}

	public HomeActivityFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		url = getArguments().getString("url");// 创建时获得url
		bitmapUtils = new BitmapUtils(activity);
		// 创建fragment时开启任务
		startTask();
	}

	@Override
	public void onPause() {
		super.onPause();
		viewPager.stopAutoScroll();
		task.cancel();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		viewPager.stopAutoScroll();
		task.cancel();
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				tv_load.setText("   加");
				break;
			case 2:
				tv_load.setText("   加载");
				break;
			case 3:
				tv_load.setText("   加载中..");
				break;
			case 4:
				tv_load.setText("   加载中....");
				break;
			}
		};
	};

	/**
	 * 开启加载动画 定时器任务
	 */
	public void startTask() {
		Timer timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				try {
					handler.sendEmptyMessage(1);
					Thread.sleep(400);

					handler.sendEmptyMessage(2);
					Thread.sleep(400);

					handler.sendEmptyMessage(3);
					Thread.sleep(400);

					handler.sendEmptyMessage(4);
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		timer.schedule(task, 1000, 2000);
		task.run();
	}

	/**
	 * 视图创建时fragment，将布局添加到viewpager中
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_home_activity, container,
				false);
		ViewUtils.inject(this, view);// 申明的时候用注解，要在此注入布局视图
		initView();// 初始化视图
		initDatas();// 获取网络数据
		setListener();
		return view;
	}

	private int preposition;
	private TimerTask task;

	private void setListener() {

		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					viewPager.startAutoScroll();// 设置开始轮播图片
				}
				return false;
			}
		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				ImageView imageView = (ImageView) view
						.findViewById(R.id.point01 + position);
				ImageView imageView1 = (ImageView) view
						.findViewById(R.id.point01 + preposition);
				imageView.setImageResource(R.drawable.point1);
				imageView1.setImageResource(R.drawable.point);
				preposition = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	/**
	 * 获取网络数据---jso数据
	 */
	private void initDatas() {
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				BaseApplication.getApplication().showToast("网络出了问题!");
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						task.cancel();
						pb_home.setVisibility(View.GONE);
						tv_load.setVisibility(View.GONE);
						tv_refresh.setVisibility(View.VISIBLE);
					}
				}, 5000);
				tv_refresh.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						initDatas();// 重新加载数据
						pb_home.setVisibility(View.VISIBLE);
						tv_load.setVisibility(View.VISIBLE);
						tv_refresh.setVisibility(View.GONE);
					}
				});
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				try {
					JSONObject object = new JSONObject(result);
					String string = object.getString("message");
					if (string.equals("OK")) {
						HomeActivityBean bean = JSON.parseObject(
								object.getString("data"),
								HomeActivityBean.class);
						// 设置图片大小
						// bitmapUtils.configDefaultBitmapMaxSize(350, 250);
						// 保存 7天
						bitmapUtils.configDefaultCacheExpiry(604800000);// 7天
						bitmapUtils.display(iv02, bean.getActivityBanner());
						/**
						 * 设置进度条
						 */
						bitmapUtils.display(iv02, bean.getActivityBanner(),
								new BitmapLoadCallBack<ImageView>() {

									@Override
									public void onLoadCompleted(
											ImageView container, String uri,
											Bitmap bitmap,
											BitmapDisplayConfig arg3,
											BitmapLoadFrom arg4) {
										task.cancel();
										tv_bg.setVisibility(View.GONE);
										pb_home.setVisibility(View.GONE);
										tv_load.setVisibility(View.GONE);
										iv02.setImageBitmap(bitmap);// 设置图片
									}

									@Override
									public void onLoadFailed(ImageView arg0,
											String arg1, Drawable arg2) {
									}
								});
						bitmapUtils.display(iv01, bean.getFocus());
						list = (ArrayList<BeanChild>) bean.getSingle();
						homeListViewAdapter = new HomeActivityListViewAdapter(
								list, activity, bitmapUtils);
						prl.setAdapter(homeListViewAdapter);
						List<String> info = bean.getActivityInfo();
						setAutoScrollView(info);// 轮播viewPager
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			/**
			 * 设置轮播的加载
			 * 
			 * @param info
			 */
			private void setAutoScrollView(final List<String> info) {
				for (int i = 0; i < info.size(); i++) {
					final ImageView imageView = new ImageView(activity);
					LayoutParams layoutParams = new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);
					imageView.setLayoutParams(layoutParams);
					imageView.setScaleType(ScaleType.FIT_XY);
					// bitmapUtils.display(imageView, info.get(i));
					/**
					 * 设置进度条
					 */
					bitmapUtils.display(imageView, info.get(i),
							new BitmapLoadCallBack<ImageView>() {

								@Override
								public void onLoadCompleted(
										ImageView container, String uri,
										Bitmap bitmap,
										BitmapDisplayConfig arg3,
										BitmapLoadFrom arg4) {
									task.cancel();
									tv_bg.setVisibility(View.GONE);
									pb_home.setVisibility(View.GONE);
									tv_load.setVisibility(View.GONE);
									imageView.setImageBitmap(bitmap);// 设置图片
									for (int j = 0; j < info.size(); j++) {
										ivList.get(j).setVisibility(
												View.VISIBLE);// 显示点
									}
								}

								@Override
								public void onLoadFailed(ImageView arg0,
										String arg1, Drawable arg2) {
								}
							});
					imageViews.add(imageView);
				}
				// viewPager.startAutoScroll();
				imagePagerAdapter = new ImagePagerAdapter(imageViews);
				viewPager.setAdapter(imagePagerAdapter);
			}
		});
	}

	/**
	 * 初始化控件---监听
	 */
	private void initView() {
		ivList.add(point01);
		ivList.add(point02);
		ivList.add(point03);
		ivList.add(point04);
		sv_home = (MyScrollView) view.findViewById(R.id.sv_home);
		// ScrollView滚动监听--设置隐藏和显示
		sv_home.onScroll(new setMyScrollViewListener() {
			@Override
			public void up() {// 顶部
			}

			@Override
			public void down() {// 底部
				tv_buttom.setVisibility(View.VISIBLE);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						tv_buttom.setVisibility(View.GONE);
					}
				}, 1000);

			}
		});
		sv_home.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					/** 滑动到顶部和底部做处理 **/
					sv_home.istouch = true;
				}
				return false;
			}
		});
	}
}
