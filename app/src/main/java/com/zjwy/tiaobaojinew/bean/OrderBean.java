package com.zjwy.tiaobaojinew.bean;

import java.util.List;

public class OrderBean {
	private String id;
	private String good_id;
	private String orderid;
	private String truename;
	private String mobile;
	private String address;
	private String activity_id;
	private String coupon_id;
	private String money;
	private String money_activity;
	private String money_coupon;
	private String rent_start;
	private String rent_type;
	private String senter_name;
	private String senter_mobile;
	private String status;
	private String user_id;
	private String user_name;
	private String account_time;
	private String payment_code;
	private String gallery_code;
	private String account_type;
	private String payment_rate;
	private String account_time_valid;
	private String account_data;
	private String day;
	private String isdelete;

	public String getAccount_time() {
		return account_time;
	}

	public void setAccount_time(String account_time) {
		this.account_time = account_time;
	}

	public String getPayment_code() {
		return payment_code;
	}

	public void setPayment_code(String payment_code) {
		this.payment_code = payment_code;
	}

	public String getGallery_code() {
		return gallery_code;
	}

	public void setGallery_code(String gallery_code) {
		this.gallery_code = gallery_code;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	public String getPayment_rate() {
		return payment_rate;
	}

	public void setPayment_rate(String payment_rate) {
		this.payment_rate = payment_rate;
	}

	public String getAccount_time_valid() {
		return account_time_valid;
	}

	public void setAccount_time_valid(String account_time_valid) {
		this.account_time_valid = account_time_valid;
	}

	public String getAccount_data() {
		return account_data;
	}

	public void setAccount_data(String account_data) {
		this.account_data = account_data;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	private List<GoodInfosBean> goodInfos;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGood_id() {
		return good_id;
	}

	public void setGood_id(String good_id) {
		this.good_id = good_id;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public String getCoupon_id() {
		return coupon_id;
	}

	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getMoney_activity() {
		return money_activity;
	}

	public void setMoney_activity(String money_activity) {
		this.money_activity = money_activity;
	}

	public String getMoney_coupon() {
		return money_coupon;
	}

	public void setMoney_coupon(String money_coupon) {
		this.money_coupon = money_coupon;
	}

	public String getRent_start() {
		return rent_start;
	}

	public void setRent_start(String rent_start) {
		this.rent_start = rent_start;
	}

	public String getRent_type() {
		return rent_type;
	}

	public void setRent_type(String rent_type) {
		this.rent_type = rent_type;
	}

	public String getSenter_name() {
		return senter_name;
	}

	public void setSenter_name(String senter_name) {
		this.senter_name = senter_name;
	}

	public String getSenter_mobile() {
		return senter_mobile;
	}

	public void setSenter_mobile(String senter_mobile) {
		this.senter_mobile = senter_mobile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public List<GoodInfosBean> getGoodInfos() {
		return goodInfos;
	}

	public void setGoodInfos(List<GoodInfosBean> goodInfos) {
		this.goodInfos = goodInfos;
	}

	public class GoodInfosBean {
		private String id;
		private String rent_price;
		private String title;
		private String thumb;
		private String rent_type;

		public String getRent_price() {
			return rent_price;
		}

		public void setRent_price(String rent_price) {
			this.rent_price = rent_price;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getThumb() {
			return thumb;
		}

		public void setThumb(String thumb) {
			this.thumb = thumb;
		}

		public String getRent_type() {
			return rent_type;
		}

		public void setRent_type(String rent_type) {
			this.rent_type = rent_type;
		}

	}
}
