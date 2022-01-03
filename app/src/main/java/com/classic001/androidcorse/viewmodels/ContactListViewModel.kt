package com.classic001.androidcorse.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.classic001.androidcorse.data.ContactRepository

class ContactListViewModel(application: Application) : AndroidViewModel(application) {
    private val contactRepository = ContactRepository(application)

    fun getContactList() = contactRepository.loadContactList()
}