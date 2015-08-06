package com.zjwy.tiaobaojinew.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.activity.ProductDetailsActivity;
import com.zjwy.tiaobaojinew.activity.SearchActivity;
import com.zjwy.tiaobaojinew.adapter.SortGridViewAdapter;
import com.zjwy.tiaobaojinew.bean.BrandBean;
import com.zjwy.tiaobaojinew.bean.HomeBestNewsBean;
import com.zjwy.tiaobaojinew.bean.HomeBestNewsBean.InfoBean;
import com.zjwy.tiaobaojinew.utils.AppConstant;
import com.zjwy.tiaobaojinew.utils.StringUtils;
import com.zjwy.tiaobaojinew.utils.Utils;

public class SortFragment extends BaseFragment implements OnClickListener {

	private static final int COLOR = 0xFFE4CAFF;
	private String[] price = { "99元", "199元", "全部" };
	private String[] style = { "单肩包", "手领包", "斜跨包", "双肩包", "钱包", "手包", "晚宴包" };
	private String url;
	private GridView gv;
	private TextView btprice, btstyle, brand, tv_search, tv_load03;
	private PopupWindow popWindow;
	private View view;
	private LayoutInflater inflater;
	private HttpUtils httpUtils = new HttpUtils();
	private List<String> list;
	// 保存品牌的图片和id(第二次点击)
	private static List<String> tempBrand = new ArrayList<String>();
	private static ArrayList<String> ids = new ArrayList<String>();
	private List<String> datasbrand = new ArrayList<String>();
	private List<String> datasprice_market = new ArrayList<String>();
	private List<String> datasprice_rent = new ArrayList<String>();
	// 某个品牌下的所有商品 ids
	private ArrayList<String> idBrand = new ArrayList<String>();
	private SortGridViewAdapter adapter;
	protected boolean isBrand = true;
	protected ArrayList<String> templist = new ArrayList<String>();
	public static final int COLORs = 0xFF8409FF;
	private EditText editText;
	private ProgressBar pb;
	private TextView tv_refreshs;

	public SortFragment(String url) {
		super();
		this.url = url;
	}

	/**
	 * 提供一个静态的方法传值，返回实例
	 * 
	 */
	public static SortFragment getInstance(String url) {
		SortFragment fragment = new SortFragment();
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		fragment.setArguments(bundle);
		return fragment;
	}

