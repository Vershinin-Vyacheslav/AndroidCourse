package com.classic001.androidcorse.interfaces

import com.classic001.androidcorse.data.Contact

interface ContactListResultListener {
    fun onComplete(result: List<Contact>)
}