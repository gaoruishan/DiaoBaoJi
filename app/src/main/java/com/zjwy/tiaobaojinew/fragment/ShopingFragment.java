package com.zjwy.tiaobaojinew.fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.Shake2Share;
import cn.sharesdk.onekeyshare.Shake2Share.OnShakeListener;
import cn.sharesdk.sina.weibo.SinaWeibo;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.activity.ProductDetailsActivity;
import com.zjwy.tiaobaojinew.activity.ShoppingOrderActivity;
import com.zjwy.tiaobaojinew.adapter.ShoppingAdapter;
import com.zjwy.tiaobaojinew.bean.ProductContentBean;

public class ShopingFragment extends BaseFragment {
	private String url;
	private View view;
	@ViewInject(R.id.lv_shopping)
	private PullToRefreshListView prl;
	@ViewInject(R.id.cb_shopping_all)
	private CheckBox cb_shopping_all;
	@ViewInject(R.id.bt_settlement)
	private Button bt_settlement;
	@ViewInject(R.id.tv_shopping_total)
	private TextView tv_shopping_total;
	@ViewInject(R.id.tv_more)
	private TextView tv_more;
	@ViewInject(R.id.tv_order_sum)
	private TextView tv_order_sum;
	private ShoppingAdapter adapter;
	private List<ProductContentBean> beans;
	boolean condition = true;

	@OnClick(R.id.tv_more)
	public void moreClick(View v) { // 点击更多设置
		if (BaseApplication.getSharedPreferences().getBoolean("isLogin", false)) {

			if (adapter.getBeans() != null && adapter.getBeans().size() > 0) {
				ArrayList<ProductContentBean> list2 = adapter.getBeans();
				// if (condition) {
				// condition = false;
				// // 摇一摇
				// setShakeAction(list2);
				// } else {
				showShare(list2);
				// condition = true;
				// }
			} else {
				BaseApplication.getApplication().showToast("请选择一个商品!");
			}
		}
	}

