package com.classic001.androidcorse.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.classic001.androidcorse.R

private const val EXTRA_CONTACT_ID = "CONTACT_ID"

class ContactDetailsFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.contact_details_bar)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_contact_details, container, false)

    companion object {
        fun newInstance(id: String): ContactDetailsFragment = ContactDetailsFragment().apply {
            arguments = bundleOf(EXTRA_CONTACT_ID to id)
        }

    }
}