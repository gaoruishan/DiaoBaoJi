package com.zjwy.tiaobaojinew.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.zjwy.tiaobaojinew.activity.PackageActivity;
import com.zjwy.tiaobaojinew.utils.Utils;

/**
 * 分类 pager的gridView 适配器
 * 
 * @author grs
 * 
 * @param <T>
 */
public class SortGridViewAdapter extends BaseAdapter {

	private static BitmapUtils bitmapUtils;
	private List<String> list;
	private List<String> datasbrand = new ArrayList<String>();
	private List<String> datasprice_market = new ArrayList<String>();
	private List<String> datasprice_rent = new ArrayList<String>();
	private Context context;
	private ProgressBar pb;
	private TextView load;

	public SortGridViewAdapter(Context context, ProgressBar pb, TextView load) {
		this.context = context;
		this.pb = pb;
		this.load = load;
		bitmapUtils = new BitmapUtils(context);
	}

	public void setDatasChange(List<String> lists) {
		this.list = lists;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list != null && !list.isEmpty() ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.gv_item, null);
			holder = new Holder();
			// 设置item的高度
			Utils.setGridViewParamLayout((Activity) context, convertView);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final String url = (String) list.get(position);// 图片的url
		if (datasprice_rent.size() > 0 && datasbrand.get(position) != null) {
			otherDatas(holder, View.VISIBLE);
			holder.tv_brand.setText(datasbrand.get(position));
			holder.tv_rmb.setText("市场价  RMB " + datasprice_market.get(position)
					+ "元");
			holder.tv_rent_price.setText("租价 RMB "
					+ datasprice_rent.get(position));
			holder.tv_try.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, PackageActivity.class);
					intent.putExtra("imgUrl", url);
					context.startActivity(intent);
				}
			});
		} else {
			otherDatas(holder, View.GONE);
		}
		// bitmapUtils.configDefaultBitmapMaxSize(550, 450);// 设置图片大小
		bitmapUtils.configDefaultLoadingImage(R.drawable.logo12);
		bitmapUtils.configDefaultCacheExpiry(604800000);// 7天
		bitmapUtils.display(holder.iView, url,
				new BitmapLoadCallBack<ImageView>() {

					@Override
					public void onLoadCompleted(ImageView arg0, String arg1,
							Bitmap bitmap, BitmapDisplayConfig arg3,
							BitmapLoadFrom arg4) {
						pb.setVisibility(View.GONE);
						load.setVisibility(View.GONE);
						holder.iView.setImageBitmap(bitmap);// 设置图片
					}

					@Override
					public void onLoadFailed(ImageView arg0, String arg1,
							Drawable arg2) {
					}

				});

		return convertView;
	}

	/**
	 * 其他的显示视图
	 */
	private void otherDatas(Holder holder, int isVisible) {
		holder.tv_brand.setVisibility(isVisible);
		holder.tv_rent_price.setVisibility(isVisible);
		holder.tv_rmb.setVisibility(isVisible);
		holder.tv_try.setVisibility(isVisible);
	}

	/**
	 * 其他的显示数据
	 */
	public void setOtherDatasChange(List<String> datasbrand,
			List<String> datasprice_market, List<String> datasprice_rent) {
		this.datasbrand = datasbrand;
		this.datasprice_market = datasprice_market;
		this.datasprice_rent = datasprice_rent;
		notifyDataSetChanged();
	}

	private static class Holder {
		@ViewInject(R.id.iv_item)
		private ImageView iView;
		@ViewInject(R.id.tv_rmb)
		private TextView tv_rmb;
		@ViewInject(R.id.tv_try)
		private TextView tv_try;
		@ViewInject(R.id.tv_rent_price)
		private TextView tv_rent_price;
		@ViewInject(R.id.tv_brand)
		private TextView tv_brand;
	}
}
