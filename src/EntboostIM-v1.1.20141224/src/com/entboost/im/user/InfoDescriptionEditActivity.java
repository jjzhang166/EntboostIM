package com.entboost.im.user;

import net.yunim.service.EntboostUM;
import net.yunim.service.listener.EditInfoListener;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class InfoDescriptionEditActivity extends EbActivity {
	@ViewInject(R.id.infodes_description)
	private EditText infodes_description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_info_description_edit);
		ViewUtils.inject(this);
	}

	@OnClick(R.id.infodes_save_btn)
	public void save(View view) {
		String sNewDescription = infodes_description.getText().toString();
		showProgressDialog("修改备注信息");
		EntboostUM.setDescription(sNewDescription, new EditInfoListener() {

			@Override
			public void onFailure(String errMsg) {
				pageInfo.showError(errMsg);
				removeProgressDialog();
			}

			@Override
			public void onEditInfoSuccess() {
				removeProgressDialog();
				finish();
			}
		});
	}

	@OnClick(R.id.infodes_cancel_btn)
	public void cancel(View view) {
		finish();
	}

}
