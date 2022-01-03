package com.classic001.androidcorse

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.classic001.androidcorse.fragments.ContactDetailsFragment
import com.classic001.androidcorse.fragments.ContactListFragment
import com.classic001.androidcorse.fragments.EXTRA_CONTACT_ID
import com.classic001.androidcorse.interfaces.ContactCardClickListener

class MainActivity : AppCompatActivity(), ContactCardClickListener {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

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
                return
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
                    return@registerForActivityResult
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
}