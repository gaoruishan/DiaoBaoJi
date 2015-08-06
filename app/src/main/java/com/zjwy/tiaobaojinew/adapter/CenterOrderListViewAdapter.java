package com.zjwy.tiaobaojinew.adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.activity.PaymentActivity01;
import com.zjwy.tiaobaojinew.bean.OrderBean;
import com.zjwy.tiaobaojinew.bean.OrderBean.GoodInfosBean;
import com.zjwy.tiaobaojinew.utils.AppConstant;
import com.zjwy.tiaobaojinew.widget.ListViewForScrollView;

public class CenterOrderListViewAdapter extends AppBaseAdapter<OrderBean> {

	private BitmapUtils bitmapUtils;
	private Date date;

	public CenterOrderListViewAdapter(List<OrderBean> list, Context context) {
		super(list, context);
		bitmapUtils = new BitmapUtils(context);
		notifyDataSetChanged();
	}

	@Override
	public View createView(final int position, View convertView,
			ViewGroup parent) {
		Holder holder;
		convertView = inflater.inflate(R.layout.list_items_myorder, parent,
				false);
		holder = new Holder();
		ViewUtils.inject(holder, convertView);
		// bitmapUtils.configDefaultBitmapMaxSize(200, 300);//设置图片大小
		bitmapUtils.configDefaultLoadingImage(R.drawable.logo12);
		final OrderBean beanInfos = list.get(position);
		String status = beanInfos.getStatus();
		if (status.equals("0")) {
			holder.tv_order_state.setText("未付款");
		} else if (status.equals("1")) {
			holder.tv_order_state.setText("交易完成");

		}
		final String orderid = beanInfos.getOrderid();
		holder.tv_order_id.setText("订单编号：" + orderid);
		int parseIntstart = Integer.parseInt(beanInfos.getRent_start());
		date = new Date((long) parseIntstart * 1000);
		final List<GoodInfosBean> beans = beanInfos.getGoodInfos();
		CenterOrderListViewChildsAdapter adapter = new CenterOrderListViewChildsAdapter(
				beans);
		holder.lv_myorder.setAdapter(adapter);
		// 继续支付
		holder.bt_continue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(context,
						PaymentActivity01.class).putExtra("payUrl",
						AppConstant.GOPAYINFOS + orderid));
			}
		});
		// 评论
		holder.bt_order_comit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseApplication.getApplication().showToast("点击评论了");
			}
		});
		// 删除订单
		holder.bt_order_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						HttpGet httpGet = new HttpGet(
								AppConstant.DELETEPAYINFOS + orderid);
						try {
							HttpResponse response = BaseApplication
									.getHttpClient().execute(httpGet);
							if (response.getStatusLine().getStatusCode() == 200) {
								String result = EntityUtils.toString(
										response.getEntity(), "UTF-8");
								JSONObject jsObj = new JSONObject(result);
								if ("OK".equals(jsObj.getString("message"))) {
									System.out.println("删除了");
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
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	private static class Holder {
		@ViewInject(R.id.tv_order_id)
		private TextView tv_order_id;
		@ViewInject(R.id.tv_order_state)
		private TextView tv_order_state;
		@ViewInject(R.id.lv_myorder)
		private ListViewForScrollView lv_myorder;
		@ViewInject(R.id.bt_continue)
		private Button bt_continue;
		@ViewInject(R.id.bt_order_cancel)
		private Button bt_order_cancel;
		@ViewInject(R.id.bt_order_comit)
		private Button bt_order_comit;
		@ViewInject(R.id.ll_item_myorder)
		private LinearLayout ll_item_myorder;
	}

	private class CenterOrderListViewChildsAdapter extends BaseAdapter {
		public List<GoodInfosBean> listBeans;

		public CenterOrderListViewChildsAdapter(List<GoodInfosBean> list) {
			this.listBeans = list;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return listBeans != null && !listBeans.isEmpty() ? listBeans.size()
					: 0;
		}

		@Override
		public Object getItem(int position) {
			return listBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final HolderChild holderChild;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_items_myorder_chird, parent, false);
				holderChild = new HolderChild();
				ViewUtils.inject(holderChild, convertView);
				convertView.setTag(holderChild);
			} else {
				holderChild = (HolderChild) convertView.getTag();
			}
			final GoodInfosBean infosBean = listBeans.get(position);
			bitmapUtils
					.display(holderChild.list_iv_order, infosBean.getThumb());
			holderChild.tv_order_name.setText("商品名称:" + infosBean.getTitle());
			holderChild.tv_order_money.setText("订单金额:"
					+ infosBean.getRent_price());
			holderChild.tv_order_renttime.setText("租期:"
					+ infosBean.getRent_type());
			holderChild.tv_order_pay.setText("下单时间:"
					+ new SimpleDateFormat("yy年MM月dd日HH时").format(date));

			return convertView;
		}
	}

	static class HolderChild {
		@ViewInject(R.id.list_iv_order)
		private ImageView list_iv_order;
		@ViewInject(R.id.tv_myorder_name)
		private TextView tv_order_name;
		@ViewInject(R.id.tv_myorder_money)
		private TextView tv_order_money;
		@ViewInject(R.id.tv_myorder_renttime)
		private TextView tv_order_renttime;
		@ViewInject(R.id.tv_order_pay)
		private TextView tv_order_pay;

	}
}
