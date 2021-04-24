package in.aditya.letsdoit.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import in.aditya.letsdoit.R;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"todoList")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("You Have a TODO ")
                .setContentText("Check app To complete your TODO ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);

        notificationCompat.notify(200, builder.build());
    }

}
