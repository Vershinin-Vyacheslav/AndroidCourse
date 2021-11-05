package com.classic001.androidcorse.data

import com.classic001.androidcorse.R
import java.util.*

object Contacts {

    private val simpleContact = Contact(
        "13",
        "Hollow Knight",
        "+100000000",
        "+200000000",
        "111@222.com",
        "111@222.ru",
        "здесь должно быть очень длинное описание...",
        R.drawable.hollowknight,
        setBirthday(29, 1, 1984)
    )

    val contacts = listOf(simpleContact)

    private fun setBirthday(day: Int, month: Int, year: Int): Calendar =
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, year)
        }
}