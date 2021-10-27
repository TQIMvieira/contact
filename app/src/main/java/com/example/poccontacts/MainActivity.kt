package com.example.poccontacts

import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var rvContact: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvContact = findViewById(R.id.rv_contact)

        rvContact.layoutManager = LinearLayoutManager(this)
        rvContact.isNestedScrollingEnabled = false

        val contactLoader = ContactLoader(this)

        contactLoader.load().subscribe()

        var list = arrayListOf<Contact>()

        rvContact.adapter = ContactAdapter(list)
    }
}