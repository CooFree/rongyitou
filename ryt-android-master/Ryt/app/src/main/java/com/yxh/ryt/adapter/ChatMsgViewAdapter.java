package com.yxh.ryt.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.R;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.vo.ChatMsgEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ChatMsgViewAdapter extends BaseAdapter {

    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;
        int IMVT_TO_MSG = 1;
    }

    private String userId;
    private String fromId;
    private long millionSeconds1;
    private long millionSeconds2;
    private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();

    private List<ChatMsgEntity> coll;

    private Context ctx;
    private ChatMsgEntity entity;

    private LayoutInflater mInflater;
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll, String userId, String fromId) {
        ctx = context;
        this.coll = coll;
        this.userId = userId;
        this.fromId = fromId;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return coll.size();
    }

    public Object getItem(int position) {
        return coll.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        entity = coll.get(position);
        if (entity.getMsgType()) {
            return IMsgViewType.IMVT_COM_MSG;
        } else {
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    public int getViewTypeCount() {
        return 2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ChatMsgEntity entity = coll.get(position);
        boolean isComMsg = entity.getMsgType();

        ViewHolder viewHolder = null;
        if (convertView == null) {
            if (isComMsg) {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_text_left, null);
            } else {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_text_right, null);
            }

            viewHolder = new ViewHolder();
            viewHolder.tvSendTime = (TextView) convertView
                    .findViewById(R.id.tv_sendtime);
            /*viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tv_username);*/
            viewHolder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_chatcontent);
            viewHolder.tvTime = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewHolder.userHead = (CircleImageView) convertView
                    .findViewById(R.id.iv_userhead);
            viewHolder.isComMsg = isComMsg;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (position == 0) {
            viewHolder.tvSendTime.setText(entity.getDate());
        } else {
            try {
                millionSeconds1 = sdf.parse(coll.get(position).getDate().toString()).getTime();
                millionSeconds2 = sdf.parse(coll.get(position - 1).getDate().toString()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((millionSeconds1 - millionSeconds2) > 300000) {
                viewHolder.tvSendTime.setText(entity.getDate());
            } else {
                viewHolder.tvSendTime.setVisibility(View.GONE);
            }
        }

        viewHolder.tvContent.setText(entity.getText());
        viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        viewHolder.tvTime.setText("");
        AppApplication.displayImage(entity.getPicUrl(),viewHolder.userHead);
        return convertView;
    }

    static class ViewHolder {
        TextView tvSendTime;
        TextView tvContent;
        TextView tvTime;
        CircleImageView userHead;
        boolean isComMsg = true;
    }

}
