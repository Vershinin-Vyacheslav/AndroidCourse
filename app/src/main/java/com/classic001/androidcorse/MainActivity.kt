package com.classic001.androidcorse

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.classic001.androidcorse.fragments.ContactDetailsFragment
import com.classic001.androidcorse.fragments.ContactListFragment
import com.classic001.androidcorse.fragments.EXTRA_CONTACT_ID
import com.classic001.androidcorse.interfaces.ContactCardClickListener
import com.classic001.androidcorse.interfaces.ContactServiceBoundListener
import com.classic001.androidcorse.interfaces.ServiceInterface
import com.classic001.androidcorse.services.ContactService
import java.lang.ref.WeakReference

private const val CONTACT_ID = "13"

class MainActivity : AppCompatActivity(), ContactCardClickListener, ServiceInterface {
    private var contactService: ContactService? = null
    private var bound = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ContactService.ContactBinder
            contactService = binder.getService()
            bound = true
            val weakReferenceFragment = WeakReference(
                supportFragmentManager.findFragmentById(
                    R.id.fragment_container
                )
            )
            when (val fragment = weakReferenceFragment.get()) {
                is ContactServiceBoundListener -> fragment.onServiceBound()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkExtrasAndStart(savedInstanceState)
        bindService(
            Intent(this, ContactService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
        if (savedInstanceState == null) {
            openContactList()
        }
    }

    override fun onDestroy() {
        if (bound) {
            unbindService(connection)
            bound = false
        }
        super.onDestroy()
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

    private fun startFromNotification(intent: Intent?) {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount == 1) {
            fragmentManager.popBackStack()
        }
        val id: String = requireNotNull(intent?.extras?.getString(EXTRA_CONTACT_ID))
        openContactDetailsFragment(id)
    }

    private fun checkExtrasAndStart(savedInstanceState: Bundle?) {
        if (intent.extras?.containsKey(EXTRA_CONTACT_ID) == true && savedInstanceState == null) {
            openContactList()
            startFromNotification(intent)
        } else if (savedInstanceState == null) {
            openContactList()
        }
    }

    override fun onClick() {
        openContactDetailsFragment(CONTACT_ID)
    }

    override fun getService(): ContactService? = contactService
}