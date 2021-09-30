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

        var list = arrayListOf<Contact>()

        val cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " COLLATE NOCASE ASC")!!
        if (cur.count > 0) {
            while (cur.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                if (cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt() > 0) {
                    val pCur = contentResolver.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = ?", arrayOf(id), null)!!
                    while (pCur.moveToNext()) {
                        val phoneNumber = pCur.getString(pCur.getColumnIndex(Phone.NUMBER))
                        list.add(Contact(id = id, name = name, phone = phoneNumber))
                    }
                    pCur.close()
                }
            }
        }
        cur.close()

        rvContact.adapter = ContactAdapter(list)
    }
}