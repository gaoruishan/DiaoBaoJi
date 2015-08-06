package com.zjwy.tiaobaojinew.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSON;
import com.zjwy.tiaobaojinew.BaseApplication;
import com.zjwy.tiaobaojinew.bean.UserInfo;
import com.zjwy.tiaobaojinew.update.Update;

public class HttpUtil {
	private static String updateUrl = "http://192.168.0.38:8080/app/upload.xml";

	/**
	 * 通过url获取更新的javabean
	 * 
	 */
	public static Update paresXML() {
		Update update = null;
		try {
			URL url = new URL(updateUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			// 启用本地缓存
			connection.setUseCaches(false);
			// 连接
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				update = Update.parse(connection.getInputStream());
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return update;
	}

	/**
	 * 进行自动登录
	 */
	public static void autoLogin() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				SharedPreferences sp = BaseApplication.getSharedPreferences();
				HttpGet httpGet = new HttpGet(AppConstant.LOGIN + "&name="
						+ sp.getString("mobile", null) + "&password="
						+ sp.getString("password", null));
				try {
					HttpResponse response = BaseApplication.getHttpClient()
							.execute(httpGet);

					if (response.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(
								response.getEntity(), "UTF-8");
						JSONObject jsObj = new JSONObject(result);
						String status = jsObj.getString("message");
						// 判断执行相应操作
						if ("登录成功".equals(status)) {
							// 保存数据
							UserInfo userInfo = JSON.parseObject(
									jsObj.getString("data"), UserInfo.class);
							Editor editor = sp.edit();
							editor.putBoolean("isLogin", true);// 用于设置是否自动登录---换头像
							editor.putString("id", userInfo.getId());
							editor.putString("mobile", userInfo.getMobile());
							editor.putString("username", userInfo.getName());
							editor.putString("gender", userInfo.getGender());
							editor.putString("nickname", userInfo.getNickname());
							editor.putString("avatar", userInfo.getAvatar());
							editor.putString("email", userInfo.getEmail());
							editor.putString("qq", userInfo.getQq());
							editor.putString("ucserver_id",
									userInfo.getUcserver_id());
							editor.putString("telephone",
									userInfo.getTelephone());
							editor.putString("truename_last",
									userInfo.getTruename_last());
							editor.putString("mobile_last",
									userInfo.getMobile_last());
							editor.putString("truename",
									userInfo.getTruename_last());
							editor.putString("nickname", userInfo.getNickname());
							editor.putString("idcard", userInfo.getIdcard());
							editor.putString("channel_code",
									userInfo.getChannel_code());
							editor.putString("birthday", userInfo.getBirthday());
							editor.putString("postcode", userInfo.getPostcode());
							editor.putString("education",
									userInfo.getEducation());
							editor.putString("graduate", userInfo.getGraduate());
							editor.putString("organization_scal",
									userInfo.getOrganization_scal());
							editor.putString("career", userInfo.getCareer());
							editor.putString("description",
									userInfo.getDescription());
							editor.putString("login_num",
									userInfo.getLogin_num());
							editor.putString("fan_num", userInfo.getFan_num());
							editor.putString("follow_num",
									userInfo.getFollow_num());
							editor.putString("lastlogin_ip",
									userInfo.getLastlogin_ip());
							editor.putString("lastlogin_time",
									userInfo.getLastlogin_time());
							editor.putString("is_verify",
									userInfo.getIs_verify());
							editor.putString("islock", userInfo.getIslock());
							editor.putString("only_bind",
									userInfo.getOnly_bind());
							editor.putString("industry_1_id",
									userInfo.getIndustry_1_id());
							editor.putString("organization",
									userInfo.getOrganization());
							editor.putString("register_ip",
									userInfo.getRegister_ip());
							editor.putString("region_id",
									userInfo.getRegion_id());
							editor.putString("address_last",
									userInfo.getAddress_last());
							editor.putString("register_date",
									userInfo.getRegister_date());
							editor.commit();
							// 保持登录状态
							BaseApplication appCookie = ((BaseApplication) BaseApplication
									.getApplication());
							// 读cookie
							CookieStore cookies = appCookie.getCookie();
							// 设置cookie
							appCookie.setCookie(cookies);
							// 销毁登录界面
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * 
	 * @Title: getBitmapFormUrl
	 * @说 明:从服务器获取Bitmap
	 * @参 数: @param url
	 * @参 数: @return
	 * @return Bitmap 返回类型
	 * @throws
	 */
	public static Bitmap getBitmapFormUrl(String url) {
		Bitmap bitmap = null;
		HttpClient httpClient = new DefaultHttpClient();
		// 设置超时时间
		HttpConnectionParams.setConnectionTimeout(new BasicHttpParams(),
				6 * 1000);
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				bitmap = BitmapFactory.decodeStream(entity.getContent());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 从服务器获取Jsons数据
	 * 
	 * @param url
	 * @return
	 */
	public static String getJsonFromServer(String url) {

		HttpClient httpClient = getHttpClient();
		HttpGet get = new HttpGet(url);

		try {
			HttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, "UTF-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (get != null) {
				get.abort();
			}
		}
		return null;

	}

	/**
	 * 
	 * @Title: getHttpClient
	 * @说 明:设置httpClient的一些参数
	 * @参 数: @return
	 * @return HttpClient 返回类型
	 * @throws
	 */
	private static HttpClient getHttpClient() {
		// 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
		HttpParams httpParams = new BasicHttpParams();
		// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		// 设置重定向，缺省为 true
		HttpClientParams.setRedirecting(httpParams, true);
		// 设置 user agent
		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);
		// 创建一个 HttpClient 实例
		// 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient
		// 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}
}
