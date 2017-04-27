package com.mark.testtx;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class MyClockService extends Service {
    public MyClockService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("--Main--", "onStartCommand: " + startId + "intent.get==" + intent.getStringExtra("test") + ",flag==" + flags);
        Notification n = new Notification();
        NotificationManager mNotifMan = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

//        String party_id = null;
        String teStrings = intent.getStringExtra("test");

        RemoteViews contentView = new RemoteViews(
                "com.mark.testtx", R.layout.notification_view);
        contentView.setTextViewText(R.id.notification_view_content, teStrings);
        n.contentView = contentView;

        n.flags |= Notification.FLAG_SHOW_LIGHTS;
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        n.defaults = Notification.DEFAULT_ALL;
        n.icon = R.drawable.ic_launcher;

        Intent intent1 = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent1, 0);
        n.contentIntent = pi;

        mNotifMan.notify(AppData.NOTIFICATION_ID, n);// NOTIF_GET_MESSAGE
        return super.onStartCommand(intent, flags, startId);
    }
}
