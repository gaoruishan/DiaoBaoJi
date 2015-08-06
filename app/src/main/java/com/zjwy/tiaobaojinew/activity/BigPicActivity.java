package com.zjwy.tiaobaojinew.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;

import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.widget.ZoomImageView;

public class BigPicActivity extends Activity {
	private ZoomImageView iv;
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bigpic);

		iv = (ZoomImageView) findViewById(R.id.imageView11);
		Intent intent = getIntent();
		if (intent != null) {
			byte[] buff = intent.getByteArrayExtra("image");
			bitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);
			if (bitmap != null) {
				iv.setImageBitmap(bitmap);
			}
		}

	}

	public void rotate(View view) {// 旋转
		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		iv.setImageBitmap(bitmap);

	}
}
