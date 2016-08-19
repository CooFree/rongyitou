package com.yxh.ryt.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.CustomMsgActivity;
import com.yxh.ryt.activity.IndexActivity;
import com.yxh.ryt.activity.ShowMsgActivity;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.ChatMsgEntity;
import com.yxh.ryt.vo.JpushEntity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		System.out.println( "[MyReceiver] 接收Registration Id : " + JPushInterface.getRegistrationID(context));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
           /* String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);*/
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
			chatMsgEntity.setDate(Utils.getCurrentTime());
			String s=bundle.getString(JPushInterface.EXTRA_EXTRA);
			JpushEntity entity = AppApplication.getSingleGson().fromJson(s, JpushEntity.class);
			chatMsgEntity.setUserId(entity.getUserId()+"");
			Log.d("userDI", entity.getUserId() + "");
			chatMsgEntity.setText(bundle.getString(JPushInterface.EXTRA_ALERT));
			EventBus.getDefault().post(chatMsgEntity);
			Intent inten = new Intent("android.intent.action.Server_BROADCAST");
			AppApplication.getSingleContext().sendBroadcast(inten);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
        	//打开自定义的Activity
        	Intent i = new Intent(context, ShowMsgActivity.class);
        	i.putExtras(bundle);
        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(i);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		Intent intent = new Intent(context, IndexActivity.class);
		intent.putExtras(bundle);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		Notification build = new NotificationCompat.Builder(context).setAutoCancel(true)
				.setContentTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE))
				.setContentText(msg)
				.setSmallIcon(
						R.mipmap.ic_launcher)
				.setContentIntent(pendingIntent)
				.build();
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(0, build);
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!EFeiYiUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
	}
//	public interface BRInteraction {
//		public void setText(String content);
//	}
//
//	public void setBRInteractionListener(BRInteraction brInteraction) {
//		this.brInteraction = brInteraction;
//	}
}
