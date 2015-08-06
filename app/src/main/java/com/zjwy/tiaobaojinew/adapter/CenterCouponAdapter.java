package com.zjwy.tiaobaojinew.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.bean.CouponBean;

public class CenterCouponAdapter extends AppBaseAdapter<CouponBean> {

	public CenterCouponAdapter(List<CouponBean> list, Context context) {
		super(list, context);
		notifyDataSetChanged();
	}

	@Override
	public View createView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_items_coupon, parent,
					false);
			holder = new Holder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		CouponBean bean = list.get(position);
		holder.tv_coupon_order.setText("序列号：" + bean.getCoupon());
		holder.tv_coupon_money.setText("金额：" + bean.getMoney());
		int parseIntend = Integer.parseInt(bean.getEnd_time());
		int parseIntstart = Integer.parseInt(bean.getSent_time());
		int valuedate = parseIntend - parseIntstart;
		holder.tv_coupon_num
				.setText("有效期：" + valuedate / (60 * 60 * 24) + " 天");
		Date date = new Date((long) parseIntend * 1000);
		String time = new SimpleDateFormat("yy年MM月dd日HH时").format(date);
		holder.tv_coupon_time.setText("结束时间：" + time);
		long diff = System.currentTimeMillis() - (long) parseIntend * 1000;
		if (diff > 0) {
			holder.list_iv_coupon.setImageResource(R.drawable.icon_coupon_old);
			holder.tv_coupon_state.setTextColor(Color.GRAY);
			holder.tv_coupon_order.setTextColor(Color.GRAY);
			holder.tv_coupon_money.setTextColor(Color.GRAY);
			holder.tv_coupon_num.setTextColor(Color.GRAY);
			holder.tv_coupon_time.setTextColor(Color.GRAY);
			holder.tv_state.setTextColor(Color.GRAY);
		}
		if (bean.getCoupon().equals("2")) {
			holder.tv_coupon_state.setText("已使用");
		} else {
			holder.tv_coupon_state.setText("未使用");
		}
		return convertView;
	}

	static class Holder {
		@ViewInject(R.id.list_iv_coupon)
		private ImageView list_iv_coupon;
		@ViewInject(R.id.tv_coupon_order)
		private TextView tv_coupon_order;
		@ViewInject(R.id.tv_coupon_state)
		private TextView tv_coupon_state;
		@ViewInject(R.id.tv_coupon_money)
		private TextView tv_coupon_money;
		@ViewInject(R.id.tv_coupon_time)
		private TextView tv_coupon_time;
		@ViewInject(R.id.tv_coupon_num)
		private TextView tv_coupon_num;
		@ViewInject(R.id.tv_state)
		private TextView tv_state;

	}
}
