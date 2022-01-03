package com.classic001.androidcorse.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.classic001.androidcorse.R
import com.classic001.androidcorse.databinding.FragmentContactListBinding
import com.classic001.androidcorse.interfaces.ContactCardClickListener
import com.classic001.androidcorse.viewmodels.ContactListViewModel

class ContactListFragment : Fragment() {
    private var listener: ContactCardClickListener? = null
    private var binding: FragmentContactListBinding? = null
    private var contactID = ""
    private val viewModel: ContactListViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactCardClickListener) {
            listener = context
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
                listener?.onClick(contactID)
            }
            (activity as AppCompatActivity?)?.supportActionBar?.apply {
                setTitle(R.string.contact_list_bar)
            }
            updateUI()
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

    private fun updateUI() {
        viewModel.getContactList().observe(viewLifecycleOwner, {
            binding?.contactCard?.apply {
                contactID = it[0].id
                contactName.text = it[0].name
                contactNum.text = it[0].phone1
                contactImage.setImageURI(it[0].photo?.toUri())
            }
        })
    }

    companion object {
        fun newInstance() = ContactListFragment()
    }
}