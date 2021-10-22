package com.classic001.androidcorse.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.classic001.androidcorse.R
import com.classic001.androidcorse.data.Contact
import com.classic001.androidcorse.databinding.FragmentContactListBinding
import com.classic001.androidcorse.interfaces.ContactCardClickListener
import com.classic001.androidcorse.interfaces.ContactListResultListener
import com.classic001.androidcorse.interfaces.ContactServiceSubscriber
import com.classic001.androidcorse.interfaces.ServiceInterface

class ContactListFragment : Fragment(), ContactServiceSubscriber {
    private var listener: ContactCardClickListener? = null
    private var binding: FragmentContactListBinding? = null
    private var serviceInterface: ServiceInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactCardClickListener) {
            listener = context
        }
        if (context is ServiceInterface) {
            serviceInterface = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_contact_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactListBinding.bind(view).apply {
            contactCard.root.setOnClickListener {
                listener?.onClick()
            }
            (activity as AppCompatActivity?)?.supportActionBar?.apply {
                setTitle(R.string.contact_list_bar)
            }
            loadContactList()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    private fun loadContactList() = serviceInterface?.getService()?.getContacts(callback)


    private val callback = object : ContactListResultListener {
        override fun onComplete(result: List<Contact>) {
            try {
                requireActivity().runOnUiThread {
                    binding?.contactCard?.apply {
                        contactName.text = result[0].name
                        contactNum.text = result[0].phone1
                        contactImage.setImageResource(result[0].photo)
                    }
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    override fun onServiceBoundListener() {
        loadContactList()
    }

    companion object {
        fun newInstance() = ContactListFragment()
    }


}