package com.classic001.androidcorse.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ContactRepository(context: Context) {
    private lateinit var contactList: MutableLiveData<List<Contact>>
    private lateinit var contact: MutableLiveData<Contact>
    private val contacts =  Contacts(context)

    fun loadContactList(): LiveData<List<Contact>> {
        if (!::contactList.isInitialized) {
            contactList = MutableLiveData()
        }
        Thread {
            contactList.postValue(contacts.getContactList())
        }.start()
        return contactList
    }

    fun loadContact(id: String): LiveData<Contact> {
        if (!::contact.isInitialized) {
            contact = MutableLiveData()
        }
        Thread {
            contact.postValue(contacts.getContactById(id))
        }.start()
        return contact
    }
}