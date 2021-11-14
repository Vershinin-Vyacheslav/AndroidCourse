package com.classic001.androidcorse.data

import java.util.*

const val FEBRUARY29 = 29
const val MONTH_OF_FEBRUARY = 1

fun nextAlarmDate(nextAlarm: Calendar): Calendar {
    val today = Calendar.getInstance()
    if (nextAlarm.before(today)) {
        if (is29February(nextAlarm) && isLeap(nextAlarm.get(Calendar.YEAR))) {
            if (nextAlarm.before(today)) {
                while (nextAlarm.before(today) || !isLeap(nextAlarm.get(Calendar.YEAR))) {
                    nextAlarm.add(Calendar.YEAR, 4)
                }
            }
        } else {
            nextAlarm.set(Calendar.YEAR, today.get(Calendar.YEAR))
            if (nextAlarm.before(today)) {
                nextAlarm.add(Calendar.YEAR, 1)
            }
        }
    }
    return nextAlarm
}

private fun is29February(currentDay: Calendar) =
    currentDay.get(Calendar.DAY_OF_MONTH) == FEBRUARY29 && currentDay.get(Calendar.MONTH) == MONTH_OF_FEBRUARY

private fun isLeap(year: Int) = year % 400 == 0 || year % 100 != 0 && year % 4 == 0