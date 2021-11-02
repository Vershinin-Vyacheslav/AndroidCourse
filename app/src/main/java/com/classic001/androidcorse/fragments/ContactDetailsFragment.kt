package com.classic001.androidcorse.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.classic001.androidcorse.R
import com.classic001.androidcorse.data.Contact
import com.classic001.androidcorse.databinding.FragmentContactDetailsBinding
import com.classic001.androidcorse.interfaces.ContactResultListener
import com.classic001.androidcorse.interfaces.ContactServiceSubscriber
import com.classic001.androidcorse.interfaces.ServiceInterface

private const val EXTRA_CONTACT_ID = "CONTACT_ID"

class ContactDetailsFragment : Fragment(), ContactServiceSubscriber {
    private var binding: FragmentContactDetailsBinding? = null
    private var serviceInterface: ServiceInterface? = null
    private var contactId: String = ""

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

    private fun loadContactById() = serviceInterface?.getService()?.getContactById(contactId, callback)

    private val callback = object : ContactResultListener {
        override fun onComplete(result: Contact?) {
            try {
                if (result != null) {
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
                        }
                    }
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    override fun onServiceBoundListener() {
        loadContactById()
    }

    companion object {
        fun newInstance(id: String): ContactDetailsFragment = ContactDetailsFragment().apply {
            arguments = bundleOf(EXTRA_CONTACT_ID to id)
        }
    }


}