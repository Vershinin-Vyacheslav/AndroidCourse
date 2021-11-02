package com.classic001.androidcorse.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.classic001.androidcorse.data.Contacts.contacts
import com.classic001.androidcorse.interfaces.ContactListResultListener
import com.classic001.androidcorse.interfaces.ContactResultListener
import java.lang.ref.WeakReference

class ContactService : Service() {
    private val binder = ContactBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    fun getContacts(callback: ContactListResultListener) {
        val weakReference = WeakReference(callback)
        Thread {
            weakReference.get()?.onComplete(contacts)
        }.start()
    }

    fun getContactById(contactId: String, callback: ContactResultListener) {
        val weakReference = WeakReference(callback)
        Thread {
            weakReference.get()?.onComplete(contacts.firstOrNull { it.id == contactId })
        }.start()
    }

    inner class ContactBinder : Binder() {
        fun getService() = this@ContactService
    }
}
