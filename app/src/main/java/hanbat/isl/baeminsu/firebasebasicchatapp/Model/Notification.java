package hanbat.isl.baeminsu.firebasebasicchatapp.Model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import hanbat.isl.baeminsu.firebasebasicchatapp.R;


public class Notification {

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotifyBuilder;

    private String CHANNEL_ID = "FIREBASE_MSG_CHANNEL_01";

    public Notification(Context context) {
        this.mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

/**
 * 수정한 부분(by 리플)
 */
//Android Version 26 over

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mNotifyBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID).setAutoCancel(true);
        } else {
            mNotifyBuilder = new NotificationCompat.Builder(mContext);
        }
        mNotifyBuilder.setVibrate(new long[]{1000, 1000});
        mNotifyBuilder.setPriority(100);

        /**
         * 수정한 부분(by 리플)
         * R.mipmap.~~ 사용시 애뮬레이터에서 오류 발생 (System UI has stopped)
         * 그래서 drawble 폴더에 ic_launcher를 복사 해서 사용
         */
        mNotifyBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        //mNotifyBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mNotifyBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

    }

    public Notification setTitle(String title) {
        mNotifyBuilder.setContentTitle(title);
        mNotifyBuilder.setTicker(title);
        return this;
    }

    public Notification setText(String text) {
        mNotifyBuilder.setContentText(text);
        return this;
    }

    public Notification setData(Intent intent) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(mContext);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent
                = taskStackBuilder.getPendingIntent(140, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyBuilder.setContentIntent(pendingIntent);
        return this;
    }

    public void notification() {
        try {

/**
 * 수정한 부분(by 리플)
 */
//Android Version 26 over
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                CharSequence name = "firebase_msg_channel";// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mNotificationManager.createNotificationChannel(mChannel);
            }

            mNotificationManager.notify(1, mNotifyBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}