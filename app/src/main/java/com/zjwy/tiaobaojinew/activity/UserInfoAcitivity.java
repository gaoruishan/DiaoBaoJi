package com.zjwy.tiaobaojinew.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.R;
import com.zjwy.tiaobaojinew.utils.AppConstant;

public class UserInfoAcitivity extends BaseActivity {
	@ViewInject(R.id.tv_back_user)
	private TextView tv_back_user;
	@ViewInject(R.id.tv_other_user)
	private TextView tv_other_user;
	@ViewInject(R.id.tv_accout)
	private TextView tv_accout;
	@ViewInject(R.id.tv_user_sex)
	private TextView tv_user_sex;
	@ViewInject(R.id.tv_nickname)
	private TextView tv_nickname;
	@ViewInject(R.id.tv_title_user)
	private TextView tv_title_user;
	@ViewInject(R.id.tv_password)
	private TextView tv_password;
	@ViewInject(R.id.bt_logout)
	private Button bt_logout;
	@ViewInject(R.id.iv_head)
	private ImageView imageView;
	@ViewInject(R.id.et_user_sign)
	private EditText et_user_sign;
	@ViewInject(R.id.et_address)
	private EditText et_address;
	@ViewInject(R.id.ll_heads)
	private LinearLayout ll_heads;
	private File filesDir;
	private File f;
	private SharedPreferences sp;

	/**
	 * 点击返回
	 */
	@OnClick(R.id.tv_back_user)
	public void backButtonClick(View v) {
		onBackPressed();
	}

	/**
	 * 点击编辑
	 */
	@OnClick(R.id.tv_other_user)
	public void othersButtonClick(View v) {
		if (BaseApplication.getSharedPreferences().getBoolean("isLogin", false)) {
			startActivity(EditUserActivity.class);
		} else {
			BaseApplication.getApplication().showToast(
					getString(R.string.please_login));
		}
	}

	/**
	 * 点击密码
	 */
	@OnClick(R.id.tv_password)
	public void passwordButtonClick(View v) {
		if (BaseApplication.getSharedPreferences().getBoolean("isLogin", false)) {
			startActivity(ChangePwdActivity.class);
		} else {
			BaseApplication.getApplication().showToast("请登录");
		}

	}

	/**
	 * 点击注销
	 */
	@OnClick(R.id.bt_logout)
	public void logoutButtonClick(View v) {
		BaseApplication.getApplication().unRegisterLogin();
		BaseApplication.getApplication().showToast(
				getString(R.string.cancelled));
		onBackPressed();
	}

	@Override
	public void setView() {
		setContentView(R.layout.activity_userinfo);
		ViewUtils.inject(this);// 申明的时候用注解，要在此注入布局视图

	}

	/**
	 * 初始化个人信息
	 */
	@Override
	public void initView() {
		tv_other_user.setVisibility(View.VISIBLE);
		tv_other_user.setText("编辑");
		tv_title_user.setText("个人信息");
		sp = getSharedPreferences("config", MODE_PRIVATE);
		tv_nickname.setText(sp.getString("nickname", ""));
		tv_accout.setText(sp.getString("username", ""));
		et_user_sign.setText(sp.getString("sign", ""));
		et_address.setText(sp.getString("address", ""));
		String gender = sp.getString("gender", "3");
		if (gender != null) {
			if (gender.equals("1")) {
				tv_user_sex.setText("男");
			} else if (gender.equals("2")) {
				tv_user_sex.setText("女");
			} else {
				tv_user_sex.setText("保密");
			}

		}
	}

	@Override
	protected void onRestart() {// 编辑后---重新初始化
		super.onRestart();
		initView();
	}

