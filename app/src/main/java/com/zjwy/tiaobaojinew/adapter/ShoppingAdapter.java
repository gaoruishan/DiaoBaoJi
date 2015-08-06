package com.zjwy.tiaobaojinew.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.bean.ProductContentBean;
import com.zjwy.tiaobaojinew.utils.AppConstant;

public class ShoppingAdapter extends AppBaseAdapter<ProductContentBean> {
	private BitmapUtils bitmapUtils;
	private ArrayList<CheckBox> temp = new ArrayList<CheckBox>();
	private HashSet<ProductContentBean> tempbeans = new HashSet<ProductContentBean>();
	private ArrayList<ProductContentBean> beans = new ArrayList<ProductContentBean>();
	private TextView total;
	private TextView sum;
	private int count;

	public ShoppingAdapter(BitmapUtils bitmapUtils,
			List<ProductContentBean> list, Context context, TextView total,
			TextView sum) {
		super(list, context);
		this.bitmapUtils = bitmapUtils;
		this.total = total;
		this.sum = sum;
	}

	/**
	 * 获取被选中的bean
	 */
	public ArrayList<ProductContentBean> getBeans() {
		if (isAll) {// 全选时
			beans = (ArrayList<ProductContentBean>) list;
		} else {
			beans.clear();
			Iterator<ProductContentBean> iterator = tempbeans.iterator();
			while (iterator.hasNext()) {
				ProductContentBean bean = iterator.next();
				beans.add(bean);
			}
		}
		return beans;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			BaseApplication.getApplication().showToast(
					context.getString(R.string.deleted));
		};
	};

	@Override
	public View createView(final int position, View convertView,
			ViewGroup parent) {
		final Holder holder;
		View view = inflater.inflate(R.layout.list_items_shopping, parent,
				false);
		holder = new Holder();
		ViewUtils.inject(holder, view);
		temp.add(holder.cb_shopping);
		final ProductContentBean bean = list.get(position);
		if (bean.getNum_iid().equals("Check")) {
			holder.cb_shopping.setChecked(true);
		} else {
			holder.cb_shopping.setChecked(false);
		}
		bitmapUtils.display(holder.list_iv_shopping, bean.getThumb());
		bitmapUtils.configDefaultCacheExpiry(604800000);// 7天
		holder.tv_shopping_name.setText("商品名称：" + bean.getBrand_code());
		holder.tv_order_title.setText("标题：" + bean.getTitle());
		holder.tv_order_money.setText("款式："
				+ bean.getSearch_field().split(" ")[0]);
		holder.tv_order_color.setText("颜色：" + bean.getColor());
		holder.tv_order_size.setText("大小：" + bean.getSize());
		holder.tv_shopping_price.setText("￥ "
				+ bean.getPrice_rent_7().split("\\.")[0] + "元");
		// 选中商品
		holder.cb_shopping
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							count += Integer.parseInt(bean.getPrice_rent_7()
									.split("\\.")[0]);
							bean.setNum_iid("Check");// 设置标示
							tempbeans.add(bean);
							sum.setText("总共选择了：" + (++num));
						} else {
							count -= Integer.parseInt(bean.getPrice_rent_7()
									.split("\\.")[0]);
							bean.setNum_iid("noCheck");
							tempbeans.remove(bean);
							sum.setText("总共选择了：" + (--num));
						}
						total.setText("(约计：￥" + count + ")");
						// notifyDataSetChanged();
					}
				});
		// 编辑
		holder.tv_shopping_set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isEdit) {
					holder.ll_edits.setVisibility(View.VISIBLE);
					holder.tv_shopping_set.setText("完成");
					isEdit = false;
				} else {
					holder.ll_edits.setVisibility(View.GONE);
					holder.tv_shopping_set.setText("编辑");
					isEdit = true;
				}
			}
		});
		// 删除
		holder.bt_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						HttpGet httpGet = new HttpGet(
								AppConstant.DELETE_SHOPPING + bean.getId());
						try {
							HttpResponse response = BaseApplication
									.getHttpClient().execute(httpGet);
							if (response.getStatusLine().getStatusCode() == 200) {
								String result = EntityUtils.toString(
										response.getEntity(), "UTF-8");
								JSONObject jsObj = new JSONObject(result);
								if ("OK".equals(jsObj.getString("message"))) {
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
				list.remove(position);
				isEdit = true;
				notifyDataSetChanged();
			}
		});
		holder.tv_jian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(context, "减", Toast.LENGTH_SHORT).show();
				holder.tv_num.setText(num-- + "");
			}
		});
		holder.tv_jias.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(context, "加", Toast.LENGTH_SHORT).show();
				holder.tv_num.setText((1 + num++) + "");

			}
		});
		return view;
	}

	private static class Holder {
		@ViewInject(R.id.cb_shopping)
		private CheckBox cb_shopping;
		@ViewInject(R.id.list_iv_shopping)
		private ImageView list_iv_shopping;
		@ViewInject(R.id.tv_shopping_price)
		private TextView tv_shopping_price;
		@ViewInject(R.id.tv_shopping_name)
		private TextView tv_shopping_name;
		@ViewInject(R.id.tv_shopping_set)
		private TextView tv_shopping_set;
		@ViewInject(R.id.tv_order_title)
		private TextView tv_order_title;
		@ViewInject(R.id.tv_order_money)
		private TextView tv_order_money;
		@ViewInject(R.id.tv_order_color)
		private TextView tv_order_color;
		@ViewInject(R.id.tv_order_size)
		private TextView tv_order_size;
		@ViewInject(R.id.ll_edits)
		private LinearLayout ll_edits;
		@ViewInject(R.id.bt_delete)
		private Button bt_delete;
		@ViewInject(R.id.tv_jian)
		private TextView tv_jian;
		@ViewInject(R.id.tv_jias)
		private TextView tv_jias;
		@ViewInject(R.id.tv_num)
		private TextView tv_num;

	}

	/**
	 * 设置全选
	 */
	public void setIsChecked(boolean b) {
		for (int j = 0; j < list.size(); j++) {
			if (b) {
				// count = 0;
				int parseInt = Integer.parseInt(list.get(j).getPrice_rent_7()
						.split("\\.")[0]);
				list.get(j).setNum_iid("Check");// 设置标示
				count += parseInt;
				total.setText("(约计：￥" + count + ")");
				sum.setText("总共选择了：" + list.size());
				isAll = true;
			} else {
				list.get(j).setNum_iid("noCheck");// 设置标示
				count = 0;
				total.setText("(约计：￥" + count + ")");
				sum.setText("总共选择了：" + 0);
			}
			notifyDataSetChanged();
		}
	}

	private boolean isAll;
	private int num;
	private boolean isEdit = true;

}
