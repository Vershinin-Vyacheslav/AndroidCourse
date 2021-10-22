package com.classic001.androidcorse.interfaces

import com.classic001.androidcorse.data.Contact

interface ContactResultListener {
    fun onComplete(result: Contact?)
}