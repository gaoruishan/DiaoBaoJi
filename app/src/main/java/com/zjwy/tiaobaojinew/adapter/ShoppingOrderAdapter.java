package com.zjwy.tiaobaojinew.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.bean.ProductContentBean;

//import android.widget.RadioGroup.OnCheckedChangeListener;

public class ShoppingOrderAdapter extends AppBaseAdapter<ProductContentBean> {
	private BitmapUtils bitmapUtils;
	private TextView total;

	public ShoppingOrderAdapter(List<ProductContentBean> list,
			final Context context, TextView total) {
		super(list, context);
		bitmapUtils = new BitmapUtils(context);
		this.notifyDataSetChanged();
		this.total = total;
	}

	@Override
	public View createView(final int position, View convertView,
			ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_items_order, parent,
					false);
			holder = new Holder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		// bitmapUtils.configDefaultBitmapMaxSize(200, 300);//设置图片大小
		final ProductContentBean bean = list.get(position);
		bitmapUtils.display(holder.list_iv_order, bean.getThumb());
		holder.tv_order_name.setText("款式："
				+ bean.getSearch_field().split(" ")[0]);
		String brand_code = bean.getBrand_code();
		if (brand_code != null) {
			holder.tv_order_brand.setText("品牌：" + brand_code);
		}
		holder.rado1.setText(bean.getPrice_rent_7().split("\\.")[0] + "元/7天");

		holder.radio2
				.setText(bean.getPrice_rent_10().split("\\.")[0] + "元/10天");
		holder.rado1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				strUrl += "&good_id[" + bean.getId() + "]=" + "7";
				if (isChecked) {
					money += Integer.parseInt(bean.getPrice_rent_7().split(
							"\\.")[0]);
				} else {
					money -= Integer.parseInt(bean.getPrice_rent_7().split(
							"\\.")[0]);
				}
				total.setText("￥" + money);
			}
		});
		holder.radio2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				strUrl += "&good_id[" + bean.getId() + "]=" + "10";
				if (isChecked) {
					money += Integer.parseInt(bean.getPrice_rent_10().split(
							"\\.")[0]);
				} else {
					money -= Integer.parseInt(bean.getPrice_rent_10().split(
							"\\.")[0]);
				}
				total.setText("￥" + money);

			}
		});

		return convertView;
	}

	private String strUrl = "";
	private int money;

	public String getUrlString() {
		return strUrl + "&money=" + money;
	}

	private static class Holder {
		@ViewInject(R.id.tv_order_name)
		private TextView tv_order_name;
		@ViewInject(R.id.tv_order_brand)
		private TextView tv_order_brand;
		@ViewInject(R.id.list_iv_order01)
		private ImageView list_iv_order;
		@ViewInject(R.id.rado1)
		private RadioButton rado1;
		@ViewInject(R.id.radio2)
		private RadioButton radio2;
		@ViewInject(R.id.radioGroup01)
		private RadioGroup radioGroup01;
	}

}
