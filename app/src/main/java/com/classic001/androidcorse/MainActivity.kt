package com.classic001.androidcorse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.classic001.androidcorse.fragments.ContactDetailsFragment
import com.classic001.androidcorse.fragments.ContactListFragment
import com.classic001.androidcorse.interfaces.ContactCardClickListener

private const val CONTACT_ID = "13"

class MainActivity : AppCompatActivity(), ContactCardClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            openContactList()
        }
    }

    private fun openContactList() {
        val contactListFragment = ContactListFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, contactListFragment)
            .commit()
    }

    private fun openContactDetailsFragment(id: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ContactDetailsFragment.newInstance(id))
            .addToBackStack(null)
            .commit()
    }

    override fun onClick() {
        openContactDetailsFragment(CONTACT_ID)
    }
}