package com.classic001.androidcorse.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.classic001.androidcorse.R
import com.classic001.androidcorse.data.*
import com.classic001.androidcorse.databinding.FragmentContactDetailsBinding
import com.classic001.androidcorse.interfaces.ContactResultListener
import com.classic001.androidcorse.interfaces.ContactServiceBoundListener
import com.classic001.androidcorse.interfaces.ServiceInterface
import java.util.*

const val EXTRA_CONTACT_ID = "CONTACT_ID"
const val CONTACT_NAME = "CONTACT_NAME"

class ContactDetailsFragment : Fragment(), ContactServiceBoundListener {
    private var binding: FragmentContactDetailsBinding? = null
    private var serviceInterface: ServiceInterface? = null
    private var contactId: String = ""
    private var alarmOn = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ServiceInterface) {
            serviceInterface = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.contact_details_bar)
        binding = FragmentContactDetailsBinding.bind(view)
        contactId = requireArguments().getString(EXTRA_CONTACT_ID).toString()
        loadContactById()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_contact_details, container, false)

    override fun onDetach() {
        serviceInterface = null
        super.onDetach()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun loadContactById() =
        serviceInterface?.getService()?.getContactById(contactId, callback)

    private val callback = object : ContactResultListener {
        override fun onComplete(result: Contact?) {
            result ?: return
            val birthdayString = getString(
                R.string.birthday_date,
                result.birthday?.get(Calendar.DAY_OF_MONTH),
                result.birthday?.getDisplayName(
                    Calendar.MONTH,
                    Calendar.SHORT,
                    Locale.getDefault()
                )
            )
            requireActivity().runOnUiThread {
                binding?.apply {
                    contactName.text = result.name
                    contactNumber1.text = result.phone1
                    contactNumber2.text = result.phone2
                    contactMail1.text = result.email1
                    contactMail2.text = result.email2
                    contactDescription.text = result.description
                    contactImage.setImageDrawable(activity?.let {
                        ContextCompat.getDrawable(
                            it.applicationContext,
                            result.photo
                        )
                    })
                    if (result.birthday != null) {
                        birthday.text = birthdayString
                        setButtonState(birthdayButton)
                        birthdayButton.setOnClickListener {
                            onBirthdayButtonClick(result)
                        }
                    }
                }
            }
        }
    }

    private fun setButtonState(button: Button) {
        alarmOn = isAlarmEnabled(requireContext(), contactId)
        if (!alarmOn) {
            button.text = getString(R.string.birthday_button_on)
        } else {
            button.text = getString(R.string.birthday_button_off)
        }
    }

    private fun onBirthdayButtonClick(currentContact: Contact) {
        if (!alarmOn) {
            alarmOn = true
            binding?.birthdayButton?.text = getString(R.string.birthday_button_off)
            setBirthdayAlarm(requireContext(), currentContact)
        } else {
            alarmOn = false
            binding?.birthdayButton?.text = getString(R.string.birthday_button_on)
            cancelBirthdayAlarm(requireContext(), currentContact)
        }
    }

    override fun onServiceBound() {
        loadContactById()
    }

    companion object {
        fun newInstance(id: String): ContactDetailsFragment = ContactDetailsFragment().apply {
            arguments = bundleOf(EXTRA_CONTACT_ID to id)
        }
    }
}