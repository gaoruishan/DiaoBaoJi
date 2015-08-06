package com.zjwy.tiaobaojinew.bean;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductContentBean implements Parcelable {
	private String id;
	private String title;
	private String thumb;
	private String thumb_best;
	private String thumb_newest;
	private String brand_code;
	private String sort_id;
	private String place_origin;
	private String position;
	private String listorder;
	private String model;
	private String color;
	private String material_outer;
	private String material_inner;
	private String style_open;
	private String size;
	private String structure_inner;
	private String level_new;
	private String completeness;
	private String price_market;
	private String price_appraiser;
	private String description;
	private String description_appraiser;
	private String picture_appraiser;
	private String search_field;
	private String is_genuine;
	private String time_buy;
	private String code_note;
	private String code_antifake;
	private String startrent_time;
	private String endrent_time;
	private String price_rent;
	private String price_rent_10;
	private String price_rent_7;
	private String provide_id;
	private String provide_name;
	private String remark;
	private String favorite_num;
	private String rent_num;
	private String status;
	private String in_use;
	private String book_status;
	private String book_time;
	private String alias;
	private String num_iid;
	private String create_time;
	private String in_inventory;
	private String price_rent_7bak;
	private String brand_name;
	private String brand_name_chinese;
	private String picture_story;
	private List<ActivityInfos> activityInfos;

	public class ActivityInfos {
		private String id;
		private String name;
		private String description;
		private String code;
		private String type;
		private String money;
		private String money_full;
		private String money_top;
		private String discount;
		private String thumb;
		private String listorder;
		private String position;
		private String start_time;
		private String end_time;
		private String status;
		private String create_time;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getMoney() {
			return money;
		}

		public void setMoney(String money) {
			this.money = money;
		}

		public String getMoney_full() {
			return money_full;
		}

		public void setMoney_full(String money_full) {
			this.money_full = money_full;
		}

		public String getMoney_top() {
			return money_top;
		}

		public void setMoney_top(String money_top) {
			this.money_top = money_top;
		}

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

		public String getThumb() {
			return thumb;
		}

		public void setThumb(String thumb) {
			this.thumb = thumb;
		}

		public String getListorder() {
			return listorder;
		}

		public void setListorder(String listorder) {
			this.listorder = listorder;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public String getStart_time() {
			return start_time;
		}

		public void setStart_time(String start_time) {
			this.start_time = start_time;
		}

		public String getEnd_time() {
			return end_time;
		}

		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getCreate_time() {
			return create_time;
		}

		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}
	}

	public List<ActivityInfos> getActivityInfos() {
		return activityInfos;
	}

	public void setActivityInfos(List<ActivityInfos> activityInfos) {
		this.activityInfos = activityInfos;
	}

	public String getThumb_newest() {
		return thumb_newest;
	}

	public void setThumb_newest(String thumb_newest) {
		this.thumb_newest = thumb_newest;
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

	public String getThumb_best() {
		return thumb_best;
	}

	public void setThumb_best(String thumb_best) {
		this.thumb_best = thumb_best;
	}

	public String getBrand_code() {
		return brand_code;
	}

	public void setBrand_code(String brand_code) {
		this.brand_code = brand_code;
	}

	public String getSort_id() {
		return sort_id;
	}

	public void setSort_id(String sort_id) {
		this.sort_id = sort_id;
	}

	public String getPlace_origin() {
		return place_origin;
	}

	public void setPlace_origin(String place_origin) {
		this.place_origin = place_origin;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getListorder() {
		return listorder;
	}

	public void setListorder(String listorder) {
		this.listorder = listorder;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getMaterial_outer() {
		return material_outer;
	}

	public void setMaterial_outer(String material_outer) {
		this.material_outer = material_outer;
	}

	public String getMaterial_inner() {
		return material_inner;
	}

	public void setMaterial_inner(String material_inner) {
		this.material_inner = material_inner;
	}

	public String getStyle_open() {
		return style_open;
	}

	public void setStyle_open(String style_open) {
		this.style_open = style_open;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getStructure_inner() {
		return structure_inner;
	}

	public void setStructure_inner(String structure_inner) {
		this.structure_inner = structure_inner;
	}

	public String getLevel_new() {
		return level_new;
	}

	public void setLevel_new(String level_new) {
		this.level_new = level_new;
	}

	public String getCompleteness() {
		return completeness;
	}

	public void setCompleteness(String completeness) {
		this.completeness = completeness;
	}

	public String getPrice_market() {
		return price_market;
	}

	public void setPrice_market(String price_market) {
		this.price_market = price_market;
	}

	public String getPrice_appraiser() {
		return price_appraiser;
	}

	public void setPrice_appraiser(String price_appraiser) {
		this.price_appraiser = price_appraiser;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription_appraiser() {
		return description_appraiser;
	}

	public void setDescription_appraiser(String description_appraiser) {
		this.description_appraiser = description_appraiser;
	}

	public String getPicture_appraiser() {
		return picture_appraiser;
	}

	public void setPicture_appraiser(String picture_appraiser) {
		this.picture_appraiser = picture_appraiser;
	}

	public String getSearch_field() {
		return search_field;
	}

	public void setSearch_field(String search_field) {
		this.search_field = search_field;
	}

	public String getIs_genuine() {
		return is_genuine;
	}

	public void setIs_genuine(String is_genuine) {
		this.is_genuine = is_genuine;
	}

	public String getTime_buy() {
		return time_buy;
	}

	public void setTime_buy(String time_buy) {
		this.time_buy = time_buy;
	}

	public String getCode_note() {
		return code_note;
	}

	public void setCode_note(String code_note) {
		this.code_note = code_note;
	}

	public String getCode_antifake() {
		return code_antifake;
	}

	public void setCode_antifake(String code_antifake) {
		this.code_antifake = code_antifake;
	}

	public String getStartrent_time() {
		return startrent_time;
	}

	public void setStartrent_time(String startrent_time) {
		this.startrent_time = startrent_time;
	}

	public String getEndrent_time() {
		return endrent_time;
	}

	public void setEndrent_time(String endrent_time) {
		this.endrent_time = endrent_time;
	}

	public String getPrice_rent() {
		return price_rent;
	}

	public void setPrice_rent(String price_rent) {
		this.price_rent = price_rent;
	}

	public String getPrice_rent_10() {
		return price_rent_10;
	}

	public void setPrice_rent_10(String price_rent_10) {
		this.price_rent_10 = price_rent_10;
	}

	public String getPrice_rent_7() {
		return price_rent_7;
	}

	public void setPrice_rent_7(String price_rent_7) {
		this.price_rent_7 = price_rent_7;
	}

	public String getProvide_id() {
		return provide_id;
	}

	public void setProvide_id(String provide_id) {
		this.provide_id = provide_id;
	}

	public String getProvide_name() {
		return provide_name;
	}

	public void setProvide_name(String provide_name) {
		this.provide_name = provide_name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFavorite_num() {
		return favorite_num;
	}

	public void setFavorite_num(String favorite_num) {
		this.favorite_num = favorite_num;
	}

	public String getRent_num() {
		return rent_num;
	}

	public void setRent_num(String rent_num) {
		this.rent_num = rent_num;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIn_use() {
		return in_use;
	}

	public void setIn_use(String in_use) {
		this.in_use = in_use;
	}

	public String getBook_status() {
		return book_status;
	}

	public void setBook_status(String book_status) {
		this.book_status = book_status;
	}

	public String getBook_time() {
		return book_time;
	}

	public void setBook_time(String book_time) {
		this.book_time = book_time;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getNum_iid() {
		return num_iid;
	}

	public void setNum_iid(String num_iid) {
		this.num_iid = num_iid;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getIn_inventory() {
		return in_inventory;
	}

	public void setIn_inventory(String in_inventory) {
		this.in_inventory = in_inventory;
	}

	public String getPrice_rent_7bak() {
		return price_rent_7bak;
	}

	public void setPrice_rent_7bak(String price_rent_7bak) {
		this.price_rent_7bak = price_rent_7bak;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getBrand_name_chinese() {
		return brand_name_chinese;
	}

	public void setBrand_name_chinese(String brand_name_chinese) {
		this.brand_name_chinese = brand_name_chinese;
	}

	public String getPicture_story() {
		return picture_story;
	}

	public void setPicture_story(String picture_story) {
		this.picture_story = picture_story;
	}

	/**
	 * 1）implements Parcelable
	 * 
	 * 2）重写writeToParcel方法，将你的对象序列化为一个Parcel对象，即：将类的数据写入外部提供的Parcel中，
	 * 打包需要传递的数据到Parcel容器保存，以便从 Parcel容器获取数据
	 * 
	 * 3）重写describeContents方法，内容接口描述，默认返回0就可以
	 * 
	 * 4）实例化静态内部对象CREATOR实现接口Parcelable.Creator
	 * 
	 * 过程：通过writeToParcel将你的对象映射成Parcel对象，再通过createFromParcel将Parcel对象映射成你的对象
	 */

	// 加上默认构造
	public ProductContentBean() {
	}

	// 创建一个构造器
	public ProductContentBean(Parcel source) {
		id = source.readString();
		title = source.readString();
		thumb = source.readString();
		thumb_best = source.readString();
		thumb_newest = source.readString();
		brand_code = source.readString();
		sort_id = source.readString();
		place_origin = source.readString();
		position = source.readString();
		listorder = source.readString();
		model = source.readString();
		color = source.readString();
		material_outer = source.readString();
		material_inner = source.readString();
		style_open = source.readString();
		size = source.readString();
		structure_inner = source.readString();
		level_new = source.readString();
		completeness = source.readString();
		price_market = source.readString();
		price_appraiser = source.readString();
		description = source.readString();
		description_appraiser = source.readString();
		picture_appraiser = source.readString();
		search_field = source.readString();
		is_genuine = source.readString();
		time_buy = source.readString();
		code_note = source.readString();
		code_antifake = source.readString();
		startrent_time = source.readString();
		endrent_time = source.readString();
		price_rent = source.readString();
		price_rent_10 = source.readString();
		price_rent_7 = source.readString();
		provide_id = source.readString();
		provide_name = source.readString();
		remark = source.readString();
		favorite_num = source.readString();
		rent_num = source.readString();
		status = source.readString();
		in_use = source.readString();
		book_status = source.readString();
		book_time = source.readString();
		alias = source.readString();
		num_iid = source.readString();
		create_time = source.readString();
		in_inventory = source.readString();
		price_rent_7bak = source.readString();
		brand_name = source.readString();
		brand_name_chinese = source.readString();
		picture_story = source.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(thumb);
		dest.writeString(thumb_best);
		dest.writeString(thumb_newest);
		dest.writeString(brand_code);
		dest.writeString(sort_id);
		dest.writeString(place_origin);
		dest.writeString(position);
		dest.writeString(listorder);
		dest.writeString(model);
		dest.writeString(color);
		dest.writeString(material_outer);
		dest.writeString(material_inner);
		dest.writeString(style_open);
		dest.writeString(size);
		dest.writeString(structure_inner);
		dest.writeString(level_new);
		dest.writeString(completeness);
		dest.writeString(price_market);
		dest.writeString(price_appraiser);
		dest.writeString(description);
		dest.writeString(description_appraiser);
		dest.writeString(picture_appraiser);
		dest.writeString(search_field);
		dest.writeString(is_genuine);
		dest.writeString(time_buy);
		dest.writeString(code_note);
		dest.writeString(code_antifake);
		dest.writeString(startrent_time);
		dest.writeString(endrent_time);
		dest.writeString(price_rent);
		dest.writeString(price_rent_10);
		dest.writeString(price_rent_7);
		dest.writeString(provide_id);
		dest.writeString(provide_name);
		dest.writeString(remark);
		dest.writeString(favorite_num);
		dest.writeString(rent_num);
		dest.writeString(status);
		dest.writeString(book_status);
		dest.writeString(in_use);
		dest.writeString(book_time);
		dest.writeString(alias);
		dest.writeString(num_iid);
		dest.writeString(create_time);
		dest.writeString(in_inventory);
		dest.writeString(price_rent_7bak);
		dest.writeString(brand_name);
		dest.writeString(brand_name_chinese);
		dest.writeString(picture_story);
	}

	public static final Parcelable.Creator<ProductContentBean> CREATOR = new Creator<ProductContentBean>() {

		@Override
		public ProductContentBean createFromParcel(Parcel source) {
			return new ProductContentBean(source);
		}

		@Override
		public ProductContentBean[] newArray(int size) {
			return new ProductContentBean[size];
		}
	};
}