	public SortFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		url = getArguments().getString("url");// 创建时获得url
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 视图创建时fragment，将布局添加到viewpager中
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.layout_sort, container, false);
		// ViewUtils.inject(this, view);
		initView(view, inflater);
		setListener();// 监听
		initDatas();
		adapter = new SortGridViewAdapter(activity, pb, tv_load03);
		gv.setAdapter(adapter);
		return view;
	}

	/**
	 * 设置gridview.spinner的监听
	 */
	private void setListener() {
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击gridView 的item，第一次默认执行
				if (isBrand && ids != null) {
					if (!StringUtils.isEmpty(ids.get(position))) {
						getDatas(AppConstant.SORT_BRAND_HEAD
								+ ids.get(position));// 点击某个品牌---获取数据
						isBrand = false;
					}
				} else {// 连续二次点击gridview---跳转
					if (idBrand != null) {
						if (idBrand.get(position) != null) {
							startActivity(new Intent(activity,
									ProductDetailsActivity.class).putExtra(
									"url",
									AppConstant.HOME_HEAD
											+ idBrand.get(position)));
						}
					}
				}
			}
		});
	}

	/**
	 * 第二次点击某个品牌---获取数据
	 */
	private void getDatas(String str) {
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {

			private List<InfoBean> infos;

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				BaseApplication.getApplication().showToast("网络出了问题！");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				list.clear();// 成功返回数据后，清空list
				idBrand.clear();// 清空某个品牌 id
				datasbrand.clear();
				datasprice_market.clear();
				datasprice_rent.clear();
				try {
					JSONObject object = new JSONObject(arg0.result);
					String string = object.getString("message");
					if (string.equals("OK")) {
						HomeBestNewsBean bean = JSON.parseObject(
								object.getString("data"),
								HomeBestNewsBean.class);
						infos = bean.getInfos();
						for (int j = 0; j < infos.size(); j++) {
							InfoBean infoBean = infos.get(j);
							// 添加图片和ids
							list.add(infoBean.getThumb());
							idBrand.add(infoBean.getId());
							// 添加品牌名称，市场价，租价数据
							datasbrand.add(infoBean.getBrand_code() + "  "
									+ infoBean.getBrand_name_chinese());
							datasprice_market.add(infoBean.getPrice_market());
							datasprice_rent
									.add(infoBean.getPrice_rent_7()
											.split("\\.")[0]
											+ "元/7天;"
											+ infoBean.getPrice_rent_10()
													.split("\\.")[0] + "元/10天");

						}
						adapter.setDatasChange(list);// 设置图片的list
						adapter.setOtherDatasChange(datasbrand,// 设置文本和品牌内容
								datasprice_market, datasprice_rent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		// return list;// 返回集合
	}

	/**
	 * 第一次 初始化 数据---- 品牌
	 */
	private void initDatas() {

		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				BaseApplication.getApplication().showToast("网络出了问题！");
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						pb.setVisibility(View.GONE);
						tv_load03.setVisibility(View.GONE);
						tv_refreshs.setVisibility(View.VISIBLE);
					}
				}, 5000);
				tv_refreshs.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						initDatas();// 重新加载数据
						pb.setVisibility(View.VISIBLE);
						tv_load03.setVisibility(View.VISIBLE);
						tv_refreshs.setVisibility(View.GONE);
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
						JSONObject object1 = new JSONObject(object
								.getString("data"));
						List<BrandBean> brandBeans = JSON.parseArray(
								object1.getString("infos"), BrandBean.class);
						if (brandBeans != null) {
							for (int i = 0; i < brandBeans.size(); i++) {
								BrandBean bean = brandBeans.get(i);
								if (bean != null) {
									list.add(bean.getThumb());
									tempBrand.add(bean.getThumb());
									ids.add(bean.getId());// 存储 品牌的id
									adapter.setDatasChange(list);
								}
							}
						}
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
	private void initView(final View view, LayoutInflater inflater) {
		gv = (GridView) view.findViewById(R.id.sort_grid);
		pb = (ProgressBar) view.findViewById(R.id.pb_home_sort);
		tv_load03 = (TextView) view.findViewById(R.id.tv_load03);
		btprice = (TextView) view.findViewById(R.id.sortprice);
		btprice.setOnClickListener(this);
		btstyle = (TextView) view.findViewById(R.id.sortstyle);
		btstyle.setOnClickListener(this);
		view.findViewById(R.id.vi01).setOnClickListener(this);
		view.findViewById(R.id.vi02).setOnClickListener(this);
		view.findViewById(R.id.tv_search).setOnClickListener(this);
		tv_search = (TextView) view.findViewById(R.id.tv_search);
		tv_refreshs = (TextView) view.findViewById(R.id.tv_refreshs);
		brand = (TextView) view.findViewById(R.id.brand);
		brand.setOnClickListener(this);
		list = new ArrayList<String>();
		editText = (EditText) view.findViewById(R.id.et_search);
		// Android EditText 设置键盘 搜索，回车
		editorSendAction(editText);
		// 点击屏幕空白处隐藏软键盘
		clickBlankHideSoftInput(view);

	}

	/**
	 * 点击---弹出下拉列表
	 */

	protected void showPopWindow(int layout, int id, String[] sort, String tag,
			TextView btsort) {
		View vPopWindow = inflater.inflate(layout, null);
		LinearLayout llsort = (LinearLayout) vPopWindow.findViewById(id);
		for (int i = 0; i < sort.length; i++) {
			TextView child = new TextView(activity);
			child.setText(sort[i]);
			child.setGravity(Gravity.CENTER);
			child.setPadding(0, 8, 0, 8);
			if (i % 2 == 0) {
				child.setBackgroundColor(COLOR);
			}
			llsort.addView(child, Utils.getScreenWidth(activity) / 3 - 15,
					LayoutParams.WRAP_CONTENT);
			if (tag.equals("price")) {
				child.setTag(i);
				llsort.findViewWithTag(i).setOnClickListener(this);
			}
			if (tag.equals("style")) {
				child.setTag(i + 5);
				llsort.findViewWithTag(i + 5).setOnClickListener(this);
			}

		}
		popWindow = new PopupWindow(vPopWindow,
				Utils.getScreenWidth(activity) / 3 - 15,
				LayoutParams.WRAP_CONTENT, true);
		// 设置背景和点击其他地方消失
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setOutsideTouchable(true);
		popWindow.showAsDropDown(btsort);
	}

	/**
	 * 点击前设置默认颜色和内容
	 */
	public void setDefaultColor() {
		brand.setTextColor(COLORs);
		btprice.setTextColor(COLORs);
		btstyle.setTextColor(COLORs);
	}

	/**
	 * 设置点击标题的颜色
	 */
	public void setColor(int brands, int prices, int styles) {
		brand.setTextColor(brands);
		btprice.setTextColor(prices);
		btstyle.setTextColor(styles);
	}

	/**
	 * 点击事件的处理----一：通过ID; 二：通过tag
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		// 设置默认颜色
		setDefaultColor();
		switch (id) {
		// 点击 搜索按钮
		case R.id.tv_search:
			if (!StringUtils.isEmpty(editText.getText().toString())) {
				startActivity(new Intent(activity, SearchActivity.class)
						.putExtra("keyword", editText.getText().toString()));
				// 隐藏键盘
				hideSoftInput();
			}
			break;
		// 点击款式，价格文字
		case R.id.sortprice:
			showPopWindow(R.layout.popwindow_price, R.id.popwindowprice, price,
					"price", btprice);
			break;
		case R.id.sortstyle:
			showPopWindow(R.layout.popwindow_style, R.id.popWindowstyle, style,
					"style", btstyle);
			break;
		// 点击下拉箭头
		case R.id.vi01:
			showPopWindow(R.layout.popwindow_price, R.id.popwindowprice, price,
					"price", btprice);
			break;
		case R.id.vi02:
			showPopWindow(R.layout.popwindow_style, R.id.popWindowstyle, style,
					"style", btstyle);
			break;
		case R.id.brand:
			// 点击了 品牌 置为true
			isBrand = true;
			// 清空第二次加载的数据
			idBrand.clear();
			datasbrand.clear();
			datasprice_market.clear();
			datasprice_rent.clear();
			if (tempBrand.size() > 0) {
				adapter.setDatasChange(tempBrand);
			} else {
				// 再第一次加载
				initDatas();
			}
			setColor(Color.RED, COLORs, COLORs);
			btprice.setText("价格");
			btstyle.setText("款式");
			break;
		}
		/**
		 * 价格: 0表示99元；1表示199元；2表示特级；
		 */
		if (v.getTag() != null) {
			int tag = (Integer) v.getTag();
			switch (tag) {
			case 0:
				getDatas(AppConstant.SORT_PRICE_HEAD + "99");
				isBrand = false;
				btprice.setText(price[tag]);
				popWindow.dismiss();
				break;
			case 1:
				getDatas(AppConstant.SORT_PRICE_HEAD + "199");
				isBrand = false;
				btprice.setText(price[tag]);
				popWindow.dismiss();
				break;
			case 2:
				getDatas(AppConstant.SORT_PRICE_ALL);
				btprice.setText(price[tag]);
				popWindow.dismiss();
				break;
			/**
			 * 款式 --下拉点击
			 */
			case 5:
				getDatas(AppConstant.SORT_STYLE_HEAD + 1);
				btstyle.setText(style[tag - 5]);
				popWindow.dismiss();
				break;
			case 6:
				getDatas(AppConstant.SORT_STYLE_HEAD + 2);
				btstyle.setText(style[tag - 5]);
				popWindow.dismiss();
				break;
			case 7:
				getDatas(AppConstant.SORT_STYLE_HEAD + 3);
				btstyle.setText(style[tag - 5]);
				popWindow.dismiss();
				break;
			case 8:
				getDatas(AppConstant.SORT_STYLE_HEAD + 4);
				btstyle.setText(style[tag - 5]);
				popWindow.dismiss();
				break;
			case 9:
				getDatas(AppConstant.SORT_STYLE_HEAD + 5);
				btstyle.setText(style[tag - 5]);
				popWindow.dismiss();
				break;
			case 10:
				getDatas(AppConstant.SORT_STYLE_HEAD + 6);
				btstyle.setText(style[tag - 5]);
				popWindow.dismiss();
				break;
			case 11:
				getDatas(AppConstant.SORT_STYLE_HEAD + 7);
				btstyle.setText(style[tag - 5]);
				popWindow.dismiss();
				break;

			}
			if (tag > 4) {
				setColor(COLORs, COLORs, Color.RED);
			} else {
				setColor(COLORs, Color.RED, COLORs);
			}
		}
	}

}
