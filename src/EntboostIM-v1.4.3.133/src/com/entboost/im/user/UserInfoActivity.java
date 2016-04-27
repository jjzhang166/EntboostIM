package com.entboost.im.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import net.yunim.service.EntboostCache;
import net.yunim.service.EntboostUM;
import net.yunim.service.constants.EB_GENDER_TYPE;
import net.yunim.service.constants.EB_LOGON_TYPE;
import net.yunim.service.entity.AccountInfo;
import net.yunim.service.entity.Dict;
import net.yunim.service.listener.EditInfoListener;
import net.yunim.service.listener.LoadDictListener;
import net.yunim.utils.ResourceUtils;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entboost.global.AbConstant;
import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.entboost.im.department.SelectHeadImgActivity;
import com.entboost.im.global.MyApplication;
import com.entboost.im.ui.SegmentedRadioGroup;
import com.entboost.utils.AbDateUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserInfoActivity extends EbActivity {
	@ViewInject(R.id.user_head)
	private ImageView userHead;
	@ViewInject(R.id.user_account)
	private TextView user_account;
	@ViewInject(R.id.user_id)
	private TextView user_id;
	@ViewInject(R.id.user_username)
	private TextView user_username;
	@ViewInject(R.id.user_type)
	private TextView user_type;
	@ViewInject(R.id.user_description)
	private TextView user_description;
	@ViewInject(R.id.user_url)
	private TextView user_url;
	@ViewInject(R.id.segment_text)
	private SegmentedRadioGroup segment_text;
	@ViewInject(R.id.user_gender_male)
	private RadioButton user_gender_male;
	@ViewInject(R.id.user_gender_female)
	private RadioButton user_gender_female;
	@ViewInject(R.id.user_birthday)
	private TextView user_birthday;
	@ViewInject(R.id.user_area)
	private TextView user_area;
	@ViewInject(R.id.user_zipcode)
	private TextView user_zipcode;
	@ViewInject(R.id.user_tel)
	private TextView user_tel;
	@ViewInject(R.id.user_mobile)
	private TextView user_mobile;
	@ViewInject(R.id.user_email)
	private TextView user_email;
	@ViewInject(R.id.user_addr)
	private TextView user_addr;
	private AccountInfo user;
	@ViewInject(R.id.user_birthday_layout)
	private RelativeLayout user_birthday_layout;
	private int mYear = 1990;
	private int mMonth = 1;
	private int mDay = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_user_info);
		ViewUtils.inject(this);
		segment_text.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) UserInfoActivity.this
						.findViewById(radioButtonId);
				int gender = 0;
				if (StringUtils.equals(rb.getText(), "男")) {
					gender = EB_GENDER_TYPE.EB_GENDER_MALE.ordinal();
				} else if (StringUtils.equals(rb.getText(), "女")) {
					gender = EB_GENDER_TYPE.EB_GENDER_FEMALE.ordinal();
				} else {
					gender = EB_GENDER_TYPE.EB_GENDER_UNKNOWN.ordinal();
				}
				showProgressDialog("修改性别");
				EntboostUM.editUserInfo(null, null, -1, null, -1, null, -1,
						null, -1, null, null, null, gender, null, null, null,
						-1, null, null, new EditInfoListener() {

							@Override
							public void onFailure(String errMsg) {
								pageInfo.showError(errMsg);
								removeProgressDialog();
							}

							@Override
							public void onEditInfoSuccess() {
								removeProgressDialog();
							}
						});
			}
		});
		user_birthday_layout.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new MyDateDialogFragment();
				newFragment.show(getSupportFragmentManager(), "出生日期");
			}
		});
	}

	/**
	 * 更新日期显示
	 */
	private void updateDateDisplay() {
		user_birthday.setText(new StringBuilder().append(mYear).append("-")
				.append(mMonth < 10 ? "0" + mMonth : mMonth).append("-")
				.append((mDay < 10) ? "0" + mDay : mDay));
		showProgressDialog("修改出生日期");
		int birthday = mYear * 10000 + mMonth * 100 + mDay;
		EntboostUM.editUserInfo(null, null, -1, null, -1, null, -1, null, -1,
				null, null, null, -1, null, null, null, birthday, null, null,
				new EditInfoListener() {

					@Override
					public void onFailure(String errMsg) {
						pageInfo.showError(errMsg);
						removeProgressDialog();
					}

					@Override
					public void onEditInfoSuccess() {
						removeProgressDialog();
					}
				});
	}

	/**
	 * 日期控件的事件
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;
			updateDateDisplay();
		}
	};

	@SuppressLint("ValidFragment")
	class MyDateDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle saveInstanceState) {
			return new DatePickerDialog(UserInfoActivity.this,
					mDateSetListener, mYear, mMonth - 1, mDay);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		user = EntboostCache.getUser();
		if (user != null) {
			Bitmap img = ResourceUtils.getHeadBitmap(user.getHead_rid());
			if (img != null) {
				userHead.setImageBitmap(img);
			} else {
				ImageLoader.getInstance().displayImage(user.getHeadUrl(),
						userHead, MyApplication.getInstance().getImgOptions());
			}
			user_account.setText(user.getAccount());
			user_username.setText(user.getUsername());
			user_id.setText(user.getUid() + "");
			if (user.getLogon_type() == EB_LOGON_TYPE.LOGON_TYPE_VISITOR
					.getValue()) {
				user_type.setText("游客");
			} else {
				user_type.setText("注册用户");
			}
			user_description.setText(user.getDescription());
			user_url.setText(user.getUrl());
			if (user.getGender() == EB_GENDER_TYPE.EB_GENDER_MALE.ordinal()) {
				segment_text.check(user_gender_male.getId());
			} else if (user.getGender() == EB_GENDER_TYPE.EB_GENDER_FEMALE
					.ordinal()) {
				segment_text.check(user_gender_female.getId());
			}
			if (user.getBirthday() > 0) {
				Calendar c = Calendar.getInstance();
				c.setTime(AbDateUtil.getDateByFormat(user.getBirthday() + "",
						"yyyyMMdd"));
				mYear = c.get(Calendar.YEAR);
				mMonth = c.get(Calendar.MONTH) + 1;
				mDay = c.get(Calendar.DAY_OF_MONTH);
				user_birthday.setText(new StringBuilder().append(mYear)
						.append("-")
						.append(mMonth < 10 ? "0" + mMonth : mMonth)
						.append("-").append((mDay < 10) ? "0" + mDay : mDay));
			}
			user_area
					.setText((user.getArea1s() == null ? "" : user.getArea1s())
							+ (user.getArea2s() == null ? "" : user.getArea2s())
							+ (user.getArea3s() == null ? "" : user.getArea3s())
							+ (user.getArea4s() == null ? "" : user.getArea4s()));
			user_zipcode.setText(user.getZipcode());
			user_tel.setText(user.getTel());
			user_mobile.setText(user.getMobile());
			user_email.setText(user.getEmail());
			user_addr.setText(user.getAddr());
		}
	}

	@OnClick(R.id.user_head)
	public void openInfoHeader(View view) {
		if (user != null & user.getDefault_emp() != null) {
			Intent intent = new Intent(this, SelectHeadImgActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("memberCode", user.getDefault_emp());
			this.startActivity(intent);
		}
	}

	@OnClick(R.id.user_name_layout)
	public void openInfoNameEdit(View view) {
		Intent intent = new Intent(this, InfoNameEditActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@OnClick(R.id.user_description_layout)
	public void openInfoDescriptionEdit(View view) {
		Intent intent = new Intent(this, InfoDescriptionEditActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@OnClick(R.id.user_url_layout)
	public void openInfoUrlEdit(View view) {
		Intent intent = new Intent(this, InfoUrlEditActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@OnClick(R.id.user_zipcode_layout)
	public void openInfoZipcodeEdit(View view) {
		Intent intent = new Intent(this, InfoZipcodeEditActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@OnClick(R.id.user_tel_layout)
	public void openInfoTelEdit(View view) {
		Intent intent = new Intent(this, InfoTelEditActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@OnClick(R.id.user_mobile_layout)
	public void openInfoMobileEdit(View view) {
		Intent intent = new Intent(this, InfoMobileEditActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@OnClick(R.id.user_email_layout)
	public void openInfoEmailEdit(View view) {
		Intent intent = new Intent(this, InfoEmailEditActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@OnClick(R.id.user_addr_layout)
	public void openInfoAddrEdit(View view) {
		Intent intent = new Intent(this, InfoAddrEditActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private List<Dict> countries = new ArrayList<Dict>();
	private List<Dict> provinces = new ArrayList<Dict>();
	private List<Dict> citys = new ArrayList<Dict>();

	@OnClick(R.id.user_area_layout)
	public void openInfoAreaEdit(View view) {
		final View dialog_exitView = mInflater.inflate(R.layout.dialog_area,
				null);
		final WheelView country = (WheelView) dialog_exitView
				.findViewById(R.id.country);
		final WheelView province = (WheelView) dialog_exitView
				.findViewById(R.id.province);
		final WheelView city = (WheelView) dialog_exitView
				.findViewById(R.id.city);
		country.setViewAdapter(new DictAdapter(countries, this));
		province.setViewAdapter(new DictAdapter(provinces, this));
		city.setViewAdapter(new DictAdapter(citys, this));
		showDialog(AbConstant.DIALOGCENTER, dialog_exitView);
		country.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				EntboostUM.loadDict(countries.get(wheel.getCurrentItem())
						.getDict_id(), new LoadDictListener() {

					@Override
					public void onFailure(String errMsg) {
						showToast("地区数据加载失败！");
					}

					@Override
					public void onLoadSuccess(List<Dict> pdicts) {
						provinces = pdicts;
						updateProvince(province, pdicts);
					}
				});
			}
		});
		province.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				EntboostUM.loadDict(provinces.get(wheel.getCurrentItem())
						.getDict_id(), new LoadDictListener() {

					@Override
					public void onFailure(String errMsg) {
						showToast("地区数据加载失败！");
					}

					@Override
					public void onLoadSuccess(List<Dict> cdicts) {
						citys = cdicts;
						DictAdapter adapter = new DictAdapter(cdicts,
								UserInfoActivity.this);
						city.setViewAdapter(adapter);
						city.setVisibleItems(10);
					}
				});
			}
		});
		showProgressDialog("正在加载地区信息");
		EntboostUM.loadDict(0, new LoadDictListener() {

			@Override
			public void onFailure(String errMsg) {
				removeProgressDialog();
				showToast("地区数据加载失败！");
			}

			@Override
			public void onLoadSuccess(final List<Dict> dicts) {
				countries = dicts;
				final Dict dCountry = getDefaultCountry(dicts);
				EntboostUM.loadDict(dCountry.getDict_id(),
						new LoadDictListener() {

							@Override
							public void onFailure(String errMsg) {
								removeProgressDialog();
								showToast("地区数据加载失败！");
							}

							@Override
							public void onLoadSuccess(List<Dict> pdicts) {
								provinces = pdicts;
								if (provinces.size() == 0) {
									initWheel(dialog_exitView, dCountry);
								} else {
									int provineId = 0;
									if (user != null && user.getArea2() > 0) {
										provineId = user.getArea2();
									} else {
										provineId = provinces.get(0)
												.getDict_id();
									}
									EntboostUM.loadDict(provineId,
											new LoadDictListener() {

												@Override
												public void onFailure(
														String errMsg) {
													removeProgressDialog();
													showToast("地区数据加载失败！");
												}

												@Override
												public void onLoadSuccess(
														List<Dict> cdicts) {
													citys = cdicts;
													initWheel(dialog_exitView,
															dCountry);
												}
											});
								}
							}
						});

			}
		});
		Button saveBtn = (Button) dialog_exitView.findViewById(R.id.area_save);
		Button cancelBtn = (Button) dialog_exitView
				.findViewById(R.id.area_cancel);
		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getCenterDialog().dismiss();
			}
		});
		saveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Dict curCountry = countries.get(country.getCurrentItem());
				Dict curProvince = provinces.get(province.getCurrentItem());
				Dict curCity = citys.get(city.getCurrentItem());
				user_area.setText(curCountry.getDict_name()
						+ curProvince.getDict_name() + curCity.getDict_name());
				showProgressDialog("修改地址");
				EntboostUM.editUserInfo(null, null, curCountry.getDict_id(),
						curCountry.getDict_name(), curProvince.getDict_id(),
						curProvince.getDict_name(), curCity.getDict_id(),
						curCity.getDict_name(), -1, null, null, null, -1, null,
						null, null, -1, null, null, new EditInfoListener() {

							@Override
							public void onFailure(String errMsg) {
								removeProgressDialog();
								getCenterDialog().cancel();
							}

							@Override
							public void onEditInfoSuccess() {
								removeProgressDialog();
								getCenterDialog().cancel();
							}
						});
			}
		});
	}

	private void initWheel(View dialog_exitView, Dict dCountry) {
		WheelView country = (WheelView) dialog_exitView
				.findViewById(R.id.country);
		WheelView province = (WheelView) dialog_exitView
				.findViewById(R.id.province);
		WheelView city = (WheelView) dialog_exitView.findViewById(R.id.city);
		country.setViewAdapter(new DictAdapter(countries, UserInfoActivity.this));
		country.setVisibleItems(10);
		country.setCurrentItem(countries.indexOf(dCountry));
		updateProvince(province, provinces);
		DictAdapter adapter = new DictAdapter(citys, UserInfoActivity.this);
		city.setViewAdapter(adapter);
		city.setVisibleItems(10);
		if (user != null && user.getArea3() > 0) {
			for (Dict dict : citys) {
				if (user.getArea3() == dict.getDict_id()) {
					city.setCurrentItem(citys.indexOf(dict));
					break;
				}
			}
		} else {
			city.setCurrentItem(0);
		}
		removeProgressDialog();
	}

	private Dict getDefaultCountry(List<Dict> dicts) {
		for (Dict dict : dicts) {
			if (user != null && user.getArea1() > 0) {
				if (user.getArea1() == dict.getDict_id()) {
					return dict;
				}
			} else if (StringUtils.equals("中国", dict.getDict_name())) {
				return dict;
			}
		}
		return dicts.get(0);
	}

	private void updateProvince(WheelView dictView, List<Dict> dicts) {
		DictAdapter adapter = new DictAdapter(dicts, this);
		dictView.setViewAdapter(adapter);
		dictView.setVisibleItems(10);
		if (user != null && user.getArea2() > 0) {
			for (Dict dict : dicts) {
				if (user.getArea2() == dict.getDict_id()) {
					dictView.setCurrentItem(dicts.indexOf(dict));
					return;
				}
			}
		} else {
			dictView.setCurrentItem(0);
		}
	}

	/**
	 * Adapter for countries
	 */
	private class DictAdapter extends AbstractWheelTextAdapter {
		private List<Dict> dicts;

		/**
		 * Constructor
		 */
		protected DictAdapter(List<Dict> dicts, Context context) {
			super(context, R.layout.area_layout, R.id.area_name);
			this.dicts = dicts;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			AreaViewHolder holder;
			if (cachedView == null) {
				cachedView = LayoutInflater.from(context).inflate(
						itemResourceId, parent, false);
				holder = new AreaViewHolder();
				holder.itemsText = ((TextView) cachedView
						.findViewById(itemTextResourceId));
				cachedView.setTag(holder);
			} else {
				holder = (AreaViewHolder) cachedView.getTag();
			}
			holder.itemsText.setText(getItemText(index));
			return cachedView;
		}

		@Override
		public int getItemsCount() {
			return dicts.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return dicts.get(index).getDict_name();
		}

		/**
		 * View元素
		 */
		private class AreaViewHolder {
			public TextView itemsText;
		}

	}
}
