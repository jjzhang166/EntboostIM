package com.entboost.im.chat;

import java.util.Vector;

import net.yunim.service.EntboostCM;
import net.yunim.service.EntboostUM;
import net.yunim.service.constants.EB_ACCOUNT_TYPE;
import net.yunim.service.entity.CardInfo;
import net.yunim.service.listener.EditContactListener;
import net.yunim.utils.UIUtils;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entboost.im.R;
import com.entboost.utils.AbDateUtil;

public class CallAdapter extends BaseAdapter {

	private Context mContext;
	// xml转View对象
	private LayoutInflater mInflater;
	private Vector<CardInfo> list;

	public CallAdapter(Context context, LayoutInflater mInflater,
			Vector<CardInfo> list) {
		this.mContext = context;
		// 用于将xml转为View
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
	}

	public void setList(Vector<CardInfo> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private void enableBtns(ViewHolder holder) {
		holder.itemsBtnAccept.setVisibility(View.GONE);
		holder.call_btns.setVisibility(View.GONE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			// 使用自定义的list_items作为Layout
			convertView = mInflater.inflate(R.layout.item_call, parent, false);
			// 减少findView的次数
			holder = new ViewHolder();
			// 初始化布局中的元素
			holder.list_item_layout = ((RelativeLayout) convertView
					.findViewById(R.id.list_item_layout));
			holder.call_btns = ((LinearLayout) convertView
					.findViewById(R.id.call_btns));
			holder.itemsTitle = ((TextView) convertView
					.findViewById(R.id.call_name));
			holder.itemstype = ((TextView) convertView
					.findViewById(R.id.call_type));
			holder.itemsMsg = ((TextView) convertView
					.findViewById(R.id.call_message));
			holder.itemsBtnAccept = ((ImageButton) convertView
					.findViewById(R.id.call_temp));
			holder.itemsBtnInfo = ((Button) convertView
					.findViewById(R.id.call_info));
			holder.itemsBtnAdd = ((Button) convertView
					.findViewById(R.id.call_add));
			holder.itemsBtnReject = ((Button) convertView
					.findViewById(R.id.call_reject));
			holder.list_item_layout
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							CardInfo cardInfo = (CardInfo) getItem(position);
							if (cardInfo.getType() != CardInfo.TYPE_CALLING) {
								holder.call_btns.setVisibility(View.GONE);
								return;
							}
							if (holder.call_btns.getVisibility() == View.GONE) {
								holder.call_btns.setVisibility(View.VISIBLE);
							} else {
								holder.call_btns.setVisibility(View.GONE);
							}
						}
					});
			holder.itemsBtnAccept
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							CardInfo cardInfo = (CardInfo) getItem(position);
							EntboostCM.callAnswer(cardInfo.getCall_id(),
									cardInfo.getTo(), true);
							enableBtns(holder);
							cardInfo.setType(CardInfo.TYPE_ACCEPT);
							holder.itemstype.setText("已接受");
							Intent intent = new Intent(mContext,
									ChatActivity.class);
							intent.putExtra(ChatActivity.INTENT_TITLE,
									cardInfo.getNa());
							intent.putExtra(ChatActivity.INTENT_UID,
									cardInfo.getTo());
							mContext.startActivity(intent);
							((CallListActivity) mContext).finish();
						}
					});
			holder.itemsBtnReject
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							CardInfo cardInfo = (CardInfo) getItem(position);
							EntboostCM.callAnswer(cardInfo.getCall_id(),
									cardInfo.getTo(), false);
							enableBtns(holder);
							cardInfo.setType(CardInfo.TYPE_REJECT);
							holder.itemstype.setText("已拒绝");
						}
					});
			holder.itemsBtnAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final CardInfo cardInfo = (CardInfo) getItem(position);
					cardInfo.setType(CardInfo.TYPE_ACCEPT);
					EntboostUM.addContact(cardInfo, new EditContactListener() {

						@Override
						public void onFailure(String arg0) {
							UIUtils.showToast(mContext, arg0);
						}

						@Override
						public void onEditContactSuccess() {
							holder.itemstype.setText("已接受");
							Intent intent = new Intent(mContext,
									ChatActivity.class);
							intent.putExtra(ChatActivity.INTENT_TITLE,
									cardInfo.getNa());
							intent.putExtra(ChatActivity.INTENT_UID,
									cardInfo.getTo());
							mContext.startActivity(intent);
							((CallListActivity) mContext).finish();
						}
					});
				}
			});
			holder.itemsBtnInfo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final CardInfo cardInfo = (CardInfo) getItem(position);
					cardInfo.setType(CardInfo.TYPE_ACCEPT);
					((CallListActivity) mContext).showDialog(
							"详细资料",
							"名称：" + cardInfo.getNa() + "\n" + "公司："
									+ cardInfo.getEn());
				}
			});

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 获取该行的数据
		CardInfo cardInfo = (CardInfo) getItem(position);
		holder.itemsTitle.setText(cardInfo.getNa());
		if (cardInfo.getType() == CardInfo.TYPE_CALLING) {
			holder.itemstype.setText("呼叫中");
		} else if (cardInfo.getType() == CardInfo.TYPE_ACCEPT) {
			holder.itemstype.setText("已接受");
			holder.itemsBtnAccept.setVisibility(View.GONE);
		} else if (cardInfo.getType() == CardInfo.TYPE_REJECT) {
			holder.itemstype.setText("已拒绝");
			holder.itemsBtnAccept.setVisibility(View.GONE);
		} else if (cardInfo.getType() == CardInfo.TYPE_TIMEOUT) {
			holder.itemstype.setText("已超时");
			holder.itemsBtnAccept.setVisibility(View.GONE);
		}
		String timeStr = AbDateUtil.formatDateStr2Desc(AbDateUtil
				.getStringByFormat(cardInfo.getTime(),
						AbDateUtil.dateFormatYMDHMS),
				AbDateUtil.dateFormatYMDHMS);
		if (cardInfo.getT() == EB_ACCOUNT_TYPE.EB_ACCOUNT_TYPE_OUT_ENT
				.ordinal()) {
			holder.itemsMsg.setText(timeStr + "来自外部企业");
		} else if (cardInfo.getT() == EB_ACCOUNT_TYPE.EB_ACCOUNT_TYPE_USER
				.ordinal()) {
			holder.itemsMsg.setText(timeStr + "来自注册用户");
		} else if (cardInfo.getT() == EB_ACCOUNT_TYPE.EB_ACCOUNT_TYPE_VISITOR
				.ordinal()) {
			holder.itemsMsg.setText(timeStr + "来自访客");
		}

		return convertView;
	}

	/**
	 * View元素
	 */
	static class ViewHolder {
		TextView itemsTitle;
		TextView itemstype;
		TextView itemsMsg;
		ImageButton itemsBtnAccept;
		Button itemsBtnInfo;
		Button itemsBtnAdd;
		Button itemsBtnReject;
		RelativeLayout list_item_layout;
		LinearLayout call_btns;
	}

}
