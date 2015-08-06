package com.zjwy.tiaobaojinew.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.FloatMath;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.utils.LightnessControl;
import com.zjwy.tiaobaojinew.utils.MusicService;
import com.zjwy.tiaobaojinew.utils.Utils;

public class PackageActivity extends BaseActivity implements OnClickListener {

	private ImageView imgPerson, bg_pic_ground;
	private ImageView imgClothing;
	private LinearLayout layerChange, buttonCamera, buttonSave, linerPerson,
			layerClothes, buttonClothing, buttonShare, layerModuleClothes;
	private boolean isFrist = true;
	private boolean isFrist01 = true;
	private boolean isFrist03 = true;
	private boolean isFrist0 = true;
	private boolean isFrist00 = true;
	private boolean play = true;
	private File f;
	private SeekBar sbLD;
	private SeekBar sbDBD;
	private int imgHeight;
	private int imgWidth;
	private Bitmap bitmap;
	private TouchListener touchListener = new TouchListener();
	private AlertDialog dialog;
	private int picId = 0;
	private Button bg_mus;

	@Override
	public void setView() {
		setContentView(R.layout.layout_package);
	}

	@Override
	public void initData() {
		String imgUrl = getIntent().getStringExtra("imgUrl");
		new BitmapUtils(this).display(imgClothing, imgUrl);
	}

