package com.classic001.androidcorse.data

import com.classic001.androidcorse.R

object Contacts {

    private val simpleContact = Contact(
        "13",
        "Hollow Knight",
        "+100000000",
        "+200000000",
        "111@222.com",
        "111@222.ru",
        "здесь должно быть очень длинное описание...",
        R.drawable.hollowknight
    )

    val contacts = listOf(simpleContact)
}