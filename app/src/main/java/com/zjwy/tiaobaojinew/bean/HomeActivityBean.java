package com.zjwy.tiaobaojinew.bean;

import java.util.List;

public class HomeActivityBean {
	private List<String> activityInfo;
	private List<BeanChild> single;
	private String focus;
	private String activityBanner;

	public List<String> getActivityInfo() {
		return activityInfo;
	}

	public void setActivityInfo(List<String> activityInfo) {
		this.activityInfo = activityInfo;
	}

	public List<BeanChild> getSingle() {
		return single;
	}

	public void setSingle(List<BeanChild> single) {
		this.single = single;
	}

	public String getFocus() {
		return focus;
	}

	public void setFocus(String focus) {
		this.focus = focus;
	}

	public String getActivityBanner() {
		return activityBanner;
	}

	public void setActivityBanner(String activityBanner) {
		this.activityBanner = activityBanner;
	}

	public class BeanChild {
		public String thumb;
		public String good_id;

		public String getThumb() {
			return thumb;
		}

		public void setThumb(String thumb) {
			this.thumb = thumb;
		}

		public String getGood_id() {
			return good_id;
		}

		public void setGood_id(String good_id) {
			this.good_id = good_id;
		}

	}
}
