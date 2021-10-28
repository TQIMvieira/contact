package com.example.poccontacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter() : PagedListAdapter<Contact, ContactAdapter.ContactViewHolder>(ContactDiffUtilItemCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = DataBindingUtil.inflate<ItemContactBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_contact,
            parent,
            false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.binding.position.text = position.toString()
        if (contact == null) {
            holder.binding.tv_name.text = ""
            holder.binding.tv_phone.text = ""
        } else {
            holder.binding.tv_name.text = contact.name
            holder.binding.tv_phone.text = contact.phone
        }
    }

    class ContactViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)
}