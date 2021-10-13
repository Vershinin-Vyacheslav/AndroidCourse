package com.classic001.androidcorse.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.classic001.androidcorse.R
import com.classic001.androidcorse.databinding.FragmentContactListBinding
import com.classic001.androidcorse.interfaces.ContactCardClickListener

class ContactListFragment : Fragment() {
    private var listener: ContactCardClickListener? = null
    private var binding: FragmentContactListBinding? = null

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
                listener?.onClick()
            }
            (activity as AppCompatActivity?)?.supportActionBar?.apply {
                setTitle(R.string.contact_list_bar)
            }
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

    companion object {
        fun newInstance() = ContactListFragment()
    }
}