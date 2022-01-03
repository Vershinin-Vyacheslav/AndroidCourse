package com.classic001.androidcorse.data

import android.content.Context
import android.provider.ContactsContract
import java.util.*

private const val SELECTION = "${ContactsContract.Data.CONTACT_ID}="
private const val CURRENT_CONTACT_SELECTION = "${ContactsContract.Contacts._ID} = ?"
private const val BIRTHDAY_SELECTION =
    " AND ${ContactsContract.Data.MIMETYPE}= '${ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE}' AND ${ContactsContract.CommonDataKinds.Event.TYPE}=${ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY}"
private const val PHONE_LIST_SELECTION = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
private const val EMAIL_LIST_SELECTION = "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?"
private const val DESCRIPTION_SELECTION = "$SELECTION ? AND ${ContactsContract.Data.MIMETYPE} = ?"

class Contacts(private val context: Context) {

    fun getContactList(): List<Contact> {
        val contactList = mutableListOf<Contact>()
        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME
        ).use { cursor ->
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id: String = (cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts._ID)
                    ))
                    val name: String? =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    var number: String? = null
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) number =
                        getPhonesList(id)[0]
                    val photo =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                    contactList.add(
                        Contact(
                            id = id,
                            name = name,
                            birthday = null,
                            phone1 = number,
                            phone2 = null,
                            email1 = null,
                            email2 = null,
                            description = null,
                            photo = photo
                        )
                    )
                }
            }
        }
        return contactList
    }

    fun getContactById(contactId: String): Contact? {
        var contact: Contact? = null
        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            CURRENT_CONTACT_SELECTION,
            arrayOf(contactId),
            null
        ).use { cursor ->
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val photo: String? =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                    val numbers = getPhonesList(contactId)
                    val emails = getEmailsList(contactId)
                    val description = getDescription(contactId)
                    val birthday = getBirthday(contactId)
                    contact = Contact(
                        id = contactId,
                        name = name,
                        phone1 = numbers.firstOrNull(),
                        phone2 = numbers.getOrNull(1),
                        email1 = emails.firstOrNull(),
                        email2 = emails.getOrNull(1),
                        description = description,
                        photo = photo,
                        birthday = birthday
                    )
                }
            }
        }
        return contact
    }

    private fun getPhonesList(contactId: String): List<String> {
        val phoneList = mutableListOf<String>()
        context.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            PHONE_LIST_SELECTION,
            arrayOf(contactId),
            null
        ).use { cursor ->
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val phoneNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    if (phoneNumber != null) {
                        phoneList.add(phoneNumber)
                    }
                }
            }
        }
        return phoneList
    }

    private fun getEmailsList(contactId: String): List<String> {
        val emailsList = mutableListOf<String>()
        context.contentResolver?.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            EMAIL_LIST_SELECTION,
            arrayOf(contactId),
            null
        ).use { cursor ->
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val email =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                    if (email != null) {
                        emailsList.add(email)
                    }
                }
            }
        }
        return emailsList
    }

    private fun getDescription(id: String): String {
        var description = " "
        context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Note.NOTE),
            DESCRIPTION_SELECTION,
            arrayOf(id),
            null
        ).use { cursor ->
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    description =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE))
                }
            }
        }
        return description
    }

    private fun getBirthday(id: String): Calendar? {
        var birthday = ""
        context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Event.DATA),
            SELECTION + id + BIRTHDAY_SELECTION,
            null,
            null
        ).use { cursor ->
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    birthday =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE))
                }
            }
        }
        return if (birthday.isNotEmpty()) {
            birthday.split("-")
                .let { date ->
                    Calendar.getInstance().apply {
                        set(Calendar.YEAR, date[0].toInt())
                        set(Calendar.MONTH, date[1].toInt() - 1)
                        set(Calendar.DAY_OF_MONTH, date[2].toInt())
                    }
                }
        } else {
            null
        }
    }
}