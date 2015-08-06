package com.zjwy.tiaobaojinew.utils;

import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.zjwy.tiaobaojinew.netstate.NetWorkUtil;
import com.zjwy.tiaobaojinew.widget.CustomDialog;

/**
 * @类 说 明:
 * @version 1.0
 * @创建时间：2014-8-5 上午10:30:18
 * 
 */
public class Utils {
	public static boolean checkNetwork(final Activity context) {
		boolean netSataus = NetWorkUtil.isNetworkAvailable(context);
		if (!netSataus) {
			CustomDialog.Builder builder = new CustomDialog.Builder(context);
			builder.setTitle("网络设置").setMessage("当前网络不可用!");
			builder.setPositiveButton("移动网络",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									Settings.ACTION_DATA_ROAMING_SETTINGS);
							context.startActivityForResult(intent, 0);
							dialog.dismiss();
						}
					}).setNegativeButton("WIFI设置 ",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									Settings.ACTION_WIFI_SETTINGS);
							context.startActivityForResult(intent, 0);
							dialog.dismiss();
						}

					});
			CustomDialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					context.finish();
					return true;
				}
			});
			dialog.show();

		} else {
		}
		return netSataus;
	}

	/**
	 * @说 明:判断程序是否第一次运行
	 */
	public static boolean isFirstRun(Context context) {
		boolean isFirstRun = false;
		SharedPreferences sp = context.getSharedPreferences("isFirst",
				Context.MODE_PRIVATE);
		int version = sp.getInt("version", 0);
		int appVersion = getVersionCode(context);
		if (version != appVersion) {
			sp.edit().putInt("version", appVersion).commit();
			isFirstRun = true;
		}
		return isFirstRun;
	}

	/**
	 * 获取版本号(内部识别号)
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取屏幕的高度
	 */
	public static int getScreenHeight(Activity context) {
		DisplayMetrics outMetrics = getDisplayMetrics(context);
		return outMetrics.heightPixels;
	}

	/**
	 * 获取屏幕的宽度
	 */
	public static int getScreenWidth(Activity context) {
		DisplayMetrics outMetrics = getDisplayMetrics(context);
		return outMetrics.widthPixels;

	}

	private static DisplayMetrics getDisplayMetrics(Activity context) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics;
	}

	/**
	 * 设置ListView的item的高度
	 */
	public static void setListViewParamLayout(Activity context, View convertView) {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				getScreenHeight(context) / 3);
		convertView.setLayoutParams(lp);
	}

	/**
	 * 设置GridView的item的高度
	 */
	public static void setGridViewParamLayout(Activity context, View convertView) {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				getScreenHeight(context) / 4);
		convertView.setLayoutParams(lp);
	}

	/**
	 * 设置ListPic的item的高度
	 */
	public static void setListPicParamLayout(Activity context, View convertView) {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				getScreenWidth(context) - 100);
		convertView.setLayoutParams(lp);
	}

	/**
	 * RelativeLayout设置宽高，子viewPager视图布局
	 */
	public static void setViewPagerParamLayout(Activity context, View view) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				getScreenWidth(context) - 50);
		view.setLayoutParams(lp);
	}

	/**
	 * 获取SDCard的目录路径功能
	 */
	public static String getSDCardPath() {
		String SDCardPath = null;
		// 判断SDCard是否存在
		boolean IsSDcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (IsSDcardExist) {
			SDCardPath = Environment.getExternalStorageDirectory().toString();// SD卡的路径为:
																				// /mnt/sdcard
		}
		return SDCardPath;
	}

	/**
	 * 进行二次处理图片操作
	 */
	public static Bitmap loadBigBitmap(String path, int width, int height) {
		Options options = new Options();
		// 创建一个选项，设置加载图片返回null(只加载图片的宽和高，不会真正的加载图片)
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int outHeight = options.outHeight;
		int outWidth = options.outWidth;
		int size = 1;// 获取缩放比例
		while (outHeight / height > size || outWidth / width > size) {
			size++;
		}
		options.inSampleSize = size;// 设置缩放比例
		options.inJustDecodeBounds = false;// 第二次加载获得图片
		return BitmapFactory.decodeFile(path, options);

	}

	private static int QR_WIDTH = 200, QR_HEIGHT = 200;

	/**
	 * @方法功能说明: 生成二维码图片,实际使用时要初始化sweepIV,不然会报空指针错误
	 * @参数: @param url 要转换的地址或字符串,可以是中文
	 */

	// 要转换的地址或字符串,可以是中文
	public static Bitmap createQRImage(String url) {
		Bitmap bitmap = null;
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
		} catch (WriterException e) {
			e.printStackTrace();
		}

		return bitmap;
	}
}
