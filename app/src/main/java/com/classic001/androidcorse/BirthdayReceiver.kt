package com.classic001.androidcorse

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.classic001.androidcorse.data.nextAlarmDate
import com.classic001.androidcorse.fragments.CONTACT_NAME
import com.classic001.androidcorse.fragments.EXTRA_CONTACT_ID
import java.util.*

private const val CHANNEL_ID = "BIRTHDAY_CHANNEL"

class BirthdayReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val contactId = intent.extras?.getString(EXTRA_CONTACT_ID)
            ?: return
        val contactName = intent.extras?.getString(CONTACT_NAME)
            ?: return
        createNotificationChannel(context)
        showNotification(context, contactId, contactName)
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, contactId.hashCode(), intent, 0)
        alarmMgr.set(
            AlarmManager.RTC_WAKEUP,
            nextAlarmDate(Calendar.getInstance()).timeInMillis,
            pendingIntent
        )
    }

    private fun showNotification(context: Context, id: String, name: String) {
        val notificationIntent = Intent(context, MainActivity::class.java)
            .putExtra(EXTRA_CONTACT_ID, id)
        val pendingIntent = PendingIntent.getActivity(
            context,
            id.hashCode(),
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentText(context.getString(R.string.notification_text, name))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.birthday_icon)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(id.hashCode(), notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
