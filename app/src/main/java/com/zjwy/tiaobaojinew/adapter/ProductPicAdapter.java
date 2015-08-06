package com.zjwy.tiaobaojinew.adapter;

import java.io.ByteArrayOutputStream;
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

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.activity.BigPicActivity;
import com.zjwy.tiaobaojinew.utils.Utils;

public class ProductPicAdapter extends AppBaseAdapter<String> {

	private BitmapUtils bitmapUtils;

	public ProductPicAdapter(List<String> list, Context context) {
		super(list, context);
		bitmapUtils = new BitmapUtils(context);
		notifyDataSetChanged();
	}

	@Override
	public View createView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_items_iv01, parent,
					false);
			holder = new Holder();
			Utils.setListPicParamLayout((Activity) context, convertView);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		bitmapUtils.configDefaultBitmapMaxSize(300, 200);// 设置图片大小
		bitmapUtils.configDefaultCacheExpiry(604800000);// 7天
		// bitmapUtils.display(holder.pager, list.get(position));
		bitmapUtils.display(holder.pager, list.get(position),
				new BitmapLoadCallBack<ImageView>() {

					@Override
					public void onLoadCompleted(ImageView container,
							String uri, final Bitmap bitmap,
							BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
						holder.pager.setImageBitmap(bitmap);
						holder.pager.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {// 获得点击的图片--用数组传（大图）
								Intent intent = new Intent(context,
										BigPicActivity.class);
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								if (bitmap != null) {
									bitmap.compress(Bitmap.CompressFormat.PNG,
											100, baos);
									byte[] array = baos.toByteArray();
									intent.putExtra("image", array);
									context.startActivity(intent);
								}
							}
						});
					}

					@Override
					public void onLoadFailed(ImageView arg0, String arg1,
							Drawable arg2) {

					}
				});
		return convertView;
	}

	private static class Holder {
		@ViewInject(R.id.pager)
		private ImageView pager;
	}
}
