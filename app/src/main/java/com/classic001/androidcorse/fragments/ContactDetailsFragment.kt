package com.classic001.androidcorse.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.classic001.androidcorse.R
import com.classic001.androidcorse.data.*
import com.classic001.androidcorse.databinding.FragmentContactDetailsBinding
import com.classic001.androidcorse.viewmodels.ContactDetailsViewModel
import java.util.*

const val EXTRA_CONTACT_ID = "CONTACT_ID"
const val CONTACT_NAME = "CONTACT_NAME"

class ContactDetailsFragment : Fragment() {
    private var binding: FragmentContactDetailsBinding? = null
    private var contactId: String = ""
    private var alarmOn = false
    private val viewModel: ContactDetailsViewModel by viewModels()

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


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun loadContactById() {
        viewModel.getContactById(contactId)
            .observe(viewLifecycleOwner, { contact ->
                binding?.apply {
                    contactName.text = contact.name
                    contactNumber1.text = contact.phone1
                    contactNumber2.text = contact.phone2
                    contactMail1.text = contact.email1
                    contactMail2.text = contact.email2
                    contactDescription.text = contact.description
                    contactImage.setImageURI(contact.photo?.toUri())
                    if (contact.birthday != null) {
                        birthday.text = getString(
                            R.string.birthday_date,
                            contact.birthday.get(Calendar.DAY_OF_MONTH),
                            contact.birthday.getDisplayName(
                                Calendar.MONTH,
                                Calendar.SHORT,
                                Locale.getDefault()
                            )
                        )
                        setButtonState(birthdayButton)
                        birthdayButton.setOnClickListener {
                            onBirthdayButtonClick(contact)
                        }
                    } else {
                        birthdayButton.text = getString(R.string.birthday_date_is_empty)
                    }
                }
            })
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

    companion object {
        fun newInstance(id: String): ContactDetailsFragment = ContactDetailsFragment().apply {
            arguments = bundleOf(EXTRA_CONTACT_ID to id)
        }
    }
}