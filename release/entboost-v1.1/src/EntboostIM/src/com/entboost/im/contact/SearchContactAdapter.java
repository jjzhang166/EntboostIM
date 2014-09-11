package com.entboost.im.contact;

import java.util.List;

import net.yunim.service.entity.SearchContact;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.entboost.im.R;
import com.entboost.ui.utils.AbBitmapUtils;

public class SearchContactAdapter extends BaseAdapter {
	private List<SearchContact> list;
	private Context mContext;
	private LayoutInflater mInflater;

	public SearchContactAdapter(Context context, LayoutInflater mInflater,
			List<SearchContact> list) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
	}

	public List<SearchContact> getList() {
		return list;
	}

	public void setList(List<SearchContact> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return this.list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			// 使用自定义的list_items作为Layout
			convertView = mInflater.inflate(R.layout.item_msg_history, parent,
					false);
			// 减少findView的次数
			holder = new ViewHolder();
			// 初始化布局中的元素
			holder.itemsIcon = ((ImageView) convertView
					.findViewById(R.id.msg_head));
			holder.itemsTitle = ((TextView) convertView
					.findViewById(R.id.msg_name));
			holder.itemsText = ((TextView) convertView
					.findViewById(R.id.msg_message));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 获取该行的数据
		SearchContact obj = (SearchContact) getItem(position);
		holder.itemsTitle.setText(obj.getName());
		holder.itemsText.setText(obj.getSrc());
		Drawable drawable = mContext.getResources()
				.getDrawable(R.drawable.head);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		holder.itemsIcon
				.setImageBitmap(AbBitmapUtils.toRoundCorner(bitmap, 30));
		// 图片的下载
		// mAbImageDownloader.display(holder.itemsIcon, null);

		return convertView;
	}

	/**
	 * View元素
	 */
	static class ViewHolder {
		ImageView itemsIcon;
		TextView itemsTitle;
		TextView itemsText;
	}
}