	@Override
	public void setListener() {
		// 点击头像修改
		final SharedPreferences sp = BaseApplication.getSharedPreferences();
		ll_heads.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sp.getBoolean("isLogin", false)) {
					// 自定义的对话框
					Builder builder = new AlertDialog.Builder(
							UserInfoAcitivity.this);
					View view = LayoutInflater.from(UserInfoAcitivity.this)
							.inflate(R.layout.alter_dialoy, null);
					builder.setView(view);
					final AlertDialog dialog = builder.show();

					// 点击相机
					view.findViewById(R.id.camera).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									openCamera();// 打开相机
									dialog.dismiss();
								}
							});
					// 点击相册
					view.findViewById(R.id.album).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											Intent.ACTION_GET_CONTENT, null);
									intent.setType("image/*");
									startActivityForResult(intent, 3);
									dialog.dismiss();
								}
							});
				} else {
					BaseApplication.getApplication().showToast("请登录");
				}
			}

		});

		// 填写签名
		et_user_sign.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				Editor editor = sp.edit();
				editor.putString("sign", s.toString());
				editor.commit();
			}
		});

		// 填写 地址
		et_address.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				Editor editor = sp.edit();
				editor.putString("address", s.toString());
				editor.commit();
			}
		});
	}

	/**
	 * 设置头像，签名，地址的监听
	 */
	@Override
	public void initData() {
		if (BaseApplication.getSharedPreferences().getBoolean("isLogin", false)) {
			String fileUrl = BaseApplication.getSharedPreferences().getString(
					"avatar", null);
			if (fileUrl != null) {
				BitmapUtils bitmapUtils = new BitmapUtils(this);
				bitmapUtils.display(imageView, fileUrl);
			}
		}

	}

	/**
	 * 打开相机
	 */
	private void openCamera() {

		/**
		 * URI—Universal Resource Identifier通用资源标志符
		 * Web上可用的每种资源如HTML文档、图像、视频片段、程序等都是一个来URI来定位的 URI一般由三部组成 ①访问资源的命名机制
		 * ②存放资源的主机名 ③资源自身的名称，由路径表示，着重强调于资源。
		 * 
		 * URL—Uniform Resource Location统一资源定位符
		 * URL是Internet上用来描述信息资源的字符串，主要用在各种WWW客户程序和服务器程序上，特别是著名的Mosaic。
		 * 采用URL可以用一种统一的格式来描述各种信息资源，包括文件、服务器的地址和目录等。 URL一般由三部组成 ①协议(或称为服务方式)
		 * ②存有该资源的主机IP地址(有时也包括端口号) ③主机资源的具体地址。如目录和文件名等
		 */
		// 1 打开相机
		Intent i = new Intent();
		i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		// 相片保存位置
		// /mnt/sdcard/android/data/包名/DCIM
		filesDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);

		// /mnt/sdcard/DCIM
		Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

		f = new File(filesDir, "myimage.png");

		// uri schema://host:port/path
		// cotent://......
		// file://
		System.out.println("-------" + filesDir + "-------" + f);
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		startActivityForResult(i, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
			imageView.setImageBitmap(bitmap);
			// 裁剪
			Intent i = new Intent("com.android.camera.action.CROP");
			i.setDataAndType(Uri.fromFile(f), "image/*");
			i.putExtra("aspectX", 1);
			i.putExtra("aspectY", 1);
			i.putExtra("outputX", 200);
			i.putExtra("outputY", 200);
			i.putExtra("scale", true);
			i.putExtra("return-data", true);
			startActivityForResult(i, 1);
			break;

		case 1:
			Bitmap bmp = data.getParcelableExtra("data");
			if (bmp != null) {
				saveLocal(bmp);
			}
			break;

		case 3:
			Uri uri = data.getData();
			// 获得相册的图片Uri---转换为 file
			String img_path = getRealPathFromURI(uri);
			File file = new File(img_path);
			Bitmap bitmap11 = loadBigBitmap(file.getPath(), 200, 200);
			if (bitmap11 != null) {
				// 设置图片
				imageView.setImageBitmap(bitmap11);
				// 保存到本地--压缩图片
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file);
					bitmap11.compress(Bitmap.CompressFormat.PNG, 100, out);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// 开启上传线程
				uploadThread(file);
			}
			break;
		}
	}

	/**
	 * Uri---转换为 file
	 */
	public String getRealPathFromURI(Uri contentUri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null,
				null, null);
		if (cursor.moveToFirst()) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}

	/**
	 * 保存到本地(固定位置)并开启上传线程
	 */
	private void saveLocal(Bitmap bmp) {
		imageView.setImageBitmap(bmp);
		// 1 保存到本地
		final File file = new File(filesDir, "caijian.png");
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 开启上传线程
		uploadThread(file);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(UserInfoAcitivity.this, "上传成功", Toast.LENGTH_LONG)
					.show();
		};
	};

	/**
	 * 开启上传线程
	 */
	private void uploadThread(final File file) {
		new Thread() {
			public void run() {
				// 2 上传
				System.out.println("-------" + "上传开始");
				HttpClient httpClient = BaseApplication.getHttpClient();
				httpClient.getParams().setParameter(
						CoreProtocolPNames.PROTOCOL_VERSION,
						HttpVersion.HTTP_1_1);
				HttpPost post = new HttpPost(AppConstant.UPLOAD_FILE);
				FileEntity reqEntity = new FileEntity(file,
						"binary/octet-stream");
				reqEntity.setContentType("binary/octet-stream");
				// post.setHeader("mobile", "15192780515");
				// post.setHeader("Content-Length",
				// String.valueOf(reqEntity.getContentLength()));
				post.setEntity(reqEntity);

				try {
					HttpResponse response = httpClient.execute(post);
					if (response.getStatusLine().getStatusCode() == 200) {
						String string = EntityUtils.toString(
								response.getEntity(), "UTF-8");
						// 分割字符串{---解析json
						JSONObject object = new JSONObject("{"
								+ string.split("\\{")[1]);
						String code = object.getString("code");
						String fileUrl = object.getString("fileUrl");
						if (code.equals("10000")) {
							System.out.println("-------" + "上传成功");
							Editor edit = sp.edit();
							edit.putString("avatar", fileUrl);
							edit.commit();
							handler.sendEmptyMessage(0);
						}
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			};
		}.start();
	}

	/**
	 * 如果要加载大图，需要使用二次加载的技术
	 * 
	 */
	public Bitmap loadBigBitmap(String filePath, int width, int height) {

		Options opts = new Options();
		// 加载图片的时间，只加载图片的宽高值，不会真正的加载图片
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		int outHeight = opts.outHeight;
		int outWidth = opts.outWidth;

		int size = 1;

		while (outHeight / height > size || outWidth / width > size) {
			size++;
		}

		// 缩放比例
		opts.inSampleSize = size;
		opts.inJustDecodeBounds = false;
		// 二次加载 得到图片
		return BitmapFactory.decodeFile(filePath, opts);

	}

}
