package com.classic001.androidcorse

import android.Manifest
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.classic001.androidcorse.fragments.ContactDetailsFragment
import com.classic001.androidcorse.fragments.ContactListFragment
import com.classic001.androidcorse.fragments.EXTRA_CONTACT_ID
import com.classic001.androidcorse.interfaces.ContactCardClickListener
import com.classic001.androidcorse.interfaces.ContactServiceBoundListener
import com.classic001.androidcorse.interfaces.ServiceInterface
import com.classic001.androidcorse.services.ContactService
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), ContactCardClickListener, ServiceInterface {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkExtrasAndStart(savedInstanceState)
        registerPermissionLauncherListener()
        checkContactReadPermission()
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkContactReadPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED -> {
                bindService(
                    Intent(this, ContactService::class.java),
                    connection,
                    Context.BIND_AUTO_CREATE
                )
            }
            shouldShowRequestPermissionRationale(
                Manifest.permission.READ_CONTACTS
            ) -> {
                val introDialog = AlertDialog.Builder(this)
                introDialog.setTitle(getString(R.string.intro_dialog_title))
                    .setMessage(getString(R.string.intro_dialog_text_message))
                    .setPositiveButton(getString(R.string.intro_dialog_positive_button)) { _, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                introDialog.show()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    private fun registerPermissionLauncherListener() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    bindService(
                        Intent(this, ContactService::class.java),
                        connection,
                        Context.BIND_AUTO_CREATE
                    )
                } else {
                    val applyPermissionOrExitDialog = AlertDialog.Builder(this)
                    applyPermissionOrExitDialog.setTitle(getString(R.string.permission_choice_dialog_title))
                        .setMessage(getString(R.string.permission_choice_dialog_text_message))
                        .setPositiveButton(R.string.permission_choice_dialog_positive_button) { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                        }
                        .setNegativeButton(R.string.permission_choice_dialog_negative_button) { _, _ ->
                            finish()
                        }
                    applyPermissionOrExitDialog.show()
                }
            }
    }

    override fun onClick(id: String) {
        openContactDetailsFragment(id)
    }

    override fun getService(): ContactService? = contactService
}