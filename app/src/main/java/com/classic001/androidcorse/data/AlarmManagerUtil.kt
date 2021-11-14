package com.classic001.androidcorse.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.classic001.androidcorse.BirthdayReceiver
import com.classic001.androidcorse.R
import com.classic001.androidcorse.fragments.CONTACT_NAME
import com.classic001.androidcorse.fragments.EXTRA_CONTACT_ID
import java.util.*

fun setBirthdayAlarm(context: Context, contact: Contact) {
    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val nextAlarm = contact.birthday?.let { nextAlarmDate(contact.birthday) }
    val pendingIntent = createPendingIntent(context, contact)
    if (isAlarmEnabled(context, contact.id)) {
        if (nextAlarm != null) {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, nextAlarm.timeInMillis, pendingIntent)
        }
        val nextAlarmString = context.getString(
            R.string.date_mask,
            nextAlarm?.get(Calendar.DAY_OF_MONTH),
            nextAlarm?.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()),
            nextAlarm?.get(Calendar.YEAR)
        )
        Toast.makeText(
            context,
            context.getString(R.string.next_alarm, nextAlarmString),
            Toast.LENGTH_LONG
        ).show()
    }
}

fun cancelBirthdayAlarm(context: Context, contact: Contact) {
    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val pendingIntent = createPendingIntent(context, contact)
    alarmMgr.cancel(pendingIntent)
    pendingIntent.cancel()
}

fun isAlarmEnabled(context: Context, contactId: String) = PendingIntent.getBroadcast(
    context,
    contactId.hashCode(),
    Intent(context, BirthdayReceiver::class.java),
    PendingIntent.FLAG_NO_CREATE
) != null

fun createPendingIntent(context: Context, contact: Contact): PendingIntent {
    Intent(context, BirthdayReceiver::class.java).apply {
        putExtra(EXTRA_CONTACT_ID, contact.id)
        putExtra(CONTACT_NAME, contact.name)
        return PendingIntent.getBroadcast(context, contact.id.hashCode(), this, 0)
    }
}
