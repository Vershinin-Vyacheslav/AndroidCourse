package com.classic001.androidcorse.data

import java.util.*

data class Contact(
    val id: String,
    val name: String,
    val phone1: String,
    val phone2: String,
    val email1: String,
    val email2: String,
    val description: String,
    val photo: Int,
    val birthday: Calendar?
)
