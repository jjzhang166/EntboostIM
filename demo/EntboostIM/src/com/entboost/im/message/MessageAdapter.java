package com.entboost.im.message;

import java.util.Vector;

import net.yunim.service.entity.DynamicNews;
import net.yunim.utils.UIUtils;
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
import com.entboost.utils.AbDateUtil;

public class MessageAdapter extends BaseAdapter {

	private Context mContext;
	// xml转View对象
	private LayoutInflater mInflater;
	private Vector<DynamicNews> list;

	public MessageAdapter(Context context,LayoutInflater mInflater,Vector<DynamicNews> list) {
		this.mContext = context;
		// 用于将xml转为View
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list=list;
	}
	
	

	public void setList(Vector<DynamicNews> list) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			// 使用自定义的list_items作为Layout
			convertView = mInflater.inflate(R.layout.item_msg_history, parent, false);
			// 减少findView的次数
			holder = new ViewHolder();
			// 初始化布局中的元素
			holder.itemsCount = ((TextView) convertView.findViewById(R.id.unread_msg_num));
			holder.itemsIcon = ((ImageView) convertView.findViewById(R.id.msg_head));
			holder.itemsTitle = ((TextView) convertView.findViewById(R.id.msg_name));
			holder.itemsText = ((TextView) convertView.findViewById(R.id.msg_message));
			holder.itemsTime = ((TextView) convertView.findViewById(R.id.msg_time));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 获取该行的数据
		DynamicNews obj = (DynamicNews)getItem(position);
		if(obj.getNoReadNum()==0){
			holder.itemsCount.setVisibility(View.GONE);
		}else{
			holder.itemsCount.setVisibility(View.VISIBLE);
			holder.itemsCount.setText(String.valueOf(obj.getNoReadNum()));
		}
		holder.itemsTitle.setText(obj.getTitle());
		holder.itemsText.setText(UIUtils.getTipCharSequence(obj.getContent()));
		holder.itemsTime.setText(AbDateUtil.formatDateStr2Desc(AbDateUtil
				.getStringByFormat(obj.getTime(), AbDateUtil.dateFormatYMDHMS),
				AbDateUtil.dateFormatYMDHMS));
		Drawable drawable = mContext.getResources().getDrawable(R.drawable.head);
		BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();  
        holder.itemsIcon.setImageBitmap(AbBitmapUtils.toRoundCorner(bitmap, 30));   
		// 图片的下载
//		mAbImageDownloader.display(holder.itemsIcon, null);

		return convertView;
	}
	
	/**
	 * View元素
	 */
	static class ViewHolder {
		TextView itemsCount;
		ImageView itemsIcon;
		TextView itemsTitle;
		TextView itemsText;
		TextView itemsTime;
	}
}
