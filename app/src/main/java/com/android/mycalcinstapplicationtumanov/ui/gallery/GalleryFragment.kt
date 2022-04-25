package com.android.mycalcinstapplicationtumanov.ui.gallery


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mycalcinstapplicationtumanov.data.Contact
import com.android.mycalcinstapplicationtumanov.databinding.FragmentGalleryBinding


class GalleryFragment : Fragment() {

    private var _binding : FragmentGalleryBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        val dashboardViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root : View = binding.root

        /*val textView : TextView = binding.
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvContacts.apply {
            adapter = ListAdapter(context, createContacts())
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun createContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        for (i in 1..20) contacts.add(Contact(i))
        return contacts
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

