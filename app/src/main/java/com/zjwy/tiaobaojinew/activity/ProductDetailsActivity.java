package com.zjwy.tiaobaojinew.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.adapter.ImagePagerAdapter;
import com.zjwy.tiaobaojinew.adapter.ProductPicAdapter;
import com.zjwy.tiaobaojinew.bean.ProductContentBean;
import com.zjwy.tiaobaojinew.bean.ProductContentBean.ActivityInfos;
import com.zjwy.tiaobaojinew.interf.AcitivityCallBack;
import com.zjwy.tiaobaojinew.interf.setMyScrollViewListener;
import com.zjwy.tiaobaojinew.utils.AppConstant;
import com.zjwy.tiaobaojinew.utils.ShopCartAnimationUtil;
import com.zjwy.tiaobaojinew.utils.StringUtils;
import com.zjwy.tiaobaojinew.utils.Utils;
import com.zjwy.tiaobaojinew.widget.AutoScrollViewPager;
import com.zjwy.tiaobaojinew.widget.ListViewForScrollView;
import com.zjwy.tiaobaojinew.widget.MyScrollView;

public class ProductDetailsActivity extends BaseActivity {
	private String id;
	@ViewInject(R.id.tv_back_pd)
	private TextView tv_back;
	@ViewInject(R.id.tv_title_pd)
	private TextView tv_title;
	@ViewInject(R.id.content_viewpager)
	private AutoScrollViewPager viewPager;
	private ImagePagerAdapter imagePagerAdapter;// 对自动轮播的图片适配
	private ArrayList<View> imageViews = new ArrayList<View>();
	private ArrayList<ImageView> ivList = new ArrayList<ImageView>();
	private ArrayList<TextView> tvList = new ArrayList<TextView>();
	@ViewInject(R.id.appraiser)
	private ImageView appraiser;
	@ViewInject(R.id.story)
	private ImageView story;
	private HttpUtils httpUtils = new HttpUtils();
	private BitmapUtils bitmapUtils;
	@ViewInject(R.id.title)
	private TextView activity_title;
	@ViewInject(R.id.rents_7)
	private TextView rents_7;
	@ViewInject(R.id.rents_10)
	private TextView rents_10;
	@ViewInject(R.id.market_price)
	private TextView market_price;
	@ViewInject(R.id.content_brand)
	private TextView content_brand;
	@ViewInject(R.id.sort)
	private TextView sort;
	@ViewInject(R.id.origin)
	private TextView origin;
	@ViewInject(R.id.colors)
	private TextView colors;
	@ViewInject(R.id.size)
	private TextView size;
	@ViewInject(R.id.material_inner)
	private TextView material_inner;
	@ViewInject(R.id.material_outer)
	private TextView material_outer;
	@ViewInject(R.id.style_open)
	private TextView style_open;
	@ViewInject(R.id.structure_inner)
	private TextView structure_inner;
	@ViewInject(R.id.level_new)
	private TextView level_new;
	@ViewInject(R.id.tv_gift)
	private TextView tv_gift;
	@ViewInject(R.id.tv_jian9)
	private TextView tv_jian9;
	@ViewInject(R.id.tv_jian19)
	private TextView tv_jian19;
	@ViewInject(R.id.tv_jian199)
	private TextView tv_jian199;
	@ViewInject(R.id.tv_buttom_prod)
	private TextView tv_buttom_prod;
	@ViewInject(R.id.pb_prod)
	private ProgressBar pb_prod;
	@ViewInject(R.id.tv_prod_loading)
	private TextView tv_prod_loading;
	@ViewInject(R.id.tv_prod_bg)
	private TextView tv_prod_bg;
	@ViewInject(R.id.cart)
	private TextView cart;
	@ViewInject(R.id.order)
	private TextView order;
	@ViewInject(R.id.prod_remind)
	private TextView prod_remind;
	@ViewInject(R.id.lv_pic)
	private ListViewForScrollView lv_pic;
	@ViewInject(R.id.backTop)
	private ImageView backTop;
	@ViewInject(R.id.kefu)
	private TextView kefu;
	@ViewInject(R.id.shoppingcart)
	private ImageView shoppingcart;
	@ViewInject(R.id.p01)
	private ImageView p01;
	@ViewInject(R.id.p02)
	private ImageView p02;
	@ViewInject(R.id.p03)
	private ImageView p03;
	@ViewInject(R.id.p04)
	private ImageView p04;
	@ViewInject(R.id.p05)
	private ImageView p05;
	@ViewInject(R.id.p06)
	private ImageView p06;
	@ViewInject(R.id.p07)
	private ImageView p07;
	@ViewInject(R.id.iv_prod_package)
	private ImageView iv_prod_package;
	@ViewInject(R.id.sc)
	private MyScrollView sc;
	private float y_tmp1;
	private float y_tmp2;
	private ProductContentBean bean;
	private ArrayList<String> list;

