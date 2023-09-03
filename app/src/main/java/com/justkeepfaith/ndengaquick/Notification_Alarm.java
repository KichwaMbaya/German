package com.justkeepfaith.ndengaquick;

import static android.Manifest.*;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notification_Alarm extends BroadcastReceiver {

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context.getApplicationContext(), striscia_pagina.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent activity = PendingIntent.getActivity(context.getApplicationContext(),
                0, intent1, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Successful")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Glückwunsch!! Ihr Kreditantrag war erfolgreich!!")
                .setContentText("Ihr Darlehen wurde genehmigt. Melden Sie sich in der App an, um Einzelheiten zur Auszahlung zu erfahren.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Ihr Darlehen wurde genehmigt. Melden Sie sich in der App an, um Einzelheiten zur Auszahlung zu erfahren."))
                .setAutoCancel(true)
                .setContentIntent(activity);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        notificationManager.notify(200, builder.build());
    }
}
