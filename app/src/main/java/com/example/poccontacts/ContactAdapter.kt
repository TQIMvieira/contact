package com.example.poccontacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.poccontacts.databinding.ItemContactBinding

class ContactAdapter() : PagedListAdapter<Contact, ContactAdapter.ContactViewHolder>(ContactDiffUtilItemCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        if (contact == null) {
            holder.binding.tvName.text = ""
            holder.binding.tvPhone.text = ""
        } else {
            holder.binding.tvName.text = contact.name
            holder.binding.tvPhone.text = contact.phone
        }
    }

    class ContactViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)
}