	@Override
	public void initView() {
		Button btnChangeLeft = (Button) this.findViewById(R.id.btnChangeLeft);
		Button btnReturn = (Button) this.findViewById(R.id.btnReturn);
		Button btnChangeRight = (Button) this.findViewById(R.id.btnChangeRight);
		Button btnChangeHigh = (Button) this.findViewById(R.id.btnChangeHigh);
		Button btnChangeShort = (Button) this.findViewById(R.id.btnChangeShort);
		Button btnChangeFat = (Button) this.findViewById(R.id.btnChangeFat);
		Button btnChangeThin = (Button) this.findViewById(R.id.btnChangeThin);
		Button imgFavor = (Button) this.findViewById(R.id.imgFavor);
		imgFavor.setOnClickListener(this);
		bg_mus = (Button) this.findViewById(R.id.bg_mus);
		bg_mus.setOnClickListener(this);
		Button bg_pic = (Button) this.findViewById(R.id.bg_pic);
		bg_pic.setOnClickListener(this);
		btnChangeLeft.setOnClickListener(this);
		btnChangeRight.setOnClickListener(this);
		btnChangeHigh.setOnClickListener(this);
		btnChangeShort.setOnClickListener(this);
		btnChangeFat.setOnClickListener(this);
		btnChangeThin.setOnClickListener(this);
		btnReturn.setOnClickListener(this);

		sbLD = (SeekBar) this.findViewById(R.id.sbLD);
		sbDBD = (SeekBar) this.findViewById(R.id.sbDBD);
		sbLD.setProgress(LightnessControl.GetLightness(this));
		LinearLayout buttonChange = (LinearLayout) this
				.findViewById(R.id.buttonChange);
		layerChange = (LinearLayout) this.findViewById(R.id.layerChange);
		buttonChange.setOnClickListener(this);
		buttonCamera = (LinearLayout) this.findViewById(R.id.buttonCamera);
		buttonCamera.setOnClickListener(this);
		buttonSave = (LinearLayout) this.findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(this);
		buttonClothing = (LinearLayout) this.findViewById(R.id.buttonClothing);
		buttonClothing.setOnClickListener(this);
		buttonShare = (LinearLayout) this.findViewById(R.id.buttonShare);
		buttonShare.setOnClickListener(this);
		layerModuleClothes = (LinearLayout) this
				.findViewById(R.id.layerModuleClothes);

		layerClothes = (LinearLayout) this.findViewById(R.id.layerClothes);
		imgPerson = (ImageView) this.findViewById(R.id.imgPerson);
		bg_pic_ground = (ImageView) this.findViewById(R.id.bg_pic_ground);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.c6);
		imgPerson.setImageBitmap(bitmap);
		imgHeight = bitmap.getHeight();
		imgWidth = bitmap.getWidth();
		imgClothing = (ImageView) this.findViewById(R.id.imgClothing);
		imgClothing.setImageResource(R.drawable.c01);
		imgClothing.setOnTouchListener(new TouchListener());
	}

	@Override
	public void setListener() {
		// 亮度
		sbLD.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// 设置亮度
				LightnessControl.SetLightness(PackageActivity.this, progress);
			}
		});
		// 对比度
		sbDBD.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				Bitmap bmp = Bitmap.createBitmap(imgWidth, imgHeight,
						Config.ARGB_8888);
				// int brightness = progress - 127;
				float contrast = (float) ((progress + 64) / 128.0);
				ColorMatrix cMatrix = new ColorMatrix();
				cMatrix.set(new float[] { contrast, 0, 0, 0, 0, 0, contrast, 0,
						0, 0,// 改变对比度
						0, 0, contrast, 0, 0, 0, 0, 0, 1, 0 });
				Paint paint = new Paint();
				paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
				Canvas canvas = new Canvas(bmp);
				// 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
				canvas.drawBitmap(bitmap, 0, 0, paint);
				imgPerson.setImageBitmap(bmp);
			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.buttonChange:
			// 调整
			if (isFrist) {
				layerChange.setVisibility(View.VISIBLE);
				isFrist = false;
			} else {
				layerChange.setVisibility(View.GONE);
				isFrist = true;
			}
			break;
		case R.id.buttonCamera:
			// 点击照片
			Builder builder = new AlertDialog.Builder(PackageActivity.this);
			View view = LayoutInflater.from(PackageActivity.this).inflate(
					R.layout.dialog_camera, null);
			builder.setView(view);
			// 初始化对话框的视图
			initBuilderView(view);
			dialog = builder.show();
			break;
		case R.id.btnChangeLeft:
			touchListener.settoLeft();
			break;
		case R.id.btnChangeRight:
			touchListener.settoRight();
			break;
		case R.id.btnChangeHigh:
			touchListener.settoUp();
			break;
		case R.id.btnChangeShort:
			touchListener.settoDown();
			break;
		case R.id.btnChangeFat:
			touchListener.settoFat();
			break;
		case R.id.btnChangeThin:
			touchListener.settoThin();
			break;
		case R.id.btnReturn:
			// 选模特
			if (isFrist03) {
				layerModuleClothes.setVisibility(View.VISIBLE);
				if (isFrist0) {
					showModuleList();
					isFrist0 = false;
				}
				isFrist03 = false;
			} else {
				layerModuleClothes.setVisibility(View.GONE);
				isFrist03 = true;
			}

			break;
		case R.id.btnChoiceCamera:
			// 1 打开相机
			startActivityForResult(new Intent(PackageActivity.this,
					CameraActivity.class), 100);
			dialog.dismiss();
			break;
		case R.id.btnChoiceAblum:
			// 2 相册
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			startActivityForResult(intent, 1);
			dialog.dismiss();
			break;

		case R.id.buttonSave:
			// 截取屏幕
			getCurrentScreen();
			break;
		case R.id.buttonShare:
			Toast.makeText(this, "点击分享", Toast.LENGTH_SHORT).show();
			break;
		case R.id.imgFavor:
			// 选包
			if (isFrist01) {
				layerClothes.setVisibility(View.VISIBLE);
				if (isFrist00) {
					showClothesList();
					isFrist00 = false;
				}
				isFrist01 = false;
			} else {
				layerClothes.setVisibility(View.GONE);
				isFrist01 = true;
			}
			break;
		case R.id.buttonClothing:
			// 恢复重选
			layerClothes.setVisibility(View.VISIBLE);
			layerModuleClothes.setVisibility(View.VISIBLE);
			showModuleList();
			showClothesList();
			break;
		case R.id.bg_pic:
			// 图片
			int[] pic = { R.drawable.f1, R.drawable.f2, R.drawable.f3 };
			switch (picId) {
			case 0:
				bg_pic_ground.setImageResource(pic[0]);
				picId = 1;
				break;
			case 1:
				bg_pic_ground.setImageResource(pic[1]);
				picId = 2;
				break;
			case 2:
				bg_pic_ground.setImageResource(pic[2]);
				picId = 0;
				break;
			}
			break;
		case R.id.bg_mus:
			// 音乐
			Intent i = new Intent(this, MusicService.class);
			if (play) {
				bg_mus.setBackgroundResource(R.drawable.ic_play);
				i.putExtra("playing", true);
				startService(i);
				play = false;
			} else {
				bg_mus.setBackgroundResource(R.drawable.ic_pauses);
				i.putExtra("playing", false);
				startService(i);
				play = true;
			}
			break;

		}

	}

	/**
	 * 选包
	 */
	private void showClothesList() {
		// 每次执行前 移除所有的视图！！
		layerClothes.removeAllViews();
		int[] pagIds = { R.drawable.e1, R.drawable.e2, R.drawable.e3,
				R.drawable.e4, R.drawable.e5, R.drawable.e6, R.drawable.e7,
				R.drawable.e8, R.drawable.e9, R.drawable.e10, R.drawable.e11,
				R.drawable.e12 };
		int[] pagIdss = { R.drawable.e1, R.drawable.e2, R.drawable.e3,
				R.drawable.e4, R.drawable.e5, R.drawable.e6, R.drawable.e7,
				R.drawable.e8, R.drawable.c01, R.drawable.c02, R.drawable.c03,
				R.drawable.c04 };
		for (int i = 0; i < pagIds.length; i++) {
			int j = pagIds[i];
			final int k = pagIdss[i];
			ImageView imageView = new ImageView(PackageActivity.this);
			LayoutParams layoutParams = new LayoutParams(150, 200);
			imageView.setLayoutParams(layoutParams);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(j);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					imgClothing.setImageResource(k);
					layerModuleClothes.removeAllViews();
					showModuleList(k % 3);
				}

				private void showModuleList(int i) {
					int[] Module0Ids = { R.drawable.cc1, R.drawable.cc3,
							R.drawable.cc4, R.drawable.cc7 };
					int[] Module0Idss = { R.drawable.c1, R.drawable.c3,
							R.drawable.c4, R.drawable.c7 };

					int[] Module1Ids = { R.drawable.cc2, R.drawable.cc5,
							R.drawable.cc6 };
					int[] Module1Idss = { R.drawable.c2, R.drawable.c5,
							R.drawable.c6 };

					int[] Module2Ids = { R.drawable.cc2 };
					int[] Module2Idss = { R.drawable.c2 };

					switch (i) {
					case 0:
						showModule(Module0Ids, Module0Idss);
						break;
					case 1:
						showModule(Module1Ids, Module1Idss);
						break;
					case 2:
						showModule(Module2Ids, Module2Idss);
						break;
					}
				}

				private void showModule(int[] ModuleIds, int[] ModuleIdss) {
					for (int l = 0; l < ModuleIds.length; l++) {
						int m = ModuleIds[l];
						final int n = ModuleIdss[l];
						ImageView imageView = new ImageView(
								PackageActivity.this);
						LayoutParams layoutParams = new LayoutParams(150, 200);
						imageView.setLayoutParams(layoutParams);
						imageView.setScaleType(ScaleType.FIT_XY);
						imageView.setImageResource(m);
						imageView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								imgPerson.setImageResource(n);
							}
						});
						layerModuleClothes.addView(imageView);

					}
				}

			});
			layerClothes.addView(imageView);
		}
	}

	/**
	 * 模特
	 */
	private void showModuleList() {
		// 每次执行前 移除所有的视图！！
		layerModuleClothes.removeAllViews();
		int[] ModuleIds = { R.drawable.cc1, R.drawable.cc2, R.drawable.cc3,
				R.drawable.cc4, R.drawable.cc5, R.drawable.cc6, R.drawable.cc7 };
		int[] ModuleIdss = { R.drawable.c1, R.drawable.c2, R.drawable.c3,
				R.drawable.c4, R.drawable.c5, R.drawable.c6, R.drawable.c7 };
		for (int i = 0; i < ModuleIds.length; i++) {
			int j = ModuleIds[i];
			final int k = ModuleIdss[i];
			ImageView imageView = new ImageView(PackageActivity.this);
			LayoutParams layoutParams = new LayoutParams(150, 320);
			imageView.setLayoutParams(layoutParams);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(j);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					imgPerson.setImageResource(k);
					layerClothes.removeAllViews();
					showPackageList(k % 3);
				}

				private void showPackageList(int i) {
					int[] package1Ids = { R.drawable.e2, R.drawable.e3,
							R.drawable.e4, R.drawable.e7, R.drawable.e8 };
					int[] package1Idss = { R.drawable.e2, R.drawable.e3,
							R.drawable.e4, R.drawable.e7, R.drawable.e8 };

					int[] package2Ids = { R.drawable.e5, R.drawable.e6 };
					int[] package2Idss = { R.drawable.e5, R.drawable.e6 };

					int[] package0Ids = { R.drawable.e9, R.drawable.e10,
							R.drawable.e11, R.drawable.e12 };
					int[] package0Idss = { R.drawable.c01, R.drawable.c02,
							R.drawable.c03, R.drawable.c04 };
					switch (i) {
					case 0:
						showPackage(package0Ids, package0Idss);
						break;
					case 1:
						showPackage(package1Ids, package1Idss);
						break;
					case 2:
						showPackage(package2Ids, package2Idss);
						break;
					}

				}

				private void showPackage(int[] package1Ids, int[] package1Idss) {
					for (int l = 0; l < package1Ids.length; l++) {
						int m = package1Ids[l];
						final int n = package1Idss[l];
						ImageView imageView = new ImageView(
								PackageActivity.this);
						LayoutParams layoutParams = new LayoutParams(150, 200);
						imageView.setLayoutParams(layoutParams);
						imageView.setScaleType(ScaleType.FIT_XY);
						imageView.setImageResource(m);
						imageView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								imgClothing.setImageResource(n);
							}
						});
						layerClothes.addView(imageView);
					}
				}
			});
			layerModuleClothes.addView(imageView);
		}
	}

	/**
	 * 标准
	 * 
	 * @param view
	 */
	private void initBuilderView(View view) {
		Button btnChoiceCamera = (Button) view
				.findViewById(R.id.btnChoiceCamera);
		Button btnChoiceAblum = (Button) view.findViewById(R.id.btnChoiceAblum);

		btnChoiceCamera.setOnClickListener(this);
		btnChoiceAblum.setOnClickListener(this);

		linerPerson = (LinearLayout) view.findViewById(R.id.linerPerson);

		int[] imagesId = { R.drawable.cc1, R.drawable.cc2, R.drawable.cc3,
				R.drawable.cc4, R.drawable.cc5, R.drawable.cc1, R.drawable.cc2,
				R.drawable.cc3, R.drawable.cc4, R.drawable.cc5 };
		int[] imagesIds = { R.drawable.c1, R.drawable.c2, R.drawable.c3,
				R.drawable.c4, R.drawable.c5, R.drawable.c1, R.drawable.c2,
				R.drawable.c3, R.drawable.c4, R.drawable.c5 };
		for (int i = 0; i < imagesId.length; i++) {
			int j = imagesId[i];
			final int k = imagesIds[i];

			ImageView imageView = new ImageView(PackageActivity.this);
			LayoutParams layoutParams = new LayoutParams(150, 250);
			imageView.setLayoutParams(layoutParams);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(j);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					imgPerson.setImageResource(k);
					dialog.dismiss();
				}
			});
			linerPerson.addView(imageView);
		}
	}

	/**
	 * Activity的数据传递：intent 的返回数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 此Activity开启intent的请求码值。
		switch (requestCode) {
		case 1:
			Uri uri = data.getData();
			// 获得相册的图片Uri---转换为 bitmap
			Bitmap bitmap1 = null;
			try {
				bitmap1 = MediaStore.Images.Media.getBitmap(
						this.getContentResolver(), uri);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (bitmap1 != null) {
				imgPerson.setImageBitmap(bitmap1);
			}
			break;

		}
		// 其他Activity的intent的结果码值。
		switch (resultCode) {
		case 22:
			Bundle data1 = data.getExtras();
			byte[] bytes = data1.getByteArray("bytes");
			// 将byte数组转换成Bitmap对象
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
					bytes.length);
			// 根据拍摄的方向旋转图像（纵向拍摄时要需要将图像选择90度)
			Matrix matrix = new Matrix();
			matrix.setRotate(CameraActivity.getPreviewDegree(this));
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			imgPerson.setImageBitmap(bitmap);
			break;
		}
	}

	/**
	 * 截取屏幕
	 */
	public void getCurrentScreen() {
		// 1.构建Bitmap
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int w = display.getWidth();// w=480
		int h = display.getHeight();// h=800
		Bitmap imageBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 最后一个参数叫位图结构
		// ARGB--Alpha,Red,Green,Blue.
		// ARGB为一种色彩模式,也就是RGB色彩模式附加上Alpha(透明度)通道,常见于32位位图的存储结构。

		// 2.获取屏幕
		View decorview = this.getWindow().getDecorView();// decor意思是装饰布置
		decorview.setDrawingCacheEnabled(true);
		imageBitmap = decorview.getDrawingCache();
		// 3.保存Bitmap到相册
		ContentResolver cr = getContentResolver();
		MediaStore.Images.Media.insertImage(cr, imageBitmap, "myPhoto",
				"this is a Photo");

		// 保存图片的文件夹路径
		// String SaveImageFilePath = getSDCardPath() + "/gameCounter";//
		String SaveImageFilePath = Utils.getSDCardPath();// 保存图片的文件夹路径--根目录

		// 4.保存Bitmap到根目录
		try {
			File path = new File(SaveImageFilePath);
			String imagepath = SaveImageFilePath + "/Screen" + ".jpg";// 保存图片的路径
			File file = new File(imagepath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				// imageBitmap.compress(format, quality, stream);
				// 把位图的压缩信息写入到一个指定的输出流中
				// 第一个参数format为压缩的格式
				// 第二个参数quality为图像压缩比的值,0-100.0 意味着小尺寸压缩,100意味着高质量压缩
				// 第三个参数stream为输出流
				imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
				Toast.makeText(this, "图片已经保存", Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对图片的移动，缩放等调整手势操作。
	 */
	private final class TouchListener implements OnTouchListener {

		/** 记录是拖拉照片模式还是放大缩小照片模式 */
		private int mode = 0;// 初始状态
		/** 拖拉照片模式 */
		private static final int MODE_DRAG = 1;
		/** 放大缩小照片模式 */
		private static final int MODE_ZOOM = 2;

		/** 用于记录开始时候的坐标位置 */
		private PointF startPoint = new PointF();
		/** 用于记录拖拉图片移动的坐标位置 */
		private Matrix matrix = new Matrix();
		/** 用于记录图片要进行拖拉时候的坐标位置 */
		private Matrix currentMatrix = new Matrix();

		/** 两个手指的开始距离 */
		private float startDis;
		/** 两个手指的中间点 */
		private PointF midPoint;

		public void settoLeft() {
			currentMatrix.set(imgClothing.getImageMatrix());
			matrix.set(currentMatrix);
			matrix.postTranslate(-10, 0);
			imgClothing.setImageMatrix(matrix);
		}

		public void settoRight() {
			currentMatrix.set(imgClothing.getImageMatrix());
			matrix.set(currentMatrix);
			matrix.postTranslate(10, 0);
			imgClothing.setImageMatrix(matrix);
		}

		public void settoUp() {
			currentMatrix.set(imgClothing.getImageMatrix());
			matrix.set(currentMatrix);
			matrix.postTranslate(0, -10);
			imgClothing.setImageMatrix(matrix);
		}

		public void settoDown() {
			currentMatrix.set(imgClothing.getImageMatrix());
			matrix.set(currentMatrix);
			matrix.postTranslate(0, 10);
			imgClothing.setImageMatrix(matrix);
		}

		public void settoFat() {
			currentMatrix.set(imgClothing.getImageMatrix());
			matrix.set(currentMatrix);
			matrix.postScale(1.05f, 1.05f);
			// matrix.postScale(1.2f, 1.2f, -1, -1);
			matrix.postTranslate(-20, 0);
			imgClothing.setImageMatrix(matrix);
		}

		public void settoThin() {
			currentMatrix.set(imgClothing.getImageMatrix());
			matrix.set(currentMatrix);
			matrix.postScale(0.95f, 0.95f);
			matrix.postTranslate(20, 0);
			imgClothing.setImageMatrix(matrix);
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			/** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// 手指压下屏幕
			case MotionEvent.ACTION_DOWN:
				mode = MODE_DRAG;
				// 记录ImageView当前的移动位置
				currentMatrix.set(imgClothing.getImageMatrix());
				startPoint.set(event.getX(), event.getY());
				break;
			// 手指在屏幕上移动，改事件会被不断触发
			case MotionEvent.ACTION_MOVE:
				// 拖拉图片
				if (mode == MODE_DRAG) {
					float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
					float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
					// 在没有移动之前的位置上进行移动
					matrix.set(currentMatrix);
					matrix.postTranslate(dx, dy);

				}
				// 放大缩小图片
				else if (mode == MODE_ZOOM) {
					float endDis = distance(event);// 结束距离
					if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
						float scale = endDis / startDis;// 得到缩放倍数
						matrix.set(currentMatrix);
						matrix.postScale(scale, scale, midPoint.x, midPoint.y);
					}
				}
				break;
			// 手指离开屏幕
			case MotionEvent.ACTION_UP:
				// 当触点离开屏幕，但是屏幕上还有触点(手指)
			case MotionEvent.ACTION_POINTER_UP:
				mode = 0;
				break;
			// 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
			case MotionEvent.ACTION_POINTER_DOWN:
				mode = MODE_ZOOM;
				/** 计算两个手指间的距离 */
				startDis = distance(event);
				/** 计算两个手指间的中间点 */
				if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
					midPoint = mid(event);
					// 记录当前ImageView的缩放倍数
					currentMatrix.set(imgClothing.getImageMatrix());
				}
				break;
			}
			imgClothing.setImageMatrix(matrix);
			return true;
		}

		/** 计算两个手指间的距离 */
		private float distance(MotionEvent event) {
			float dx = event.getX(1) - event.getX(0);
			float dy = event.getY(1) - event.getY(0);
			/** 使用勾股定理返回两点之间的距离 */
			return FloatMath.sqrt(dx * dx + dy * dy);
		}

		/** 计算两个手指间的中间点 */
		private PointF mid(MotionEvent event) {
			float midX = (event.getX(1) + event.getX(0)) / 2;
			float midY = (event.getY(1) + event.getY(0)) / 2;
			return new PointF(midX, midY);
		}

	}

}