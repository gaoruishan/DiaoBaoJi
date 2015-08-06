package com.zjwy.tiaobaojinew.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.activity.ProductDetailsActivity;
import com.zjwy.tiaobaojinew.bean.HomeBestNewsBean.InfoBean;
import com.zjwy.tiaobaojinew.utils.StringUtils;
import com.zjwy.tiaobaojinew.utils.Utils;

public class HomeBestListViewAdapter extends AppBaseAdapter<InfoBean> {

	private BitmapUtils bitmapUtils;
	private ProgressBar pb;
	private TextView load;

	public HomeBestListViewAdapter(List<InfoBean> list, Context context,
			ProgressBar pb, TextView load) {
		super(list, context);
		bitmapUtils = new BitmapUtils(context);
		this.pb = pb;
		this.load = load;
		notifyDataSetChanged();
	}

	@Override
	public View createView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_items_iv_best, parent,
					false);
			holder = new Holder();
			// 设置item的高度
			Utils.setListViewParamLayout((Activity) context, convertView);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final InfoBean bean = list.get(position);
		bitmapUtils.configDefaultLoadingImage(R.drawable.logo10);
		bitmapUtils.configDefaultBitmapMaxSize(650, 550);// 设置图片大小
		bitmapUtils.configDefaultCacheExpiry(604800000);// 7天
		// bitmapUtils.display(holder.iv, list.get(position).getThumb_best());
		String thumb_best = bean.getThumb_best();
		if (thumb_best.equals("")) {
			thumb_best = bean.getThumb();
		}
		bitmapUtils.display(holder.iv, thumb_best,
				new BitmapLoadCallBack<ImageView>() {

					@Override
					public void onLoadCompleted(ImageView arg0, String arg1,
							Bitmap bitmap, BitmapDisplayConfig arg3,
							BitmapLoadFrom arg4) {
						holder.iv.setImageBitmap(bitmap);// 设置图片
						pb.setVisibility(View.GONE);
						load.setVisibility(View.GONE);
					}

					@Override
					public void onLoadFailed(ImageView arg0, String arg1,
							Drawable arg2) {
					}
				});
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtils.isEmpty(bean.getId())) {
					context.startActivity(new Intent(context,
							ProductDetailsActivity.class).putExtra("id",
							bean.getId()));
				}
			}
		});
		return convertView;
	}

	private static class Holder {
		@ViewInject(R.id.list_iv_best)
		private ImageView iv;
	}
}
