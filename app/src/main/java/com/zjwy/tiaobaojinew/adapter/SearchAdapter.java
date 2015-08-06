package com.zjwy.tiaobaojinew.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.bean.HomeBestNewsBean.InfoBean;
import com.zjwy.tiaobaojinew.utils.Utils;

/**
 * 分类 pager的gridView 适配器
 * 
 * @author grs
 * 
 * @param <T>
 */
public class SearchAdapter extends AppBaseAdapter<InfoBean> {

	private BitmapUtils bitmapUtils;

	public SearchAdapter(List<InfoBean> list, Context context) {
		super(list, context);
		bitmapUtils = new BitmapUtils(context);
		notifyDataSetChanged();
	}

	static class Holder {
		@ViewInject(R.id.iv_item)
		private ImageView iv_item;
		@ViewInject(R.id.tv_rmb)
		private TextView tv_rmb;
		@ViewInject(R.id.tv_rent_price)
		private TextView tv_rent_price;
		@ViewInject(R.id.tv_brand)
		private TextView tv_brand;
	}

	@Override
	public View createView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_items_searchs, parent, false);
			holder = new Holder();
			// 设置item的高度
			Utils.setGridViewParamLayout((Activity) context, convertView);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final InfoBean bean = list.get(position);
		holder.tv_brand.setText(bean.getBrand_code() + "  "
				+ bean.getBrand_name_chinese());
		holder.tv_rmb.setText("市场价  RMB " + bean.getPrice_market() + "元");
		holder.tv_rent_price.setText("租价 RMB "
				+ bean.getPrice_rent_7().split("\\.")[0] + "元/7天;"
				+ bean.getPrice_rent_10().split("\\.")[0] + "元/10天");
		bitmapUtils.display(holder.iv_item, bean.getThumb());

		return convertView;
	}
}
