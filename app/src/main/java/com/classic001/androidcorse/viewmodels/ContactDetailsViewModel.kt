package com.classic001.androidcorse.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.classic001.androidcorse.data.ContactRepository

class ContactDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val contactRepository = ContactRepository(application)

    fun getContactById(id: String) = contactRepository.loadContact(id)
}