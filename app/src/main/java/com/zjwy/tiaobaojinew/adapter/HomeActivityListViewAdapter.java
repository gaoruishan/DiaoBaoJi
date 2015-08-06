package com.zjwy.tiaobaojinew.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.activity.ProductDetailsActivity;
import com.zjwy.tiaobaojinew.bean.HomeActivityBean.BeanChild;
import com.zjwy.tiaobaojinew.utils.StringUtils;
import com.zjwy.tiaobaojinew.utils.Utils;

public class HomeActivityListViewAdapter extends AppBaseAdapter<BeanChild> {

	private BitmapUtils bitmapUtils;

	public HomeActivityListViewAdapter(List<BeanChild> list, Context context,
			BitmapUtils bitmapUtils) {
		super(list, context);
		this.bitmapUtils = bitmapUtils;
		notifyDataSetChanged();
	}

	@Override
	public View createView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_items_iv_activty,
					parent, false);
			holder = new Holder();
			// 设置item的高度
			Utils.setListViewParamLayout((Activity) context, convertView);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final BeanChild beanChild = list.get(position);
		// bitmapUtils.configDefaultLoadingImage(R.drawable.logo1);
		// bitmapUtils.configDefaultBitmapMaxSize(650, 550);// 设置图片大小
		bitmapUtils.configDefaultCacheExpiry(604800000);// 7天
		bitmapUtils.display(holder.iv, beanChild.getThumb());
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtils.isEmpty(beanChild.getGood_id())) {
					context.startActivity(new Intent(context,
							ProductDetailsActivity.class).putExtra("id",
							beanChild.getGood_id()));
				}
			}
		});
		return convertView;
	}

	private static class Holder {
		@ViewInject(R.id.list_iv_activity)
		private ImageView iv;
	}
}