	/**
	 * 设置“摇一摇”的截屏分享
	 * 
	 * @param list2
	 */
	@SuppressWarnings("unused")
	private void setShakeAction(final ArrayList<ProductContentBean> list2) {
		Shake2Share s2s = new Shake2Share();
		// 设置回调，摇晃到一定程度就会触发分享
		s2s.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				OnekeyShare oks = new OnekeyShare();
				// 设置一个用于截屏分享的View
				View windowView = activity.getWindow().getDecorView();
				oks.setViewToShare(windowView);
				oks.setText(list2.get(0).getDescription().trim().toString());
				oks.setPlatform(SinaWeibo.NAME);
				oks.show(activity.getBaseContext());
			}

		});
		// 启动“摇一摇分享”功能
		s2s.show(activity, null);
	}

	/**
	 * 打开一键分享功能进行分享
	 * 
	 * @param list2
	 */
	private void showShare(ArrayList<ProductContentBean> list2) {

		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.logo, getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share) + ":" + list2.get(0).getTitle());
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(getActivity().getString(R.string.diaobaoji));
		// text是分享文本，所有平台都需要这个字段
		/**
		 * 替换
		 * < p >
		 * < /p >
		 * 标签和空格 为“”
		 */
		oks.setText(list2.get(0).getDescription().toString()
				.replaceAll("(<(\\/)?p>)|\\s", ""));
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		File file = bitmapUtils.getBitmapFileFromDiskCache(list2.get(0)
				.getThumb());
		if (file != null) {
			oks.setImagePath(file.getPath());// 确保SDcard下面存在此张图片
		}
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(getActivity().getString(R.string.diaobaoji));
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(getActivity().getString(R.string.diaobaoji));

		// 启动分享GUI
		oks.show(activity);
	}

	/**
	 * 提供一个静态的方法传值，返回实例
	 * 
	 */
	public static ShopingFragment getInstance(String url) {
		ShopingFragment fragment = new ShopingFragment();
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		fragment.setArguments(bundle);
		return fragment;
	}

	public ShopingFragment() {
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
		view = inflater.inflate(R.layout.layout_shoping, container, false);
		ViewUtils.inject(this, view);// 注入
		initView(view, inflater);
		initDatas();// 获取购物车数据
		setListener();// 监听
		bitmapUtils = new BitmapUtils(activity);
		// adapter = new ShoppingAdapter(bitmapUtils, beans, activity,
		// tv_shopping_total, tv_order_sum);
		return view;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (beans.size() > 0) {
				adapter = new ShoppingAdapter(bitmapUtils, beans, activity,
						tv_shopping_total, tv_order_sum);
				prl.setAdapter(adapter);
				cb_shopping_all.setEnabled(true);
			}
		};
	};
	private BitmapUtils bitmapUtils;

	private void initDatas() {// 登录后才能正常访问
		if (BaseApplication.getSharedPreferences().getBoolean("isLogin", false)) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					HttpGet httpGet = new HttpGet(url);
					try {
						HttpResponse response = BaseApplication.getHttpClient()
								.execute(httpGet);
						if (response.getStatusLine().getStatusCode() == 200) {
							String result = EntityUtils.toString(
									response.getEntity(), "UTF-8");
							JSONObject jsObj = new JSONObject(result);
							if ("OK".equals(jsObj.getString("message"))) {
								JSONObject object1 = new JSONObject(
										jsObj.getString("data"));
								beans = JSON.parseArray(
										object1.getString("infos"),
										ProductContentBean.class);
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
		/**
		 * 从数据库取 ：beans = BaseApplication.getDbUtils().findAll(
		 * Selector.from(ProductContentBean.class).where("sort_id", "=",
		 * "shopping")); prl.setSelection(beans.size());//置底
		 */

	}

	@Override
	public void onStart() {
		super.onStart();
		/**
		 * 只有当界面获得焦点时，在进行获取数据(解决后来加入购物车时无数据)
		 */
		initDatas();
		tv_order_sum.setText("总共选择了：" + 0);// 置零
	}

	private void setListener() {
		/**
		 * 要想listView的item与其上的button皆能得到焦点响应： 在listView item 的布局中：
		 * 在<RelativeLayout>中 android:descendantFocusability="blocksDescendants"
		 * 和<ImageButton>中 android:focusable="false"
		 */
		prl.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position > 0) {
					startActivity(new Intent(activity,
							ProductDetailsActivity.class).putExtra("id", beans
							.get(position - 1).getId()));
				}
			}
		});
		/**
		 * 点击全选
		 */
		cb_shopping_all.setEnabled(false);
		cb_shopping_all
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (adapter != null) {
							if (isChecked) {
								adapter.setIsChecked(true);
								tv_order_sum.setText("总共选择了：" + beans.size());
							} else {
								adapter.setIsChecked(false);
							}
						}
					}
				});
		/**
		 * 结算
		 */
		bt_settlement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (adapter != null) {
					ArrayList<ProductContentBean> list = adapter.getBeans();// 获取选中的bean
					if (list.size() > 0) {
						startActivity(new Intent(activity,
								ShoppingOrderActivity.class)
								.putParcelableArrayListExtra("beans", list));
						cb_shopping_all.setChecked(false);
					} else {
						BaseApplication.getApplication().showToast("请选择一个商品！");
					}

				} else {
					BaseApplication.getApplication().showToast("请先登录哦！");
				}
			}
		});
	}

	private void initView(View view2, LayoutInflater inflater2) {
		ILoadingLayout ill = prl.getLoadingLayoutProxy();
		ill.setPullLabel("~(^ˍ^)~ 嘻嘻");
		ill.setReleaseLabel("亲，松开你的手哦！");
		ill.setRefreshingLabel("拼命刷新。。。。");
		ill.setLoadingDrawable(getResources().getDrawable(// 刷新图标
				R.drawable.progress_rotate));

		prl.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String data2 = new SimpleDateFormat("dd日  HH时mm分ss秒")
						.format(new Date());// 设置刷新时间
				prl.getLoadingLayoutProxy().setLastUpdatedLabel(data2);
				initDatas();// 加载数据
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						prl.onRefreshComplete();// 推迟500完成刷新---一定要提交
					}
				}, 2000);
			}
		});
	}
}