	@ViewInject(R.id.sc)
	private ScrollView sl;
	private String url;
	protected int preposition;
	protected boolean orderEnable = true;

	@Override
	public void setView() {
		setContentView(R.layout.activity_product_detail);
		ViewUtils.inject(this);
		// viewPager的相对布局宽高设置
		Utils.setViewPagerParamLayout(this, findViewById(R.id.prod_rlayout));
	}

	@Override
	public void initView() {
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		url = intent.getStringExtra("url");
		tv_title.setText("商品详情");
		// 添加view
		addView();
		// 添加到购物车
		ShopCartAnimationUtil.prepareAnimation(this, shoppingcart);

	}

	@Override
	public void initData() {
		// 加载数据
		if (id != null) {// 判断 传过来的是什么参数
			initDatas(AppConstant.HOME_HEAD + id);
		} else if (url != null) {
			initDatas(url);
		}

		// 是否可租
		validRent();
	}

	/**
	 * 判断是否可租
	 */
	private void validRent() {
		httpUtils.send(HttpMethod.GET, AppConstant.VALIDRENT + id,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						BaseApplication.getApplication().showToast("网络出了问题!");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							JSONObject object = new JSONObject(arg0.result);
							if (!object.getString("message").equals("OK")) {
								// order.setEnabled(false);
								orderEnable = false;
								// order.setBackgroundColor(Color.GRAY);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	private AcitivityCallBack callBack = new MainActivity();

	/**
	 * 点击购物车，跳转
	 */
	@OnClick(R.id.shoppingcart)
	public void shoppingcartClick(View v) {
		// 接口回调
		callBack.goToShopping();
		onBackPressed();
	}

	/**
	 * 点击返回键
	 */
	@OnClick(R.id.tv_back_pd)
	public void backButtonClick(View v) {
		onBackPressed();
	}

	/**
	 * 立即下单
	 */
	@OnClick(R.id.order)
	public void orderButtonClick(View v) {
		if (bean != null && orderEnable) {
			startActivity(new Intent(this, SingleOrdersActivity.class)
					.putExtra("bean", bean));
		} else {
			BaseApplication.getApplication().showToast("商品正在使用中！");
		}
	}

	/**
	 * 返回顶部
	 */
	@OnClick(R.id.backTop)
	public void backTopButtonClick(View v) { // 方法签名必须和接口中的要求一致
		sl.smoothScrollTo(0, 0);
	}

	/**
	 * 点击进入客服
	 */
	@OnClick(R.id.kefu)
	public void kefuClick(View v) {
		// 用intent启动拨打电话
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ AppConstant.KEFU));
		startActivity(intent);
	}

	/**
	 * 点击试包间
	 */
	@OnClick(R.id.iv_prod_package)
	public void ImClickPackage(View v) {
		startActivity(PackageActivity.class);
	}

	/**
	 * 点击提醒
	 */
	@OnClick(R.id.prod_remind)
	public void remindClick(View v) {
		// &mobile=15192780515&good_id=24
		final String mobile = BaseApplication.getSharedPreferences().getString(
				"mobile", "");
		if (mobile != null && !StringUtils.isEmpty(mobile)) {
			BaseApplication.getApplication().showToast("到货后马上提醒你！");
			new Thread(new Runnable() {

				@Override
				public void run() {
					HttpGet httpGet = new HttpGet(AppConstant.REMIND
							+ "&mobile=" + mobile + "&good_id=" + id);
					try {
						HttpResponse response = BaseApplication.getHttpClient()
								.execute(httpGet);
						if (response.getStatusLine().getStatusCode() == 200) {
							String result = EntityUtils.toString(
									response.getEntity(), "UTF-8");
							JSONObject jsObj = new JSONObject(result);
							if ("OK".equals(jsObj.getString("message"))) {
								System.out.println("已设置提醒！");
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
		} else {
			BaseApplication.getApplication().showToast("请登录！");
		}

	}

	/**
	 * 加入购物车
	 */
	@OnClick(R.id.cart)
	public void cartButtonClick(View v) {
		if (BaseApplication.getSharedPreferences().getBoolean("isLogin", false)) {
			// 开始执行动画
			ShopCartAnimationUtil.startAnimation(v);
			new Thread(new Runnable() {

				@Override
				public void run() {
					HttpGet httpGet = new HttpGet(AppConstant.ADD_SHOPPING + id);
					try {
						HttpResponse response = BaseApplication.getHttpClient()
								.execute(httpGet);
						if (response.getStatusLine().getStatusCode() == 200) {
							String result = EntityUtils.toString(
									response.getEntity(), "UTF-8");
							JSONObject jsObj = new JSONObject(result);
							if ("OK".equals(jsObj.getString("message"))) {
								System.out.println("已添加到购物车");
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
		} else {
			if (!BaseApplication.getSharedPreferences().getBoolean("isLogin",
					false)) {
				startActivity(new Intent(this, LoginAcitivity.class));
			} else {
				if (isFrist) {
					BaseApplication.getApplication().showToast("已登录");
					isFrist = false;
				}
			}
		}

	}

	private boolean isFrist = true;

	/**
	 * 添加需要的视图
	 */
	private void addView() {
		ivList.add(p01);
		ivList.add(p02);
		ivList.add(p03);
		ivList.add(p04);
		ivList.add(p05);
		ivList.add(p06);
		ivList.add(p07);
		tvList.add(tv_gift);
		tvList.add(tv_jian9);
		tvList.add(tv_jian19);
		tvList.add(tv_jian199);
	}

	/**
	 * 初始化点击事件
	 */
	@Override
	@SuppressLint("ClickableViewAccessibility")
	public void setListener() {
		// viewPager点击开启滚动

		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					viewPager.startAutoScroll();
				}
				return false;
			}
		});
		// viewpager滚动时 --圆点的滚动
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				ImageView imageView = (ImageView) ProductDetailsActivity.this
						.findViewById(R.id.p01 + position);
				ImageView imageView1 = (ImageView) ProductDetailsActivity.this
						.findViewById(R.id.p01 + preposition);
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
		// SrollView 置底部事件处理
		sc.onScroll(new setMyScrollViewListener() {
			@Override
			public void up() {
			}

			boolean isonce = true;

			@Override
			public void down() {
				tv_buttom_prod.setVisibility(View.VISIBLE);
				if (isonce) {
					tv_buttom_prod.setText("加载更多！");
					setDetailIcon(list);// 填充图片
					isonce = false;
				} else {
					tv_buttom_prod.setText("已到底部！");
				}
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						tv_buttom_prod.setVisibility(View.GONE);
					}
				}, 1000);

			}
		});
		// Srollview上下滚动监听--是否显示置顶图标
		sc.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					y_tmp1 = event.getY();
				}
				if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_MOVE) {
					/** 滑动到顶部和底部做处理 **/
					y_tmp2 = event.getY();
					// 判断上下滑动方向
					if (y_tmp1 - y_tmp2 > 5) {// 向下滑动
						backTop.setVisibility(View.GONE);
					}
					if (y_tmp2 - y_tmp1 > 5) {// 向上滑动
						backTop.setVisibility(View.VISIBLE);
					}
					sc.istouch = true;
				}
				return false;
			}
		});

	}

	@Override
	public void onStop() {
		super.onStop();
		viewPager.stopAutoScroll();
	}

	/**
	 * 在底部填写详情图片
	 */
	private void setDetailIcon(ArrayList<String> list) {
		lv_pic.setAdapter(new ProductPicAdapter(list,
				ProductDetailsActivity.this));
	}

	/**
	 * 初始化数据--URL访问网络数据
	 */
	private void initDatas(String str) {
		bitmapUtils = new BitmapUtils(ProductDetailsActivity.this);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {

			private int num;

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
						bean = JSON.parseObject(object1.getString("infos"),
								ProductContentBean.class);
						// 填充文本内容
						setProductContent(bean);
						// 初始化分享
						// initOnekeyShare();
						// 初始化快速评论栏
						// initQuickCommentBar();
						JSONObject object2 = new JSONObject(object1
								.getString("infos"));
						JSONArray jsonArray = new JSONArray(object2
								.getString("picture"));
						list = new ArrayList<String>();
						for (int i = 0; i < jsonArray.length(); i++) {
							list.add(jsonArray.get(i).toString());
						}
						// 轮播viewPager
						setAutoScrollView(list);
						// 历史和评价图片
						// bitmapUtils.display(appraiser,
						// bean.getPicture_appraiser());
						// bitmapUtils.display(story, bean.getPicture_story());

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			/**
			 * 设置详情页的内容
			 * 
			 * @param bean
			 */
			private void setProductContent(ProductContentBean bean) {
				activity_title.setText(bean.getTitle());
				rents_7.setText("  租价：RMB " + bean.getPrice_rent_7() + "/7天");
				rents_10.setText("             RMB " + bean.getPrice_rent_10()
						+ "/10天");
				market_price.setText("市场价：RMB " + bean.getPrice_market() + "元");
				content_brand.setText("品牌：" + bean.getBrand_code());
				sort.setText(".款式：" + bean.getSort_id());
				origin.setText(".产地：" + bean.getPlace_origin());
				colors.setText(".颜色：" + bean.getColor());
				size.setText(".尺寸：" + bean.getSize());
				material_inner.setText(".内部材质：" + bean.getMaterial_inner());
				material_outer.setText(".面料材质：" + bean.getMaterial_outer());
				style_open.setText(".开合方式：" + bean.getStyle_open());
				structure_inner.setText(".内部结构：" + bean.getStructure_inner());
				level_new.setText(".新旧程度：" + bean.getLevel_new());
				List<ActivityInfos> infos = bean.getActivityInfos();
				if (infos.size() > 0) {
					for (int i = 0; i < infos.size(); i++) {
						String name = infos.get(i).getName();
						tvList.get(i).setText(name);
					}
				}
			}

			/**
			 * 设置轮播的加载
			 * 
			 * @param info
			 */
			private void setAutoScrollView(final List<String> info) {
				num = info.size();
				for (int i = 0; i < num; i++) {
					ivList.get(i).setVisibility(View.VISIBLE);// 显示 点
					final ImageView imageView = new ImageView(
							ProductDetailsActivity.this);
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
									tv_prod_bg.setVisibility(View.GONE);
									tv_prod_loading.setVisibility(View.GONE);
									pb_prod.setVisibility(View.GONE);
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
				imagePagerAdapter = new ImagePagerAdapter(imageViews);
				viewPager.setAdapter(imagePagerAdapter);
			}
		});
	}

	/**
	 * 开启定时器!!!----会占用开启Activity的时间！！！
	 */
	@SuppressWarnings("unused")
	private void startTimerTask() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				try {
					Animation animation = AnimationUtils.loadAnimation(
							ProductDetailsActivity.this, R.anim.alpha);
					Animation animation01 = AnimationUtils.loadAnimation(
							ProductDetailsActivity.this, R.anim.alpha01);
					kefu.setAnimation(animation);
					Thread.sleep(3000);
					kefu.setAnimation(animation01);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		timer.schedule(task, 0, 6000);
		task.run();
	}

}
